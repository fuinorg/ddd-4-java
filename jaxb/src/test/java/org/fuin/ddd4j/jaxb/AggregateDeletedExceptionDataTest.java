package org.fuin.ddd4j.jaxb;

import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.fuin.ddd4j.core.AggregateDeletedException;
import org.fuin.ddd4j.jaxbtest.JaxbTestEntityIdFactory;
import org.fuin.ddd4j.jaxbtest.VendorId;
import org.fuin.utils4j.jaxb.MarshallerBuilder;
import org.fuin.utils4j.jaxb.UnmarshallerBuilder;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fuin.utils4j.Utils4J.deserialize;
import static org.fuin.utils4j.Utils4J.serialize;
import static org.fuin.utils4j.jaxb.JaxbUtils.marshal;
import static org.fuin.utils4j.jaxb.JaxbUtils.unmarshal;

/**
 * Test for the {@link AggregateDeletedExceptionData} class.
 */
class AggregateDeletedExceptionDataTest {

    @Test
    final void testEqualsHashCode() {
        EqualsVerifier.simple().forClass(AggregateDeletedExceptionData.class).verify();
    }

    @Test
    public final void testSerializeDeserialize() {

        // PREPARE
        final AggregateDeletedException originalEx = new AggregateDeletedException(VendorId.TYPE,
                new VendorId(UUID.fromString("4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119")));
        final AggregateDeletedExceptionData original = new AggregateDeletedExceptionData(originalEx);

        // TEST
        final AggregateDeletedExceptionData copy = deserialize(serialize(original));

        // VERIFY
        assertThat(copy).isEqualTo(original);
        assertThat(copy.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copy.getShortId()).isEqualTo(originalEx.getShortId());
        final AggregateDeletedException copyEx = copy.toException();
        assertThat(copyEx.getShortId()).isEqualTo(originalEx.getShortId());
        assertThat(copyEx.getType()).isEqualTo(originalEx.getType());
        assertThat(copyEx.getId()).isEqualTo(originalEx.getId());
        assertThat(copyEx.getMessage()).isEqualTo(originalEx.getMessage());

    }

    @Test
    public final void testMarshalUnmarshal() {

        // PREPARE
        final AggregateDeletedException originalEx = new AggregateDeletedException(VendorId.TYPE,
                new VendorId(UUID.fromString("4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119")));
        final AggregateDeletedExceptionData original = new AggregateDeletedExceptionData(originalEx);

        // TEST
        final Marshaller marshaller = new MarshallerBuilder().addClassesToBeBound(AggregateDeletedExceptionData.class).addAdapters(createXmlAdapter()).build();
        final String xml = marshal(marshaller, original);
        final Unmarshaller unmarshaller = new UnmarshallerBuilder().addClassesToBeBound(AggregateDeletedExceptionData.class).addAdapters(createXmlAdapter()).build();
        final AggregateDeletedExceptionData copy = unmarshal(unmarshaller, xml);

        // VERIFY
        assertThat(copy).isEqualTo(original);
        assertThat(copy.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copy.getShortId()).isEqualTo(originalEx.getShortId());
        final AggregateDeletedException copyEx = copy.toException();
        assertThat(copyEx.getShortId()).isEqualTo(originalEx.getShortId());
        assertThat(copyEx.getType()).isEqualTo(originalEx.getType());
        assertThat(copyEx.getId()).isEqualTo(originalEx.getId());
        assertThat(copyEx.getMessage()).isEqualTo(originalEx.getMessage());

    }

    @Test
    public final void testUnmarshal() {

        // PREPARE
        final AggregateDeletedException originalEx = new AggregateDeletedException(VendorId.TYPE,
                new VendorId(UUID.fromString("4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119")));
        final String xml = """
                <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                <aggregate-deleted-exception>
                    <msg>Vendor with id 4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119 already deleted</msg>
                    <sid>DDD4J-AGGREGATE_DELETED</sid>
                    <aggregate-type>Vendor</aggregate-type>
                    <aggregate-id>4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119</aggregate-id>
                </aggregate-deleted-exception>
                """;

        // TEST
        final Unmarshaller unmarshaller = new UnmarshallerBuilder().addClassesToBeBound(AggregateDeletedExceptionData.class).addAdapters(createXmlAdapter()).build();
        final AggregateDeletedExceptionData copy = unmarshal(unmarshaller, xml);

        // VERIFY
        final AggregateDeletedException copyEx = copy.toException();
        assertThat(copy.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copy.getShortId()).isEqualTo(originalEx.getShortId());
        assertThat(copyEx.getShortId()).isEqualTo(originalEx.getShortId());
        assertThat(copyEx.getType()).isEqualTo(originalEx.getType());
        assertThat(copyEx.getId()).isEqualTo(originalEx.getId());
        assertThat(copyEx.getMessage()).isEqualTo(originalEx.getMessage());

    }

    private static XmlAdapter[] createXmlAdapter() {
        return new XmlAdapter[]{new EntityIdPathXmlAdapter(new JaxbTestEntityIdFactory())};
    }

}
