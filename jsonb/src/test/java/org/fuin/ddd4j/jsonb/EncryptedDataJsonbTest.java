package org.fuin.ddd4j.jsonb;

import jakarta.json.bind.Jsonb;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fuin.ddd4j.jsonb.TestUtils.jsonb;
import static org.fuin.utils4j.Utils4J.deserialize;
import static org.fuin.utils4j.Utils4J.serialize;

public class EncryptedDataJsonbTest {

    @Test
    void testEqualsHashCode() {
        EqualsVerifier.forClass(EncryptedDataJsonb.class).verify();
    }

    @Test
    void testSerializeDeserialize() {

        // PREPARE
        final String keyId = "the/key";
        final String keyVersion = "1";
        final String dataType = "TheSecretData";
        final String contentType = "application/json";
        final byte[] encryptedData = """
                {
                    "lastName" : "Parker",
                    "firstName" : "Peter",
                }
                """.getBytes(StandardCharsets.UTF_8);
        final EncryptedDataJsonb original = new EncryptedDataJsonb(keyId, keyVersion, dataType, contentType, encryptedData);

        // TEST
        final EncryptedDataJsonb copy = deserialize(serialize(original));

        // VERIFY
        assertThat(copy.getKeyId()).isEqualTo(keyId);
        assertThat(copy.getKeyVersion()).isEqualTo(keyVersion);
        assertThat(copy.getDataType()).isEqualTo(dataType);
        assertThat(copy.getContentType()).isEqualTo(contentType);
        assertThat(copy.getEncryptedData()).isEqualTo(encryptedData);

    }

    @Test
    void testMarshalUnmarshal() throws Exception {

        // PREPARE
        final String keyId = "the/key";
        final String keyVersion = "1";
        final String dataType = "TheSecretData";
        final String contentType = "application/json";
        final byte[] encryptedData = """
                {
                    "lastName" : "Parker",
                    "firstName" : "Peter",
                }
                """.getBytes(StandardCharsets.UTF_8);
        final EncryptedDataJsonb original = new EncryptedDataJsonb(keyId, keyVersion, dataType, contentType, encryptedData);

        // TEST
        try (final Jsonb jsonb = jsonb()) {
            final String json = jsonb.toJson(original);
            final EncryptedDataJsonb copy = jsonb.fromJson(json, EncryptedDataJsonb.class);

            // VERIFY
            assertThat(copy.getKeyId()).isEqualTo(keyId);
            assertThat(copy.getKeyVersion()).isEqualTo(keyVersion);
            assertThat(copy.getDataType()).isEqualTo(dataType);
            assertThat(copy.getContentType()).isEqualTo(contentType);
            assertThat(copy.getEncryptedData()).isEqualTo(encryptedData);
        }

    }

    @Test
    public final void testUnmarshal() throws Exception {

        // PREPARE
        final String json = """
                {
                  "key-id" : "the/key",
                  "key-version" : "1",
                  "data-type" : "TheSecretData",
                  "content-type" : "application/json",
                  "encrypted-data" : "ewogICAgImxhc3ROYW1lIiA6ICJQYXJrZXIiLAogICAgImZpcnN0TmFtZSIgOiAiUGV0ZXIiLAp9Cg=="
                }
                """;

        // TEST
        try (final Jsonb jsonb = jsonb()) {
            final EncryptedDataJsonb copy = jsonb.fromJson(json, EncryptedDataJsonb.class);

            // VERIFY
            // VERIFY
            assertThat(copy.getKeyId()).isEqualTo("the/key");
            assertThat(copy.getKeyVersion()).isEqualTo("1");
            assertThat(copy.getDataType()).isEqualTo("TheSecretData");
            assertThat(copy.getContentType()).isEqualTo("application/json");
            assertThat(copy.getEncryptedData()).isEqualTo(
                    Base64.getDecoder().decode("ewogICAgImxhc3ROYW1lIiA6ICJQYXJrZXIiLAogICAgImZpcnN0TmFtZSIgOiAiUGV0ZXIiLAp9Cg=="));

        }

    }

}
