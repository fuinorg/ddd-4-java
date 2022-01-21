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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Test for the {@link InMemoryCryptoService} class.
 */
public class InMemoryCryptoServiceTest {

    @Test
    public void testCreateKey() throws DuplicateEncryptionKeyIdException, EncryptionKeyIdUnknownException {

        // PREPARE
        final String keyId = "michael";
        final Map<String, Object> params = new HashMap<>();
        params.put(InMemoryCryptoService.PARAM_PASSWORD, "abc".toCharArray());
        params.put(InMemoryCryptoService.PARAM_SALT, "123".getBytes());
        final InMemoryCryptoService testee = new InMemoryCryptoService();

        // TEST
        testee.createKey(keyId, params);

        // VERIFY
        assertThat(testee.keyExists(keyId)).isTrue();
        assertThat(testee.getKeyVersion(keyId)).isEqualTo("1");

    }

    @Test
    public void testCreateKeyNoParams() throws DuplicateEncryptionKeyIdException, EncryptionKeyIdUnknownException {

        // PREPARE
        final String keyId = "michael";
        final Map<String, Object> params = new HashMap<>();
        final InMemoryCryptoService testee = new InMemoryCryptoService();

        // TEST
        try {
            testee.createKey(keyId, params);
            fail("Expected exception");
        } catch (final IllegalArgumentException ex) {
            assertThat(ex.getMessage()).isEqualTo("The implementation requires parameters, but got none");
        }

    }

    @Test
    public void testCreateKeyWrongPasswordParam() throws DuplicateEncryptionKeyIdException, EncryptionKeyIdUnknownException {

        // PREPARE
        final String keyId = "michael";
        final Map<String, Object> params = new HashMap<>();
        final InMemoryCryptoService testee = new InMemoryCryptoService();

        // TEST
        try {
            params.put(InMemoryCryptoService.PARAM_PASSWORD, "abc");
            params.put(InMemoryCryptoService.PARAM_SALT, "123".getBytes());
            testee.createKey(keyId, params);
            fail("Expected exception");
        } catch (final IllegalArgumentException ex) {
            assertThat(ex.getMessage())
                    .isEqualTo("The argument 'password' is expected to be of type 'char[]', but was: class java.lang.String");
        }
    }

    @Test
    public void testCreateKeyWrongSaltParam() throws DuplicateEncryptionKeyIdException, EncryptionKeyIdUnknownException {

        // PREPARE
        final String keyId = "michael";
        final Map<String, Object> params = new HashMap<>();
        final InMemoryCryptoService testee = new InMemoryCryptoService();
        params.put(InMemoryCryptoService.PARAM_PASSWORD, "abc".toCharArray());
        params.put(InMemoryCryptoService.PARAM_SALT, "123");

        // TEST
        try {
            testee.createKey(keyId, params);
            fail("Expected exception");
        } catch (final IllegalArgumentException ex) {
            assertThat(ex.getMessage())
                    .isEqualTo("The argument 'salt' is expected to be of type 'byte[]', but was: class java.lang.String");
        }

    }

    @Test
    public void testCreateKeyNoPasswordParam() throws DuplicateEncryptionKeyIdException, EncryptionKeyIdUnknownException {

        // PREPARE
        final String keyId = "michael";
        final Map<String, Object> params = new HashMap<>();
        final InMemoryCryptoService testee = new InMemoryCryptoService();

        // TEST
        try {
            params.put(InMemoryCryptoService.PARAM_SALT, "123".getBytes());
            testee.createKey(keyId, params);
            fail("Expected exception");
        } catch (final IllegalArgumentException ex) {
            assertThat(ex.getMessage()).isEqualTo("The argument 'password' is required");
        }
    }

