package org.fuin.ddd4j.jsonb;

import jakarta.json.bind.Jsonb;
import org.fuin.ddd4j.core.ObjectSerDeserializer;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonbObjectSerDeserializerTest {

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

        final Jsonb jsonb = TestUtils.jsonb();
        final ObjectSerDeserializer testee = new JsonbObjectSerDeserializer(jsonb);

        // TEST
        final byte[] serialized = testee.serialize(original);
        final EncryptedDataJsonb copy = testee.deserialize(serialized, EncryptedDataJsonb.class);

        // VERIFY
        assertThat(copy.getKeyId()).isEqualTo(keyId);
        assertThat(copy.getKeyVersion()).isEqualTo(keyVersion);
        assertThat(copy.getDataType()).isEqualTo(dataType);
        assertThat(copy.getContentType()).isEqualTo(contentType);
        assertThat(copy.getEncryptedData()).isEqualTo(encryptedData);

    }

}
