package org.fuin.ddd4j.esc;

import org.fuin.esc.api.EscConnectionException;
import org.fuin.esc.api.EventStore;
import org.fuin.esc.mem.InMemoryEventStore;
import org.fuin.objects4j.crypto.DecryptionFailedException;
import org.fuin.objects4j.crypto.EncryptedData;
import org.fuin.objects4j.crypto.EncryptedDataService;
import org.fuin.objects4j.crypto.DuplicateEncryptionKeyIdException;
import org.fuin.objects4j.crypto.EncryptionKeyIdUnknownException;
import org.fuin.objects4j.crypto.EncryptionKeyVersionUnknownException;
import org.junit.jupiter.api.Test;

import java.io.UncheckedIOException;
import java.net.ConnectException;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test for {@link EncryptionServiceConnectionException}.
 * <p>
 * The point of this type is a distinction, so the tests exercise both sides of it: a key service that cannot
 * be reached is transient and may be retried, while a destroyed key is permanent and must never be. Before
 * the type existed both arrived as the same bare {@link RuntimeException}.
 */
public class EncryptionServiceConnectionExceptionTest {

    @Test
    public void testIsClassifiedAsATransientConnectionFailure() {
        final EncryptionServiceConnectionException testee =
                new EncryptionServiceConnectionException("Vault down", new ConnectException("refused"));

        // One instanceof has to cover the store, the database and the key service alike
        assertThat(testee).isInstanceOf(EscConnectionException.class);
        assertThat(testee.getMessage()).isEqualTo("Vault down");
        assertThat(testee.getCause()).isInstanceOf(ConnectException.class);
    }

    @Test
    public void testAnUnreachableKeyServiceIsTransient() throws Exception {

        // PREPARE: an event was written while the vault was up
        try (final EventStore eventStore = new InMemoryEventStore(Executors.newCachedThreadPool()).open()) {
            final UnreachableKeyService service = new UnreachableKeyService();
            final MyCustomerRepository repo =
                    new MyCustomerRepository(eventStore, new GsonObjectSerDeserializer(), service);
            final MyCustomerId customerId = new MyCustomerId();
            repo.update(new MyCustomer(customerId, "John Doe"));

            // TEST: the vault goes away
            service.breakIt();

            // VERIFY: reading the aggregate reports a transient failure, so a caller may retry it. The
            // synchronous repository unwraps the future's CompletionException, so this arrives directly.
            assertThatThrownBy(() -> repo.read(customerId))
                    .isInstanceOf(EncryptionServiceConnectionException.class)
                    .isInstanceOf(EscConnectionException.class)
                    .getRootCause().isInstanceOf(ConnectException.class);
        }
    }

    @Test
    public void testADestroyedKeyRedactsAndIsNeverTransient() throws Exception {

        // PREPARE: crypto-shredding - the key is gone on purpose and the data is unrecoverable
        try (final EventStore eventStore = new InMemoryEventStore(Executors.newCachedThreadPool()).open()) {
            final FakeEncryptedDataService service = new FakeEncryptedDataService();
            final MyCustomerRepository repo =
                    new MyCustomerRepository(eventStore, new GsonObjectSerDeserializer(), service);
            final MyCustomerId customerId = new MyCustomerId();
            repo.update(new MyCustomer(customerId, "John Doe"));
            service.destroyKey(customerId.asString());

            // TEST & VERIFY: shredded data is redacted, not reported as a failure at all - so it can never
            // be mistaken for a vault that is merely unreachable and be retried. The aggregate still loads.
            assertThat(repo.read(customerId).getName()).isEqualTo("***");
        }
    }

    /**
     * Key service whose decryption fails the way an unreachable vault does. Wraps the fake rather than
     * extending it, because that one is final.
     */
    private static final class UnreachableKeyService implements EncryptedDataService {

        private final FakeEncryptedDataService delegate = new FakeEncryptedDataService();

        private volatile boolean broken;

        void breakIt() {
            broken = true;
        }

        @Override
        public byte[] decrypt(final EncryptedData encryptedData) throws DecryptionFailedException,
                EncryptionKeyIdUnknownException, EncryptionKeyVersionUnknownException {
            if (broken) {
                throw new UncheckedIOException(new ConnectException("Connection refused"));
            }
            return delegate.decrypt(encryptedData);
        }

        @Override
        public boolean keyExists(final String keyId) {
            return delegate.keyExists(keyId);
        }

        @Override
        public void createKey(final String keyId) throws DuplicateEncryptionKeyIdException {
            delegate.createKey(keyId);
        }

        @Override
        public String rotateKey(final String keyId) throws EncryptionKeyIdUnknownException {
            return delegate.rotateKey(keyId);
        }

        @Override
        public String getKeyVersion(final String keyId) throws EncryptionKeyIdUnknownException {
            return delegate.getKeyVersion(keyId);
        }

        @Override
        public EncryptedData encrypt(final String keyId, final String dataType, final String contentType,
                                     final byte[] data) throws EncryptionKeyIdUnknownException {
            return delegate.encrypt(keyId, dataType, contentType, data);
        }

    }

}
