package org.fuin.ddd4j.jaxb;

import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.fuin.ddd4j.core.EncryptionKeyIdUnknownException;
import org.fuin.ddd4j.jaxbtest.JaxbTestEntityIdFactory;
import org.fuin.utils4j.jaxb.MarshallerBuilder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fuin.utils4j.Utils4J.deserialize;
import static org.fuin.utils4j.Utils4J.serialize;
import static org.fuin.utils4j.jaxb.JaxbUtils.marshal;
import static org.fuin.utils4j.jaxb.JaxbUtils.unmarshal;

/**
 * Test for the {@link EncryptionKeyIdUnknownExceptionData} class.
 */
class EncryptionKeyIdUnknownExceptionDataTest {

    @Test
    final void testEqualsHashCode() {
        EqualsVerifier.simple().forClass(EncryptionKeyIdUnknownExceptionData.class).verify();
    }

    @Test
    public final void testSerializeDeserialize() {

        // PREPARE
        final EncryptionKeyIdUnknownException originalEx = new EncryptionKeyIdUnknownException("xyz");
        final EncryptionKeyIdUnknownExceptionData original = new EncryptionKeyIdUnknownExceptionData(originalEx);

        // TEST
        final EncryptionKeyIdUnknownExceptionData copy = deserialize(serialize(original));

        // VERIFY
        assertThat(copy).isEqualTo(original);
        assertThat(copy.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copy.getShortId()).isEqualTo(originalEx.getShortId());
        final EncryptionKeyIdUnknownException copyEx = copy.toException();
        assertThat(copyEx.getShortId()).isEqualTo(originalEx.getShortId());
        assertThat(copyEx.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copyEx.getKeyId()).isEqualTo(originalEx.getKeyId());

    }

    @Test
    public final void testMarshalUnmarshal() {

        // PREPARE
        final EncryptionKeyIdUnknownException originalEx = new EncryptionKeyIdUnknownException("xyz");
        final EncryptionKeyIdUnknownExceptionData original = new EncryptionKeyIdUnknownExceptionData(originalEx);

        // TEST
        final Marshaller marshaller = new MarshallerBuilder().addClassesToBeBound(EncryptionKeyIdUnknownExceptionData.class).addAdapters(createXmlAdapter()).build();
        final String xml = marshal(marshaller, original);
        final EncryptionKeyIdUnknownExceptionData copy = unmarshal(xml, createXmlAdapter(), EncryptionKeyIdUnknownExceptionData.class);

        // VERIFY
        assertThat(copy).isEqualTo(original);
        assertThat(copy.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copy.getShortId()).isEqualTo(originalEx.getShortId());
        final EncryptionKeyIdUnknownException copyEx = copy.toException();
        assertThat(copyEx.getShortId()).isEqualTo(originalEx.getShortId());
        assertThat(copyEx.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copyEx.getKeyId()).isEqualTo(originalEx.getKeyId());

    }

    @Test
    public final void testUnmarshal() {

        // PREPARE
        final EncryptionKeyIdUnknownException originalEx = new EncryptionKeyIdUnknownException("xyz");
        final String xml = """
                <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                <encryption-key-id-unknown-exception>
                    <msg>Unknown keyId: xyz</msg>
                    <sid>DDD4J-ENCRYPTION_KEY_ID_UNKNOWN</sid>
                    <key-id>xyz</key-id>
                </encryption-key-id-unknown-exception>
                """;

        // TEST
        final EncryptionKeyIdUnknownExceptionData copy = unmarshal(xml, createXmlAdapter(), EncryptionKeyIdUnknownExceptionData.class);

        // VERIFY
        final EncryptionKeyIdUnknownException copyEx = copy.toException();
        assertThat(copy.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copy.getShortId()).isEqualTo(originalEx.getShortId());
        assertThat(copyEx.getShortId()).isEqualTo(originalEx.getShortId());
        assertThat(copyEx.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copyEx.getKeyId()).isEqualTo(originalEx.getKeyId());

    }

    private static XmlAdapter[] createXmlAdapter() {
        return new XmlAdapter[]{new EntityIdPathXmlAdapter(new JaxbTestEntityIdFactory())};
    }

}
