package org.fuin.ddd4j.jaxb;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.fuin.ddd4j.core.EncryptionKeyVersionUnknownException;
import org.fuin.ddd4j.jaxbtest.JaxbTestEntityIdFactory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fuin.utils4j.Utils4J.deserialize;
import static org.fuin.utils4j.Utils4J.serialize;
import static org.fuin.utils4j.jaxb.JaxbUtils.marshal;
import static org.fuin.utils4j.jaxb.JaxbUtils.unmarshal;

/**
 * Test for the {@link EncryptionKeyVersionUnknownExceptionData} class.
 */
class EncryptionKeyVersionUnknownExceptionDataTest {

    @Test
    final void testEqualsHashCode() {
        EqualsVerifier.simple().forClass(EncryptionKeyVersionUnknownExceptionData.class).verify();
    }

    @Test
    public final void testSerializeDeserialize() {

        // PREPARE
        final EncryptionKeyVersionUnknownException originalEx = new EncryptionKeyVersionUnknownException("1");
        final EncryptionKeyVersionUnknownExceptionData original = new EncryptionKeyVersionUnknownExceptionData(originalEx);

        // TEST
        final EncryptionKeyVersionUnknownExceptionData copy = deserialize(serialize(original));

        // VERIFY
        assertThat(copy).isEqualTo(original);
        assertThat(copy.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copy.getShortId()).isEqualTo(originalEx.getShortId());
        final EncryptionKeyVersionUnknownException copyEx = copy.toException();
        assertThat(copyEx.getShortId()).isEqualTo(originalEx.getShortId());
        assertThat(copyEx.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copyEx.getKeyVersion()).isEqualTo(originalEx.getKeyVersion());

    }

    @Test
    public final void testMarshalUnmarshal() {

        // PREPARE
        final EncryptionKeyVersionUnknownException originalEx = new EncryptionKeyVersionUnknownException("1");
        final EncryptionKeyVersionUnknownExceptionData original = new EncryptionKeyVersionUnknownExceptionData(originalEx);

        // TEST
        final String xml = marshal(original, createXmlAdapter(), EncryptionKeyVersionUnknownExceptionData.class);
        final EncryptionKeyVersionUnknownExceptionData copy = unmarshal(xml, createXmlAdapter(), EncryptionKeyVersionUnknownExceptionData.class);

        // VERIFY
        assertThat(copy).isEqualTo(original);
        assertThat(copy.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copy.getShortId()).isEqualTo(originalEx.getShortId());
        final EncryptionKeyVersionUnknownException copyEx = copy.toException();
        assertThat(copyEx.getShortId()).isEqualTo(originalEx.getShortId());
        assertThat(copyEx.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copyEx.getKeyVersion()).isEqualTo(originalEx.getKeyVersion());

    }

    @Test
    public final void testUnmarshal() {

        // PREPARE
        final EncryptionKeyVersionUnknownException originalEx = new EncryptionKeyVersionUnknownException("1");
        final String xml = """
                <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                <encryption-key-version-unknown-exception>
                    <msg>Unknown keyVersion: 1</msg>
                    <sid>DDD4J-ENCRYPTION_KEY_VERSION_UNKNOWN</sid>
                    <key-version>1</key-version>
                </encryption-key-version-unknown-exception>
                """;

        // TEST
        final EncryptionKeyVersionUnknownExceptionData copy = unmarshal(xml, createXmlAdapter(), EncryptionKeyVersionUnknownExceptionData.class);

        // VERIFY
        final EncryptionKeyVersionUnknownException copyEx = copy.toException();
        assertThat(copy.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copy.getShortId()).isEqualTo(originalEx.getShortId());
        assertThat(copyEx.getShortId()).isEqualTo(originalEx.getShortId());
        assertThat(copyEx.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copyEx.getKeyVersion()).isEqualTo(originalEx.getKeyVersion());

    }

    private static XmlAdapter[] createXmlAdapter() {
        return new XmlAdapter[]{new EntityIdPathXmlAdapter(new JaxbTestEntityIdFactory())};
    }

}
