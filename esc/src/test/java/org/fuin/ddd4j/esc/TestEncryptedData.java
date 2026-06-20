/**
 * Copyright (C) 2015 Michael Schnell. All rights reserved.
 * http://www.fuin.org/
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see http://www.gnu.org/licenses/.
 */
package org.fuin.ddd4j.esc;

import org.fuin.objects4j.crypto.EncryptedData;
import org.fuin.utils4j.TestOmitted;

import java.io.Serial;

/**
 * Simple {@link EncryptedData} implementation for tests.
 */
@TestOmitted("Only a test class")
public final class TestEncryptedData implements EncryptedData {

    @Serial
    private static final long serialVersionUID = 1000L;

    private final String keyId;

    private final String keyVersion;

    private final String dataType;

    private final String contentType;

    private final byte[] encryptedData;

    /**
     * Constructor with all data.
     *
     * @param keyId         Key identifier.
     * @param keyVersion    Key version.
     * @param dataType      Original data type.
     * @param contentType   Original content type.
     * @param encryptedData Encrypted bytes.
     */
    public TestEncryptedData(final String keyId, final String keyVersion, final String dataType, final String contentType,
                             final byte[] encryptedData) {
        this.keyId = keyId;
        this.keyVersion = keyVersion;
        this.dataType = dataType;
        this.contentType = contentType;
        this.encryptedData = encryptedData;
    }

    @Override
    public String getKeyId() {
        return keyId;
    }

    @Override
    public String getKeyVersion() {
        return keyVersion;
    }

    @Override
    public String getDataType() {
        return dataType;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public byte[] getEncryptedData() {
        return encryptedData;
    }

}
