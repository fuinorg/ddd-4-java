package org.fuin.ddd4j.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.fuin.ddd4j.core.ObjectSerDeserializer;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class JacksonObjectSerDeserializerTest {

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

        final ObjectMapper objectMapper = TestUtils.objectMapper();
        final ObjectSerDeserializer testee = new JacksonObjectSerDeserializer(contentType + "; " + StandardCharsets.UTF_8.name(), objectMapper);

        // TEST
        final byte[] serialized = testee.serialize(original);
        final EncryptedDataJackson copy = testee.deserialize(serialized, EncryptedDataJackson.class);

        // VERIFY
        assertThat(copy.getKeyId()).isEqualTo(keyId);
        assertThat(copy.getKeyVersion()).isEqualTo(keyVersion);
        assertThat(copy.getDataType()).isEqualTo(dataType);
        assertThat(copy.getContentType()).isEqualTo(contentType);
        assertThat(copy.getEncryptedData()).isEqualTo(encryptedData);

    }

}
