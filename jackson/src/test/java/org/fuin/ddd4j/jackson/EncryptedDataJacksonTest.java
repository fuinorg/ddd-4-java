package org.fuin.ddd4j.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.fuin.utils4j.Utils4J.deserialize;
import static org.fuin.utils4j.Utils4J.serialize;

public class EncryptedDataJacksonTest {

    @Test
    void testEqualsHashCode() {
        EqualsVerifier.forClass(EncryptedDataJackson.class).verify();
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
        final EncryptedDataJackson original = new EncryptedDataJackson(keyId, keyVersion, dataType, contentType, encryptedData);

        // TEST
        final EncryptedDataJackson copy = deserialize(serialize(original));

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
        final EncryptedDataJackson original = new EncryptedDataJackson(keyId, keyVersion, dataType, contentType, encryptedData);

        // TEST
        final ObjectMapper objectMapper = TestUtils.objectMapper();

        final String json = objectMapper.writeValueAsString(original);
        final EncryptedDataJackson copy = objectMapper.readValue(json, EncryptedDataJackson.class);

        // VERIFY
        assertThat(copy.getKeyId()).isEqualTo(keyId);
        assertThat(copy.getKeyVersion()).isEqualTo(keyVersion);
        assertThat(copy.getDataType()).isEqualTo(dataType);
        assertThat(copy.getContentType()).isEqualTo(contentType);
        assertThat(copy.getEncryptedData()).isEqualTo(encryptedData);

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
        final ObjectMapper objectMapper = TestUtils.objectMapper();

        final EncryptedDataJackson copy = objectMapper.readValue(json, EncryptedDataJackson.class);

        // VERIFY
        final String copyJson = objectMapper.writeValueAsString(copy);
        assertThatJson(copyJson).isEqualTo(json);

    }

}
