package org.fuin.ddd4j.jaxb;

import jakarta.xml.bind.JAXBContext;
import org.fuin.ddd4j.core.ObjectSerDeserializer;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class JaxbObjectSerDeserializerTest {

    @Test
    void testSerializeDeserialize() throws Exception {

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
        final EncryptedDataJaxb original = new EncryptedDataJaxb(keyId, keyVersion, dataType, contentType, encryptedData);

        final JAXBContext jaxbContext = JAXBContext.newInstance(EncryptedDataJaxb.class);
        final ObjectSerDeserializer testee = new JaxbObjectSerDeserializer(jaxbContext);

        // TEST
        final byte[] serialized = testee.serialize(original);
        final EncryptedDataJaxb copy = testee.deserialize(serialized, EncryptedDataJaxb.class);

        // VERIFY
        assertThat(copy.getKeyId()).isEqualTo(keyId);
        assertThat(copy.getKeyVersion()).isEqualTo(keyVersion);
        assertThat(copy.getDataType()).isEqualTo(dataType);
        assertThat(copy.getContentType()).isEqualTo(contentType);
        assertThat(copy.getEncryptedData()).isEqualTo(encryptedData);

    }

}
