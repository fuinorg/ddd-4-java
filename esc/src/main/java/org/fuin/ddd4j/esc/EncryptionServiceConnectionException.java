package org.fuin.ddd4j.esc;

import org.fuin.esc.api.EscConnectionException;
import org.fuin.objects4j.common.NotThreadSafe;
import org.jspecify.annotations.Nullable;

import java.io.Serial;

/**
 * Signals that the external key service could not be reached while encrypting or decrypting the fields of an
 * event - the vault is down, the network is broken, or the call did not answer in time.
 * <p>
 * It extends {@link EscConnectionException} on purpose: an aggregate that cannot be loaded or saved because
 * its key service is unavailable is unavailable, so a consumer classifies it with the same single
 * {@code instanceof EscConnectionException} that covers the event store and the database.
 * <p>
 * <b>The distinction this type exists for is the crypto-shredding invariant.</b> A destroyed key is a
 * permanent answer - the data is gone on purpose and no amount of retrying brings it back - and it must never
 * be confused with a vault that happens to be unreachable right now. Before this type existed both arrived as
 * the same bare {@link RuntimeException}, so a retry policy could not tell them apart: it would either retry
 * a destroyed key forever or never retry a blip. Failures of the key service that are *answers*
 * ({@code EncryptionKeyIdUnknownException}, {@code EncryptionKeyVersionUnknownException},
 * {@code DecryptionFailedException}) therefore stay permanent and are deliberately not reported through this
 * type.
 */
@NotThreadSafe
public final class EncryptionServiceConnectionException extends EscConnectionException {

    @Serial
    private static final long serialVersionUID = 1000L;

    /**
     * Constructor with message and cause.
     *
     * @param message Description of the problem.
     * @param cause   Original failure (may be {@literal null}).
     */
    public EncryptionServiceConnectionException(final String message, @Nullable final Throwable cause) {
        super(message, cause);
    }

}
