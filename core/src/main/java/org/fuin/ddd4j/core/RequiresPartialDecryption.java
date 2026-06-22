package org.fuin.ddd4j.core;


import org.fuin.objects4j.crypto.DecryptionFailedException;
import org.fuin.objects4j.crypto.EncryptedDataService;
import org.fuin.objects4j.crypto.EncryptionKeyVersionUnknownException;
import org.fuin.objects4j.common.ThreadSafe;

import java.io.IOException;

/**
 * Tags an object that requires decryption for some of its fields.
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
