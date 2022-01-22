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

import javax.json.bind.annotation.JsonbProperty;
import javax.validation.constraints.NotEmpty;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.fuin.objects4j.common.Immutable;
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
     * @param dataType
     *            Unique type of the data like "UserPersonalData" or even a fully qualified class name.
     * @param contentType
     *            Content/Mime type like "application/json; encoding=UTF-8; version=1".
     * @param encryptedData
     *            Encrypted data.
     */
    public EncryptedData(@NotEmpty final String keyId, @NotEmpty final String keyVersion, @NotEmpty final String dataType,
            @NotEmpty final String contentType, @NotEmpty final byte[] encryptedData) {
        super();
        this.keyId = keyId;
        this.keyVersion = keyVersion;
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
        return "EncryptedData [keyId=" + keyId + ", keyVersion=" + keyVersion + ", dataType=" + dataType + ", contentType=" + contentType
                + "]";
    }

}
