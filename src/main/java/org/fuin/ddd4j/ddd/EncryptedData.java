/**
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

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import org.fuin.objects4j.common.Immutable;
import org.fuin.objects4j.common.Nullable;
import org.fuin.objects4j.vo.ValueObject;

/**
 * Container for encrypted data. In addition to the data itself, the container has information about the key used to encrypt the data and
 * the format of the data. The receiving system must have a notion of what the key identifier, versions and data type means. Equals and hash
 * code is based on all data (value object)
 */
@Immutable
@XmlRootElement(name = "eyncrypted-data")
public final class EncryptedData implements ValueObject, Serializable {

    private static final long serialVersionUID = 1000L;

    @NotEmpty
    @JsonbProperty("key-id")
    @XmlElement(name = "key-id")
    private String keyId;

    @NotEmpty
    @JsonbProperty("key-version")
    @XmlElement(name = "key-version")
    private String keyVersion;

    @Nullable
    @JsonbProperty("iv-version")
    @XmlElement(name = "iv-version")
    private String ivVersion;

    @NotEmpty
    @JsonbProperty("data-type")
    @XmlElement(name = "data-type")
    private String dataType;

    @NotEmpty
    @JsonbProperty("content-type")
    @XmlElement(name = "content-type")
    private String contentType;

    @NotEmpty
    @JsonbProperty("encrypted-data")
    @XmlElement(name = "encrypted-data")
    private byte[] encryptedData;

    /**
     * Default constructor for deserialization (JAX-B/JSON-B).
     */
    protected EncryptedData() {
        super();
    }

    /**
     * Constructor with all mandatory data.
     * 
     * @param keyId
     *            Unique identifier of the private key used.
     * @param keyVersion
     *            Version of the private key used.
     * @param ivVersion
     *            Version of the initialization vector (optional).
     * @param dataType
     *            Unique type of the data like "UserPersonalData" or even a fully qualified class name.
     * @param contentType
     *            Content/Mime type like "application/json; encoding=UTF-8; version=1".
     * @param encryptedData
     *            Encrypted data.
     */
    public EncryptedData(@NotEmpty final String keyId, @NotEmpty final String keyVersion, @Nullable final String ivVersion,
            @NotEmpty final String dataType, @NotEmpty final String contentType, @NotEmpty final byte[] encryptedData) {
        super();
        this.keyId = keyId;
        this.keyVersion = keyVersion;
        this.ivVersion = ivVersion;
        this.dataType = dataType;
        this.contentType = contentType;
        this.encryptedData = encryptedData;
    }

    /**
     * Returns the unique identifier of the private key used.
     * 
     * @return Private key name.
     */
    @NotEmpty
    public final String getKeyId() {
        return keyId;
    }

    /**
     * Returns the version of the private key used.
     * 
     * @return Version.
     */
    @NotEmpty
    public final String getKeyVersion() {
        return keyVersion;
    }

    /**
     * Returns the optional version of the initialization vector.
     * 
     * @return Version or {@literal null}.
     */
    @Nullable
    public final String getIvVersion() {
        return ivVersion;
    }

    /**
     * Returns the unique type of the data like "UserPersonalData".
     * 
     * @return Unique type name.
     */
    @NotEmpty
    public final String getDataType() {
        return dataType;
    }

    /**
     * Returns the content type like "application/json; encoding=UTF-8; version=1".
     * 
     * @return Mime type.
     */
    @NotEmpty
    public final String getContentType() {
        return contentType;
    }

    /**
     * Returns the encrypted data.
     * 
     * @return Data.
     */
    @NotEmpty
    public final byte[] getEncryptedData() {
        return encryptedData;
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((contentType == null) ? 0 : contentType.hashCode());
        result = prime * result + ((dataType == null) ? 0 : dataType.hashCode());
        result = prime * result + Arrays.hashCode(encryptedData);
        result = prime * result + ((ivVersion == null) ? 0 : ivVersion.hashCode());
        result = prime * result + ((keyId == null) ? 0 : keyId.hashCode());
        result = prime * result + ((keyVersion == null) ? 0 : keyVersion.hashCode());
        return result;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EncryptedData other = (EncryptedData) obj;
        if (contentType == null) {
            if (other.contentType != null) {
                return false;
            }
        } else if (!contentType.equals(other.contentType)) {
            return false;
        }
        if (dataType == null) {
            if (other.dataType != null) {
                return false;
            }
        } else if (!dataType.equals(other.dataType)) {
            return false;
        }
        if (!Arrays.equals(encryptedData, other.encryptedData)) {
            return false;
        }
        if (ivVersion == null) {
            if (other.ivVersion != null) {
                return false;
            }
        } else if (!ivVersion.equals(other.ivVersion)) {
            return false;
        }
        if (keyId == null) {
            if (other.keyId != null) {
                return false;
            }
        } else if (!keyId.equals(other.keyId)) {
            return false;
        }
        if (keyVersion == null) {
            if (other.keyVersion != null) {
                return false;
            }
        } else if (!keyVersion.equals(other.keyVersion)) {
            return false;
        }
        return true;
    }

    @Override
    public final String toString() {
        return "EncryptedData [keyId=" + keyId + ", keyVersion=" + keyVersion + ", ivVersion=" + ivVersion + ", dataType=" + dataType
                + ", contentType=" + contentType + "]";
    }

    /**
     * Base functions for the crypto engine.
     */
    public static interface CryptoEngine {

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

    }

    /**
     * Encrypts some data.
     */
    public static interface Encrypter {

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

    }

    /**
     * Decrypts some data.
     */
    public static interface Decrypter {

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

}
