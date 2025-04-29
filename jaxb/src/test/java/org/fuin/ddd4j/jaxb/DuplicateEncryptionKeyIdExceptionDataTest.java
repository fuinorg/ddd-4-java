package org.fuin.ddd4j.jaxb;

import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.fuin.ddd4j.core.DuplicateEncryptionKeyIdException;
import org.fuin.ddd4j.jaxbtest.JaxbTestEntityIdFactory;
import org.fuin.utils4j.jaxb.MarshallerBuilder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fuin.utils4j.Utils4J.deserialize;
import static org.fuin.utils4j.Utils4J.serialize;
import static org.fuin.utils4j.jaxb.JaxbUtils.marshal;
import static org.fuin.utils4j.jaxb.JaxbUtils.unmarshal;

/**
 * Test for the {@link DuplicateEncryptionKeyIdExceptionData} class.
 */
class DuplicateEncryptionKeyIdExceptionDataTest {

    @Test
    final void testEqualsHashCode() {
        EqualsVerifier.simple().forClass(DuplicateEncryptionKeyIdExceptionData.class).verify();
    }

    @Test
    public final void testSerializeDeserialize() {

        // PREPARE
        final DuplicateEncryptionKeyIdException originalEx = new DuplicateEncryptionKeyIdException("xyz");
        final DuplicateEncryptionKeyIdExceptionData original = new DuplicateEncryptionKeyIdExceptionData(originalEx);

        // TEST
        final DuplicateEncryptionKeyIdExceptionData copy = deserialize(serialize(original));

        // VERIFY
        assertThat(copy).isEqualTo(original);
        assertThat(copy.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copy.getShortId()).isEqualTo(originalEx.getShortId());
        final DuplicateEncryptionKeyIdException copyEx = copy.toException();
        assertThat(copyEx.getShortId()).isEqualTo(originalEx.getShortId());
        assertThat(copyEx.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copyEx.getKeyId()).isEqualTo(originalEx.getKeyId());

    }

    @Test
    public final void testMarshalUnmarshal() {

        // PREPARE
        final DuplicateEncryptionKeyIdException originalEx = new DuplicateEncryptionKeyIdException("xyz");
        final DuplicateEncryptionKeyIdExceptionData original = new DuplicateEncryptionKeyIdExceptionData(originalEx);

        // TEST
        final Marshaller marshaller = new MarshallerBuilder().addClassesToBeBound(DuplicateEncryptionKeyIdExceptionData.class).addAdapters(createXmlAdapter()).build();
        final String xml = marshal(marshaller, original);
        final DuplicateEncryptionKeyIdExceptionData copy = unmarshal(xml, createXmlAdapter(), DuplicateEncryptionKeyIdExceptionData.class);

        // VERIFY
        assertThat(copy).isEqualTo(original);
        assertThat(copy.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copy.getShortId()).isEqualTo(originalEx.getShortId());
        final DuplicateEncryptionKeyIdException copyEx = copy.toException();
        assertThat(copyEx.getShortId()).isEqualTo(originalEx.getShortId());
        assertThat(copyEx.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copyEx.getKeyId()).isEqualTo(originalEx.getKeyId());

    }

    @Test
    public final void testUnmarshal() {

        // PREPARE
        final DuplicateEncryptionKeyIdException originalEx = new DuplicateEncryptionKeyIdException("xyz");
        final String xml = """
                <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                <duplicate-encryption-key-id-exception>
                    <msg>Duplicate keyId: xyz</msg>
                    <sid>DDD4J-DUPLICATE-ENCRYPTION_KEY_ID</sid>
                    <key-id>xyz</key-id>
                </duplicate-encryption-key-id-exception>
                """;

        // TEST
        final DuplicateEncryptionKeyIdExceptionData copy = unmarshal(xml, createXmlAdapter(), DuplicateEncryptionKeyIdExceptionData.class);

        // VERIFY
        final DuplicateEncryptionKeyIdException copyEx = copy.toException();
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
