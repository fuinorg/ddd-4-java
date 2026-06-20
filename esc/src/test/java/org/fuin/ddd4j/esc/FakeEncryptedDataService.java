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

import org.fuin.objects4j.crypto.DecryptionFailedException;
import org.fuin.objects4j.crypto.EncryptedData;
import org.fuin.objects4j.crypto.EncryptedDataService;
import org.fuin.utils4j.TestOmitted;

import java.util.HashSet;
import java.util.Set;

/**
 * In-memory reversible {@link EncryptedDataService} for tests. "Encryption" is a symmetric XOR, so any key identifier works
 * and no keys need to be created up front. A key can be {@link #destroyKey(String) destroyed} to simulate crypto-shredding,
 * after which decryption with that key fails.
 */
@TestOmitted("Only a test class")
public final class FakeEncryptedDataService implements EncryptedDataService {

    private static final byte MASK = 0x5A;

    private final Set<String> destroyedKeys = new HashSet<>();

    /**
     * Destroys the key so any later decryption with it fails (simulates GDPR crypto-shredding).
     *
     * @param keyId Identifier of the key to destroy.
     */
    public void destroyKey(final String keyId) {
        destroyedKeys.add(keyId);
    }

    @Override
    public boolean keyExists(final String keyId) {
        return !destroyedKeys.contains(keyId);
    }

    @Override
    public void createKey(final String keyId) {
        // Nothing to do - any key is considered to exist
    }

    @Override
    public String rotateKey(final String keyId) {
        return "1";
    }

    @Override
    public String getKeyVersion(final String keyId) {
        return "1";
    }

    @Override
    public EncryptedData encrypt(final String keyId, final String dataType, final String contentType, final byte[] data) {
        return new TestEncryptedData(keyId, "1", dataType, contentType, xor(data));
    }

    @Override
    public byte[] decrypt(final EncryptedData encryptedData) throws DecryptionFailedException {
        if (destroyedKeys.contains(encryptedData.getKeyId())) {
            throw new DecryptionFailedException("Key '" + encryptedData.getKeyId() + "' was destroyed");
        }
        return xor(encryptedData.getEncryptedData());
    }

    private static byte[] xor(final byte[] data) {
        final byte[] result = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = (byte) (data[i] ^ MASK);
        }
        return result;
    }

}
