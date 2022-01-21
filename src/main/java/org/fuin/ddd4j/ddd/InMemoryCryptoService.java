package org.fuin.ddd4j.ddd;

import java.io.PrintStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import org.fuin.utils4j.Utils4J;

/**
 * Simple in-memory crypto service for testing purposes.
 */
public final class InMemoryCryptoService implements EncryptedData.CryptoEngine, EncryptedData.Encrypter, EncryptedData.Decrypter {

    /** Key the hash map that has a byte array (byte[]) as value that contains the salt to use for key creation. */
    public static final String PARAM_SALT = "salt";

    /** Key the hash map that has a character array (char[]) as value that contains the password to use for key creation. */
    public static final String PARAM_PASSWORD = "password";

    private Set<String> keyIds;

    private Map<String, Map<Integer, SecretKey>> secretKeys;

    private Map<String, Map<Integer, IvParameterSpec>> ivParameterSpecs;

    /**
     * Default constructor.
     */
    public InMemoryCryptoService() {
        super();
        this.keyIds = new HashSet<>();
        this.secretKeys = new HashMap<>();
        this.ivParameterSpecs = new HashMap<>();
    }

    /**
     * Creates a new entry.
     * 
     * @param keyId
     *            Unique key identifier.
     * @param pw
     *            Password to use for secret key creation.
     * @param salt
     *            Salt to use for key creation.
     */
    private void createEntry(final String keyId, final char[] pw, final byte[] salt) {
        nextSecretKeyIntern(keyId, pw, salt);
        nextIvParameterSpecIntern(keyId);
        keyIds.add(keyId);
    }

    private int nextSecretKeyIntern(final String keyId, final char[] pw, final byte[] salt) {
        final Map<Integer, SecretKey> keyVersions = secretKeys.computeIfAbsent(keyId, k -> new HashMap<>());
        final int nextVersion = calculateNextVersion(keyVersions);
        keyVersions.computeIfAbsent(nextVersion, k -> createSecretKey(pw, salt));
        return nextVersion;
    }

    /**
     * Creates a new IV version.
     * 
     * @param keyId
     *            Unique key identifier.
     * 
     * @return Newly created IV version.
     */
    public int nextIvParameterSpec(final String keyId) {
        if (!keyIds.contains(keyId)) {
            throw new IllegalArgumentException("Unknown key: " + keyId);
        }
        return nextIvParameterSpecIntern(keyId);
    }

    /**
     * Prints known key IDs and versions on the given stream.
     * 
     * @param out
     *            Stream to print information to.
     */
    public void printStatus(final PrintStream out) {
        keyIds.forEach(keyId -> {
            out.println("KEY: " + keyId);

            out.print("  SECRET KEYS: ");
            final Map<Integer, SecretKey> keyVersions = secretKeys.get(keyId);
            keyVersions.keySet().forEach(version -> out.print(version + ", "));
            out.println();

            out.print("  IV PARAM SPECS: ");
            final Map<Integer, IvParameterSpec> ivVersions = ivParameterSpecs.get(keyId);
            ivVersions.keySet().forEach(version -> out.print(version + ", "));
            out.println();

        });
    }

    private int nextIvParameterSpecIntern(final String keyId) {
        final Map<Integer, IvParameterSpec> ivVersions = ivParameterSpecs.computeIfAbsent(keyId, k -> new HashMap<>());
        final int nextVersion = calculateNextVersion(ivVersions);
        ivVersions.computeIfAbsent(nextVersion, k -> createIvParameterSpec());
        return nextVersion;
    }

    private static Integer findLatestVersion(final Map<Integer, ?> map) {
        int latestVersion = 0;
        final Iterator<Integer> it = map.keySet().iterator();
        while (it.hasNext()) {
            final Integer version = it.next();
            if (version > latestVersion) {
                latestVersion = version;
            }
        }
        return latestVersion;
    }

    private static int calculateNextVersion(final Map<Integer, ?> map) {
        return findLatestVersion(map) + 1;
    }

    private static IvParameterSpec createIvParameterSpec() {
        final byte[] ivBytes = new byte[16];
        new SecureRandom().nextBytes(ivBytes);
        return new IvParameterSpec(ivBytes);
    }

    private static SecretKey createSecretKey(final char[] pw, final byte[] salt) {
        try {
            final SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            final KeySpec spec = new PBEKeySpec(pw, salt, 65536, 256);
            return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        } catch (final NoSuchAlgorithmException | InvalidKeySpecException ex) {
            throw new RuntimeException("Failed to create secret key", ex);
        }
    }

    @Override
    public boolean keyExists(@NotEmpty String keyId) {
        return keyIds.contains(keyId);
    }

    @Override
    public void createKey(@NotEmpty String keyId, Map<String, Object> params) throws DuplicateEncryptionKeyIdException {
        Utils4J.checkNotEmpty(keyId, keyId);
        if (keyIds.contains(keyId)) {
            throw new DuplicateEncryptionKeyIdException(keyId);
        }
        final PwSalt result = verifyParams(params);
        createEntry(keyId, result.getPw(), result.getSalt());
    }

