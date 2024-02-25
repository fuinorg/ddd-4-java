package org.fuin.ddd4j.jaxb;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fuin.utils4j.Utils4J.deserialize;
import static org.fuin.utils4j.Utils4J.serialize;
import static org.fuin.utils4j.jaxb.JaxbUtils.marshal;
import static org.fuin.utils4j.jaxb.JaxbUtils.unmarshal;

public class EncryptedDataJaxbTest {

    @Test
    void testEqualsHashCode() {
        EqualsVerifier.forClass(EncryptedDataJaxb.class).verify();
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
        final EncryptedDataJaxb original = new EncryptedDataJaxb(keyId, keyVersion, dataType, contentType, encryptedData);

        // TEST
        final EncryptedDataJaxb copy = deserialize(serialize(original));

        // VERIFY
        assertThat(copy.getKeyId()).isEqualTo(keyId);
        assertThat(copy.getKeyVersion()).isEqualTo(keyVersion);
        assertThat(copy.getDataType()).isEqualTo(dataType);
        assertThat(copy.getContentType()).isEqualTo(contentType);
        assertThat(copy.getEncryptedData()).isEqualTo(encryptedData);

    }

    @Test
    void testMarshalUnmarshal() {

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

        // TEST
        final String xml = marshal(original, EncryptedDataJaxb.class);
        final EncryptedDataJaxb copy = unmarshal(xml, EncryptedDataJaxb.class);

        // VERIFY
        assertThat(copy.getKeyId()).isEqualTo(keyId);
        assertThat(copy.getKeyVersion()).isEqualTo(keyVersion);
        assertThat(copy.getDataType()).isEqualTo(dataType);
        assertThat(copy.getContentType()).isEqualTo(contentType);
        assertThat(copy.getEncryptedData()).isEqualTo(encryptedData);

    }

    @Test
    public final void testUnmarshal() {

        // PREPARE
        final String xml = """
                <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                <eyncrypted-data>
                  <key-id>the/key</key-id>
                  <key-version>1</key-version>
                  <data-type>TheSecretData</data-type>
                  <content-type>application/json</content-type>
                  <encrypted-data>ewogICAgImxhc3ROYW1lIiA6ICJQYXJrZXIiLAogICAgImZpcnN0TmFtZSIgOiAiUGV0ZXIiLAp9Cg==</encrypted-data>
                </eyncrypted-data>
                """;

        // TEST
        final EncryptedDataJaxb copy = unmarshal(xml, EncryptedDataJaxb.class);

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
