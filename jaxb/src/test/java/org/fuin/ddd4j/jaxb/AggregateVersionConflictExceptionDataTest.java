package org.fuin.ddd4j.jaxb;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.fuin.ddd4j.core.AggregateVersionConflictException;
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
 * Test for the {@link AggregateVersionConflictExceptionData} class.
 */
class AggregateVersionConflictExceptionDataTest {

    @Test
    final void testEqualsHashCode() {
        EqualsVerifier.simple().forClass(AggregateVersionConflictExceptionData.class).verify();
    }

    @Test
    public final void testSerializeDeserialize() {

        // PREPARE
        final AggregateVersionConflictException originalEx = new AggregateVersionConflictException(VendorId.TYPE,
                new VendorId(UUID.fromString("4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119")), 102, 103);
        final AggregateVersionConflictExceptionData original = new AggregateVersionConflictExceptionData(originalEx);

        // TEST
        final AggregateVersionConflictExceptionData copy = deserialize(serialize(original));

        // VERIFY
        assertThat(copy).isEqualTo(original);
        assertThat(copy.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copy.getShortId()).isEqualTo(originalEx.getShortId());
        final AggregateVersionConflictException copyEx = copy.toException();
        assertThat(copyEx.getShortId()).isEqualTo(originalEx.getShortId());
        assertThat(copyEx.getType()).isEqualTo(originalEx.getType());
        assertThat(copyEx.getId()).isEqualTo(originalEx.getId());
        assertThat(copyEx.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copyEx.getExpected()).isEqualTo(originalEx.getExpected());
        assertThat(copyEx.getActual()).isEqualTo(originalEx.getActual());

    }

    @Test
    public final void testMarshalUnmarshal() {

        // PREPARE
        final AggregateVersionConflictException originalEx = new AggregateVersionConflictException(VendorId.TYPE,
                new VendorId(UUID.fromString("4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119")), 102, 103);
        final AggregateVersionConflictExceptionData original = new AggregateVersionConflictExceptionData(originalEx);

        // TEST
        final String xml = marshal(original, createXmlAdapter(), AggregateVersionConflictExceptionData.class);
        final AggregateVersionConflictExceptionData copy = unmarshal(xml, createXmlAdapter(), AggregateVersionConflictExceptionData.class);

        // VERIFY
        assertThat(copy).isEqualTo(original);
        assertThat(copy.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copy.getShortId()).isEqualTo(originalEx.getShortId());
        final AggregateVersionConflictException copyEx = copy.toException();
        assertThat(copyEx.getShortId()).isEqualTo(originalEx.getShortId());
        assertThat(copyEx.getType()).isEqualTo(originalEx.getType());
        assertThat(copyEx.getId()).isEqualTo(originalEx.getId());
        assertThat(copyEx.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copyEx.getExpected()).isEqualTo(originalEx.getExpected());
        assertThat(copyEx.getActual()).isEqualTo(originalEx.getActual());

    }

    @Test
    public final void testUnmarshal() {

        // PREPARE
        final AggregateVersionConflictException originalEx = new AggregateVersionConflictException(VendorId.TYPE,
                new VendorId(UUID.fromString("4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119")), 102, 103);
        final String xml = """
                <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                <aggregate-version-conflict-exception>
                    <msg>Expected version 102 for Vendor (4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119), but was 103</msg>
                    <sid>DDD4J-AGGREGATE_VERSION_CONFLICT</sid>
                    <aggregate-type>Vendor</aggregate-type>
                    <aggregate-id>4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119</aggregate-id>
                    <expected-version>102</expected-version>
                    <actual-version>103</actual-version>
                </aggregate-version-conflict-exception>
                """;

        // TEST
        final AggregateVersionConflictExceptionData copy = unmarshal(xml, createXmlAdapter(), AggregateVersionConflictExceptionData.class);

        // VERIFY
        final AggregateVersionConflictException copyEx = copy.toException();
        assertThat(copy.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copy.getShortId()).isEqualTo(originalEx.getShortId());
        assertThat(copyEx.getShortId()).isEqualTo(originalEx.getShortId());
        assertThat(copyEx.getType()).isEqualTo(originalEx.getType());
        assertThat(copyEx.getId()).isEqualTo(originalEx.getId());
        assertThat(copyEx.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copyEx.getExpected()).isEqualTo(originalEx.getExpected());
        assertThat(copyEx.getActual()).isEqualTo(originalEx.getActual());

    }

    private static XmlAdapter[] createXmlAdapter() {
        return new XmlAdapter[]{new EntityIdPathXmlAdapter(new JaxbTestEntityIdFactory())};
    }

}