    @Test
    public void testCreateKeyNoSaltParam() throws DuplicateEncryptionKeyIdException, EncryptionKeyIdUnknownException {

        // PREPARE
        final String keyId = "michael";
        final Map<String, Object> params = new HashMap<>();
        final InMemoryCryptoService testee = new InMemoryCryptoService();
        params.put(InMemoryCryptoService.PARAM_PASSWORD, "abc".toCharArray());

        // TEST
        try {
            testee.createKey(keyId, params);
            fail("Expected exception");
        } catch (final IllegalArgumentException ex) {
            assertThat(ex.getMessage()).isEqualTo("The argument 'salt' is required");
        }

    }

    @Test
    public void testCreateKeyDuplicate() {

        // PREPARE
        final String keyId = "michael";
        final Map<String, Object> params = new HashMap<>();
        params.put(InMemoryCryptoService.PARAM_PASSWORD, "abc".toCharArray());
        params.put(InMemoryCryptoService.PARAM_SALT, "123".getBytes());
        final InMemoryCryptoService testee = new InMemoryCryptoService();
        try {
            testee.createKey(keyId, params);
        } catch (final DuplicateEncryptionKeyIdException ex) {
            throw new RuntimeException(ex);
        }

        // TEST
        try {
            testee.createKey(keyId, params);
            fail("Expected exception");
        } catch (final DuplicateEncryptionKeyIdException ex) {
            assertThat(ex.getMessage()).isEqualTo("Duplicate keyId: michael");
        }

    }

    @Test
    public void testGetKeyVersionUnknown() throws DuplicateEncryptionKeyIdException {

        // PREPARE
        final String keyId = "michael";
        final InMemoryCryptoService testee = new InMemoryCryptoService();

        // TEST
        try {
            testee.getKeyVersion(keyId);
            fail("Expected exception");
        } catch (final EncryptionKeyIdUnknownException ex) {
            assertThat(ex.getMessage()).isEqualTo("Unknown keyId: michael");
        }
    }

    @Test
    public void testRotateKey() throws DuplicateEncryptionKeyIdException, EncryptionKeyIdUnknownException {

        // PREPARE
        final String keyId = "michael";
        final Map<String, Object> params = new HashMap<>();
        params.put(InMemoryCryptoService.PARAM_PASSWORD, "abc".toCharArray());
        params.put(InMemoryCryptoService.PARAM_SALT, "123".getBytes());
        final InMemoryCryptoService testee = new InMemoryCryptoService();
        testee.createKey(keyId, params);
        assertThat(testee.keyExists(keyId)).isTrue();
        assertThat(testee.getKeyVersion(keyId)).isEqualTo("1");

        // TEST
        testee.rotateKey(keyId, params);

        // VERIFY
        assertThat(testee.getKeyVersion(keyId)).isEqualTo("2");

    }

    @Test
    public void testRotateKeyUnknown() {

        // PREPARE
        final String keyId = "michael";
        final Map<String, Object> params = new HashMap<>();
        params.put(InMemoryCryptoService.PARAM_PASSWORD, "abc".toCharArray());
        params.put(InMemoryCryptoService.PARAM_SALT, "123".getBytes());
        final InMemoryCryptoService testee = new InMemoryCryptoService();

        // TEST & VERIFY
        try {
            testee.rotateKey(keyId, params);
            fail("Expected exception");
        } catch (final EncryptionKeyIdUnknownException ex) {
            assertThat(ex.getMessage()).isEqualTo("Unknown keyId: michael");
        }

    }