    @Override
    public String rotateKey(@NotEmpty String keyId, Map<String, Object> params) throws EncryptionKeyIdUnknownException {
        Utils4J.checkNotEmpty(keyId, keyId);
        if (!keyIds.contains(keyId)) {
            throw new EncryptionKeyIdUnknownException(keyId);
        }
        final PwSalt result = verifyParams(params);
        return "" + nextSecretKeyIntern(keyId, result.getPw(), result.getSalt());
    }

    @Override
    public String getKeyVersion(@NotEmpty String keyId) throws EncryptionKeyIdUnknownException {
        Utils4J.checkNotEmpty(keyId, keyId);
        if (!keyIds.contains(keyId)) {
            throw new EncryptionKeyIdUnknownException(keyId);
        }
        final Map<Integer, SecretKey> keyVersions = secretKeys.get(keyId);
        return "" + findLatestVersion(keyVersions);
    }

    @Override
    public EncryptedData encrypt(@NotEmpty String keyId, @NotEmpty String dataType, @NotEmpty String contentType, @NotEmpty byte[] data)
            throws EncryptionKeyIdUnknownException {

        if (!keyIds.contains(keyId)) {
            throw new EncryptionKeyIdUnknownException(keyId);
        }

        final Map<Integer, SecretKey> keyVersions = secretKeys.get(keyId);
        final int keyVersion = findLatestVersion(keyVersions);
        final SecretKey secretKey = keyVersions.get(keyVersion);
        final Map<Integer, IvParameterSpec> ivVersions = ivParameterSpecs.get(keyId);
        final int ivVersion = findLatestVersion(ivVersions);
        final IvParameterSpec ivParameterSpec = ivVersions.get(ivVersion);

        try {
            final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
            byte[] encryptedData = cipher.doFinal(data);
            return new EncryptedData(keyId, "" + keyVersion, "" + ivVersion, dataType, contentType, encryptedData);
        } catch (final NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException
                | IllegalBlockSizeException | BadPaddingException ex) {
            throw new RuntimeException("Failed to encrypt data of type '" + dataType + "' with key '" + keyId + "'", ex);
        }

    }

    @Override
    public @NotEmpty byte[] decrypt(@NotNull EncryptedData encryptedData) throws EncryptionKeyIdUnknownException,
            EncryptionKeyVersionUnknownException, EncryptionIvVersionUnknownException, DecryptionFailedException {

        final String keyId = encryptedData.getKeyId();
        if (!keyIds.contains(keyId)) {
            throw new EncryptionKeyIdUnknownException(keyId);
        }

        final Map<Integer, SecretKey> keyVersions = secretKeys.get(keyId);
        final int keyVersion = Integer.parseInt(encryptedData.getKeyVersion());
        final SecretKey secretKey = keyVersions.get(keyVersion);
        if (secretKey == null) {
            throw new EncryptionKeyVersionUnknownException(encryptedData.getKeyVersion());
        }
        final Map<Integer, IvParameterSpec> ivVersions = ivParameterSpecs.get(keyId);
        final int ivVersion = Integer.parseInt(encryptedData.getIvVersion());
        final IvParameterSpec ivParameterSpec = ivVersions.get(ivVersion);
        if (ivParameterSpec == null) {
            throw new EncryptionIvVersionUnknownException(encryptedData.getIvVersion());
        }

        try {
            final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            return cipher.doFinal(encryptedData.getEncryptedData());
        } catch (final InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException
                | NoSuchAlgorithmException | NoSuchPaddingException ex) {
            throw new DecryptionFailedException();
        }

    }

    private PwSalt verifyParams(final Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            throw new IllegalArgumentException("The implementation requires parameters, but got none");
        }
        final Object pwObj = params.get(PARAM_PASSWORD);
        if (pwObj == null) {
            throw new IllegalArgumentException("The argument '" + PARAM_PASSWORD + "' is required");
        }
        if (!(pwObj instanceof char[])) {
            throw new IllegalArgumentException("The argument '" + PARAM_PASSWORD + "' is expected to be of type 'char[]', but was: " + pwObj.getClass());
        }
        final char[] pw = (char[]) pwObj;
        final Object saltObj = params.get(PARAM_SALT);
        if (saltObj == null) {
            throw new IllegalArgumentException("The argument '" + PARAM_SALT + "' is required");
        }
        if (!(saltObj instanceof byte[])) {
            throw new IllegalArgumentException("The argument '" + PARAM_SALT + "' is expected to be of type 'byte[]', but was: " + saltObj.getClass());
        }
        final byte[] salt = (byte[]) saltObj;
        return new PwSalt(pw, salt);
    }

    private static final class PwSalt {

        private final char[] pw;

        private final byte[] salt;

        public PwSalt(char[] pw, byte[] salt) {
            super();
            this.pw = pw;
            this.salt = salt;
        }

        public char[] getPw() {
            return pw;
        }

        public byte[] getSalt() {
            return salt;
        }

    }

}
