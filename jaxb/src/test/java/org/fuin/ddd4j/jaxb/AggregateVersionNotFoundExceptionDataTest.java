package org.fuin.ddd4j.jaxb;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.fuin.ddd4j.core.AggregateVersionNotFoundException;
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
 * Test for the {@link AggregateVersionNotFoundExceptionData} class.
 */
class AggregateVersionNotFoundExceptionDataTest {

    @Test
    final void testEqualsHashCode() {
        EqualsVerifier.simple().forClass(AggregateVersionNotFoundExceptionData.class).verify();
    }

    @Test
    public final void testSerializeDeserialize() {

        // PREPARE
        final AggregateVersionNotFoundException originalEx = new AggregateVersionNotFoundException(VendorId.TYPE,
                new VendorId(UUID.fromString("4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119")), 102);
        final AggregateVersionNotFoundExceptionData original = new AggregateVersionNotFoundExceptionData(originalEx);

        // TEST
        final AggregateVersionNotFoundExceptionData copy = deserialize(serialize(original));

        // VERIFY
        assertThat(copy).isEqualTo(original);
        assertThat(copy.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copy.getShortId()).isEqualTo(originalEx.getShortId());
        final AggregateVersionNotFoundException copyEx = copy.toException();
        assertThat(copyEx.getShortId()).isEqualTo(originalEx.getShortId());
        assertThat(copyEx.getType()).isEqualTo(originalEx.getType());
        assertThat(copyEx.getId()).isEqualTo(originalEx.getId());
        assertThat(copyEx.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copyEx.getVersion()).isEqualTo(originalEx.getVersion());

    }

    @Test
    public final void testMarshalUnmarshal() {

        // PREPARE
        final AggregateVersionNotFoundException originalEx = new AggregateVersionNotFoundException(VendorId.TYPE,
                new VendorId(UUID.fromString("4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119")), 102);
        final AggregateVersionNotFoundExceptionData original = new AggregateVersionNotFoundExceptionData(originalEx);

        // TEST
        final String xml = marshal(original, createXmlAdapter(), AggregateVersionNotFoundExceptionData.class);
        final AggregateVersionNotFoundExceptionData copy = unmarshal(xml, createXmlAdapter(), AggregateVersionNotFoundExceptionData.class);

        // VERIFY
        assertThat(copy).isEqualTo(original);
        assertThat(copy.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copy.getShortId()).isEqualTo(originalEx.getShortId());
        final AggregateVersionNotFoundException copyEx = copy.toException();
        assertThat(copyEx.getShortId()).isEqualTo(originalEx.getShortId());
        assertThat(copyEx.getType()).isEqualTo(originalEx.getType());
        assertThat(copyEx.getId()).isEqualTo(originalEx.getId());
        assertThat(copyEx.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copyEx.getVersion()).isEqualTo(originalEx.getVersion());

    }

    @Test
    public final void testUnmarshal() {

        // PREPARE
        final AggregateVersionNotFoundException originalEx = new AggregateVersionNotFoundException(VendorId.TYPE,
                new VendorId(UUID.fromString("4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119")), 102);
        final String xml = """
                <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                <aggregate-version-not-found-exception>
                    <msg>Requested version 102 for Vendor (4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119) does not exist</msg>
                    <sid>DDD4J-AGGREGATE_VERSION_NOT_FOUND</sid>
                    <aggregate-type>Vendor</aggregate-type>
                    <aggregate-id>4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119</aggregate-id>
                    <version>102</version>
                </aggregate-version-not-found-exception>
                """;

        // TEST
        final AggregateVersionNotFoundExceptionData copy = unmarshal(xml, createXmlAdapter(), AggregateVersionNotFoundExceptionData.class);

        // VERIFY
        final AggregateVersionNotFoundException copyEx = copy.toException();
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