    @Test
    public void testEncryptDecrypt() throws EncryptionKeyIdUnknownException, DuplicateEncryptionKeyIdException,
            EncryptionKeyVersionUnknownException, EncryptionIvVersionUnknownException, DecryptionFailedException {

        // PREPARE
        final String keyId = "michael";
        final Map<String, Object> params = new HashMap<>();
        params.put(InMemoryCryptoService.PARAM_PASSWORD, "abc".toCharArray());
        params.put(InMemoryCryptoService.PARAM_SALT, "123".getBytes());
        final InMemoryCryptoService testee = new InMemoryCryptoService();
        testee.createKey(keyId, params);
        final String plainText = "Hello, world!";
        final String contentType = "text/plain";
        final String dataType = "MyText";

        // TEST
        final EncryptedData encryptedData = testee.encrypt(keyId, dataType, contentType, plainText.getBytes(StandardCharsets.UTF_8));

        // VERIFY
        assertThat(encryptedData.getKeyId()).isEqualTo(keyId);
        assertThat(encryptedData.getContentType()).isEqualTo(contentType);
        assertThat(encryptedData.getDataType()).isEqualTo(dataType);
        assertThat(encryptedData.getKeyVersion()).isEqualTo("1");
        assertThat(encryptedData.getIvVersion()).isEqualTo("1");
        assertThat(encryptedData.getEncryptedData()).isNotEmpty();

        // TEST
        final byte[] decryptedData = testee.decrypt(encryptedData);

        // VERIFY
        assertThat(new String(decryptedData)).isEqualTo(plainText);

    }

    @Test
    public void testEncryptUnknownKeyId() throws DuplicateEncryptionKeyIdException, EncryptionKeyVersionUnknownException,
            EncryptionIvVersionUnknownException, DecryptionFailedException {

        // PREPARE
        final String keyId = "michael";
        final String plainText = "Hello, world!";
        final String contentType = "text/plain";
        final String dataType = "MyText";
        final InMemoryCryptoService testee = new InMemoryCryptoService();

        // TEST
        try {
            testee.encrypt(keyId, dataType, contentType, plainText.getBytes(StandardCharsets.UTF_8));
            fail("Expected exception");
        } catch (final EncryptionKeyIdUnknownException ex) {
            assertThat(ex.getMessage()).isEqualTo("Unknown keyId: michael");
        }

    }

    @Test
    public void testDecryptUnknownKeyId() throws DuplicateEncryptionKeyIdException, EncryptionKeyVersionUnknownException,
            EncryptionIvVersionUnknownException, DecryptionFailedException, EncryptionKeyIdUnknownException {

        // PREPARE
        final InMemoryCryptoService testee = new InMemoryCryptoService();
        final EncryptedData encryptedData = new EncryptedData("michael", "1", "1", "MyData", "text/plain", new byte[] { 0 });

        // TEST
        try {
            testee.decrypt(encryptedData);
            fail("Expected exception");
        } catch (final EncryptionKeyIdUnknownException ex) {
            assertThat(ex.getMessage()).isEqualTo("Unknown keyId: michael");
        }

    }

    @Test
    public void testDecryptUnknownVersion() throws EncryptionKeyIdUnknownException, DuplicateEncryptionKeyIdException,
            EncryptionIvVersionUnknownException, DecryptionFailedException {

        // PREPARE
        final String keyId = "michael";
        final Map<String, Object> params = new HashMap<>();
        params.put(InMemoryCryptoService.PARAM_PASSWORD, "abc".toCharArray());
        params.put(InMemoryCryptoService.PARAM_SALT, "123".getBytes());
        final InMemoryCryptoService testee = new InMemoryCryptoService();
        testee.createKey(keyId, params);
        final String plainText = "Hello, world!";
        final String contentType = "text/plain";
        final String dataType = "MyText";
        final EncryptedData encryptedData = testee.encrypt(keyId, dataType, contentType, plainText.getBytes(StandardCharsets.UTF_8));
        final EncryptedData wrongData = new EncryptedData(encryptedData.getKeyId(), "2", encryptedData.getIvVersion(),
                encryptedData.getDataType(), encryptedData.getContentType(), encryptedData.getEncryptedData());

        // TEST
        try {
            testee.decrypt(wrongData);
            fail("Expected exception");
        } catch (final EncryptionKeyVersionUnknownException ex) {
            assertThat(ex.getMessage()).isEqualTo("Unknown keyVersion: 2");
        }

    }

}
