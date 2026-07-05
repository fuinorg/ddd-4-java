package org.fuin.ddd4j.core;

import org.fuin.objects4j.crypto.EncryptedDataService;
import org.fuin.objects4j.crypto.EncryptionKeyIdUnknownException;
import org.fuin.objects4j.common.ThreadSafe;

/**
 * Tags a domain event that carries personal data in some of its fields and must be stored with those fields
 * encrypted. This is the write side of the <b>crypto-shredding</b> pattern used for GDPR-style erasure:
 * <ol>
 * <li>Encrypt the personal fields with a key that is unique <em>per subject</em> - by convention
 * {@code keyId = aggregateId.asString()} (the subject/aggregate root owns its key).</li>
 * <li>{@link #encrypt(ObjectSerDeserializer, EncryptedDataService)} returns a separate encrypted variant of
 * the event (implementing {@link RequiresPartialDecryption}) that is what actually gets written to the store;
 * only the personal fields move into an {@code EncryptedData} envelope, the rest (e.g. the id) is copied
 * as-is.</li>
 * <li>To erase a subject later, destroy its key in the key store ("forget the key"); the stored events can no
 * longer be decrypted and are redacted on read (see {@link RequiresPartialDecryption}). Signal downstream
 * consumers to purge derived data by appending a {@link RemovedPrivateData} event.</li>
 * </ol>
 * The repository applies this automatically on append - see {@code EventStoreRepository} in the {@code esc}
 * module, which encrypts any appended event implementing this interface.
 * <p>
 * All implementations are expected to be thread safe.
 *
 * @param <T> Type of the unencrypted data.
 * @param <E> Type of the encrypted data.
 */
@ThreadSafe
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
