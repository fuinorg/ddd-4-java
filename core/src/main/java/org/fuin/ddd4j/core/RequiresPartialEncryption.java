package org.fuin.ddd4j.core;

import org.fuin.objects4j.crypto.EncryptedDataService;
import org.fuin.objects4j.crypto.EncryptionKeyIdUnknownException;

/**
 * Tags an object that requires encryption for some of its fields.
 *
 * @param <T> Type of the unencrypted data.
 * @param <E> Type of the encrypted data.
 */
public interface RequiresPartialEncryption<T, E> {

    /**
     * Replaces fields in the input structure with an encrypted version.
     *
     * @param serDeserializer Used to serialize the data to bytes.
     * @param service         Service that supports encryption.
     * @return Similar type of object, but with necessary fields encrypted.
     * @throws EncryptionKeyIdUnknownException No key found that could be used to encrypt fields.
     */
    E encrypt(ObjectSerDeserializer serDeserializer, EncryptedDataService service) throws EncryptionKeyIdUnknownException;

}
