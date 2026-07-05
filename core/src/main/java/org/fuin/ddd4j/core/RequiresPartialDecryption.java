package org.fuin.ddd4j.core;


import org.fuin.objects4j.crypto.DecryptionFailedException;
import org.fuin.objects4j.crypto.EncryptedDataService;
import org.fuin.objects4j.crypto.EncryptionKeyVersionUnknownException;
import org.fuin.objects4j.common.ThreadSafe;

import java.io.IOException;

/**
 * Tags the encrypted variant of a domain event (the counterpart of {@link RequiresPartialEncryption}) so the
 * repository can restore the personal fields on read. This is the read side of the <b>crypto-shredding</b>
 * pattern; the repository ({@code EventStoreRepository} in the {@code esc} module) calls
 * {@link #decrypt(ObjectSerDeserializer, EncryptedDataService)} on any event implementing this interface after
 * it is read from the store.
 * <p>
 * <b>Redact-on-key-loss convention:</b> a subject is erased by destroying its key ("forget the key"). After
 * that, {@code decrypt(...)} can no longer recover the personal fields. Implementations should therefore
 * <em>catch</em> the key-loss exceptions ({@link org.fuin.objects4j.crypto.EncryptionKeyIdUnknownException},
 * {@link EncryptionKeyVersionUnknownException}, {@link DecryptionFailedException}) and return a
 * <em>redacted</em> plain event (for example replacing the name with {@code "***"}) rather than propagating
 * the failure - so a shredded subject's stream stays readable-but-anonymized instead of breaking replay.
 * <p>
 * All implementations are expected to be thread safe.
 *
 * @param <E> Type of the encrypted data.
 * @param <T> Type of the unencrypted data.
 */
@ThreadSafe
public interface RequiresPartialDecryption<E, T> {

    /**
     * Replaces the encrypted fields in the input structure with their decrypted version.
     *
     * @param serDeserializer Used to deserialize the decrypted data from bytes.
     * @param service         Service that supports decryption.
     * @return Similar type of object, but with necessary fields decrypted.
     */
    T decrypt(ObjectSerDeserializer serDeserializer, EncryptedDataService service)
            throws EncryptionKeyVersionUnknownException, DecryptionFailedException, IOException;

}
