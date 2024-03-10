package org.fuin.ddd4j.jaxb;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.fuin.ddd4j.core.AggregateAlreadyExistsException;
import org.fuin.ddd4j.jaxbtest.JaxbTestEntityIdFactory;
import org.fuin.ddd4j.jaxbtest.VendorId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fuin.utils4j.Utils4J.deserialize;
import static org.fuin.utils4j.Utils4J.serialize;
import static org.fuin.utils4j.jaxb.JaxbUtils.marshal;
import static org.fuin.utils4j.jaxb.JaxbUtils.unmarshal;

/**
 * Test for the {@link AggregateAlreadyExistsExceptionData} class.
 */
class AggregateAlreadyExistsExceptionDataTest {

    @Test
    final void testEqualsHashCode() {
        EqualsVerifier.simple().forClass(AggregateAlreadyExistsExceptionData.class).verify();
    }

    @Test
    public final void testSerializeDeserialize() {

        // PREPARE
        final AggregateAlreadyExistsException originalEx = new AggregateAlreadyExistsException(VendorId.TYPE,
                new VendorId(UUID.fromString("4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119")), 102);
        final AggregateAlreadyExistsExceptionData original = new AggregateAlreadyExistsExceptionData(originalEx);

        // TEST
        final AggregateAlreadyExistsExceptionData copy = deserialize(serialize(original));

        // VERIFY
        assertThat(copy).isEqualTo(original);
        final AggregateAlreadyExistsException copyEx = copy.toException();
        assertThat(copy.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copyEx.getShortId()).isEqualTo(originalEx.getShortId());
        assertThat(copyEx.getType()).isEqualTo(originalEx.getType());
        assertThat(copyEx.getId()).isEqualTo(originalEx.getId());
        assertThat(copyEx.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copyEx.getVersion()).isEqualTo(originalEx.getVersion());

    }

    @Test
    public final void testMarshalUnmarshal() {

        // PREPARE
        final AggregateAlreadyExistsException originalEx = new AggregateAlreadyExistsException(VendorId.TYPE,
                new VendorId(UUID.fromString("4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119")), 102);
        final AggregateAlreadyExistsExceptionData original = new AggregateAlreadyExistsExceptionData(originalEx);

        // TEST
        final String xml = marshal(original, createXmlAdapter(), AggregateAlreadyExistsExceptionData.class);
        final AggregateAlreadyExistsExceptionData copy = unmarshal(xml, createXmlAdapter(), AggregateAlreadyExistsExceptionData.class);

        // VERIFY
        assertThat(copy).isEqualTo(original);
        final AggregateAlreadyExistsException copyEx = copy.toException();
        assertThat(copy.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copy.getShortId()).isEqualTo(originalEx.getShortId());
        assertThat(copyEx.getShortId()).isEqualTo(originalEx.getShortId());
        assertThat(copyEx.getType()).isEqualTo(originalEx.getType());
        assertThat(copyEx.getId()).isEqualTo(originalEx.getId());
        assertThat(copyEx.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copyEx.getVersion()).isEqualTo(originalEx.getVersion());

    }

    @Test
    public final void testUnmarshal() {

        // PREPARE
        final AggregateAlreadyExistsException originalEx = new AggregateAlreadyExistsException(VendorId.TYPE,
                new VendorId(UUID.fromString("4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119")), 102);
        final String xml = """
                <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                <aggregate-already-exists-exception>
                    <msg>Vendor 4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119 already exists (version=102)</msg>
                    <sid>DDD4J-AGGREGATE_ALREADY_EXISTS</sid>
                    <aggregate-type>Vendor</aggregate-type>
                    <aggregate-id>4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119</aggregate-id>
                    <version>102</version>
                </aggregate-already-exists-exception>
                """;

        // TEST
        final AggregateAlreadyExistsExceptionData copy = unmarshal(xml, createXmlAdapter(), AggregateAlreadyExistsExceptionData.class);

        // VERIFY
        final AggregateAlreadyExistsException copyEx = copy.toException();
        assertThat(copy.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copy.getShortId()).isEqualTo(originalEx.getShortId());
        assertThat(copyEx.getShortId()).isEqualTo(originalEx.getShortId());
        assertThat(copyEx.getType()).isEqualTo(originalEx.getType());
        assertThat(copyEx.getId()).isEqualTo(originalEx.getId());
        assertThat(copyEx.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copyEx.getVersion()).isEqualTo(originalEx.getVersion());

    }

    private static XmlAdapter[] createXmlAdapter() {
        return new XmlAdapter[]{new EntityIdPathXmlAdapter(new JaxbTestEntityIdFactory())};
    }

}
