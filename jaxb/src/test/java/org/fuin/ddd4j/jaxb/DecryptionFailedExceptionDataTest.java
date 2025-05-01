package org.fuin.ddd4j.jaxb;

import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.fuin.ddd4j.core.DecryptionFailedException;
import org.fuin.ddd4j.jaxbtest.JaxbTestEntityIdFactory;
import org.fuin.utils4j.jaxb.MarshallerBuilder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fuin.utils4j.Utils4J.deserialize;
import static org.fuin.utils4j.Utils4J.serialize;
import static org.fuin.utils4j.jaxb.JaxbUtils.marshal;
import static org.fuin.utils4j.jaxb.JaxbUtils.unmarshal;

/**
 * Test for the {@link DecryptionFailedExceptionData} class.
 */
class DecryptionFailedExceptionDataTest {

    @Test
    final void testEqualsHashCode() {
        EqualsVerifier.simple().forClass(DecryptionFailedExceptionData.class).verify();
    }

    @Test
    public final void testSerializeDeserialize() {

        // PREPARE
        final RuntimeException cause = new RuntimeException("Foo Bar");
        final DecryptionFailedException originalEx = new DecryptionFailedException(cause);
        final DecryptionFailedExceptionData original = new DecryptionFailedExceptionData(originalEx);

        // TEST
        final DecryptionFailedExceptionData copy = deserialize(serialize(original));

        // VERIFY
        assertThat(copy).isEqualTo(original);
        assertThat(copy.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copy.getShortId()).isEqualTo(originalEx.getShortId());
        final DecryptionFailedException copyEx = copy.toException();
        assertThat(copyEx.getShortId()).isEqualTo(originalEx.getShortId());
        assertThat(copyEx.getMessage()).isEqualTo(originalEx.getMessage());

    }

    @Test
    public final void testMarshalUnmarshal() {

        // PREPARE
        final RuntimeException cause = new RuntimeException("Foo Bar");
        final DecryptionFailedException originalEx = new DecryptionFailedException(cause);
        final DecryptionFailedExceptionData original = new DecryptionFailedExceptionData(originalEx);

        // TEST
        final Marshaller marshaller = new MarshallerBuilder().addClassesToBeBound(DecryptionFailedExceptionData.class).addAdapters(createXmlAdapter()).build();
        final String xml = marshal(marshaller, original);
        final DecryptionFailedExceptionData copy = unmarshal(xml, createXmlAdapter(), DecryptionFailedExceptionData.class);

        // VERIFY
        assertThat(copy).isEqualTo(original);
        assertThat(copy.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copy.getShortId()).isEqualTo(originalEx.getShortId());
        final DecryptionFailedException copyEx = copy.toException();
        assertThat(copyEx.getShortId()).isEqualTo(originalEx.getShortId());
        assertThat(copyEx.getMessage()).isEqualTo(originalEx.getMessage());

    }

    @Test
    public final void testUnmarshal() {

        // PREPARE
        final RuntimeException cause = new RuntimeException("Foo Bar");
        final DecryptionFailedException originalEx = new DecryptionFailedException(cause);
        final String xml = """
                <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                <decryption-failed-exception>
                    <msg>Decryption failed: Foo Bar</msg>
                    <sid>DDD4J-DECRYPTION_FAILED</sid>
                </decryption-failed-exception>
                """;

        // TEST
        final DecryptionFailedExceptionData copy = unmarshal(xml, createXmlAdapter(), DecryptionFailedExceptionData.class);

        // VERIFY
        final DecryptionFailedException copyEx = copy.toException();
        assertThat(copy.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copy.getShortId()).isEqualTo(originalEx.getShortId());
        assertThat(copyEx.getShortId()).isEqualTo(originalEx.getShortId());
        assertThat(copyEx.getMessage()).isEqualTo(originalEx.getMessage());

    }

    private static XmlAdapter[] createXmlAdapter() {
        return new XmlAdapter[]{new EntityIdPathXmlAdapter(new JaxbTestEntityIdFactory())};
    }

}
