/*
 * Copyright (C) 2015 Michael Schnell. All rights reserved. 
 * http://www.fuin.org/
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see http://www.gnu.org/licenses/.
 */
package org.fuin.ddd4j.ddd;

import java.util.Map;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Service for encrypting/decrypting {@link EncryptedData} and handling versioned secret keys. 
 */
public interface EncryptedDataService {

    /**
     * Determines if a key for the given identifier exists.
     * 
     * @param keyId
     *            Identifier to test for.
     * 
     * @return TRUE if the key is known and can be used for encryption.
     */
    public boolean keyExists(@NotEmpty String keyId);

    /**
     * Creates a new key for the given identifier.
     * 
     * @param keyId
     *            Identifier to create a new secret key for.
     * @param params
     *            Parameters for the key creation. The implementation defines the (optional) key/values.
     * 
     * @throws DuplicateEncryptionKeyIdException
     *             The given ID already exists and a key cannot be created again.
     */
    public void createKey(@NotEmpty String keyId, Map<String, Object> params) throws DuplicateEncryptionKeyIdException;

    /**
     * Rotates the existing key by creating a new one as the next version.
     * 
     * @param keyId
     *            Key identifier to create a new secret key.
     * @param params
     *            Parameters for the key creation. The implementation defines the (optional) key/values.
     * 
     * @return The new version of the key.
     * 
     * @throws EncryptionKeyIdUnknownException
     *             The given key identifier is unknown.
     */
    public String rotateKey(@NotEmpty String keyId, Map<String, Object> params) throws EncryptionKeyIdUnknownException;

    /**
     * Returns the current version of the given identifier.
     * 
     * @param keyId
     *            Key ID to return the version for.
     * 
     * @return Version of the given identifier.
     * 
     * @throws EncryptionKeyIdUnknownException
     *             The given key identifier is unknown.
     */
    public String getKeyVersion(@NotEmpty String keyId) throws EncryptionKeyIdUnknownException;

    
    /**
     * Encrypts some data using a dedicated key. The encrypter will use the latest available version of key and initialization vector.
     * The only arguments used actively for encryption are the <code>keyId</code> and the <code>data</code> byte array. The rest of the
     * arguments is only stored for information purposes in the resulting data structure.
     * 
     * @param keyId
     *            Unique identifier of a key to use.
     * @param dataType
     *            Unique type of the data like "UserPersonalData" or even a fully qualified class name.
     * @param contentType
     *            Content type like "application/json; encoding=UTF-8; version=1".
     * @param data
     *            Data to encrypt.
     * 
     * @return Encrypted data.
     * 
     * @throws EncryptionKeyIdUnknownException
     *             The given key identifier is unknown.
     */
    public EncryptedData encrypt(@NotEmpty String keyId, @NotEmpty String dataType, @NotEmpty String contentType, @NotEmpty byte[] data)
            throws EncryptionKeyIdUnknownException;

    /**
     * Decrypts the data using the information provided by the parameter. The data itself will only be decrypted, means no
     * transformation in regard to mime and data type will take place. It's up to the caller to use this information to transform the
     * returned byte array to the target type.
     * 
     * @param encryptedData
     *            Encrypted data and meta information about it.
     * 
     * @return Decrypted data.
     * 
     * @throws EncryptionKeyIdUnknownException
     *             The given key identifier is unknown.
     * @throws EncryptionKeyVersionUnknownException
     *             The given version of the key is unknown.
     * @throws EncryptionIvVersionUnknownException
     *             The given initialization vector version is unknown.
     * @throws DecryptionFailedException
     *             Decrypting the data using they key, version and (optional) IV version failed.
     */
    @NotEmpty
    public byte[] decrypt(@NotNull EncryptedData encryptedData) throws EncryptionKeyIdUnknownException,
            EncryptionKeyVersionUnknownException, EncryptionIvVersionUnknownException, DecryptionFailedException;
    
}
