package org.fuin.ddd4j.jaxb;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.fuin.ddd4j.core.EntityIdPath;
import org.fuin.ddd4j.core.EntityNotFoundException;
import org.fuin.ddd4j.jaxbtest.JaxbTestEntityIdFactory;
import org.fuin.ddd4j.jaxbtest.PersonId;
import org.fuin.ddd4j.jaxbtest.VendorId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fuin.utils4j.Utils4J.deserialize;
import static org.fuin.utils4j.Utils4J.serialize;
import static org.fuin.utils4j.jaxb.JaxbUtils.marshal;
import static org.fuin.utils4j.jaxb.JaxbUtils.unmarshal;

/**
 * Test for the {@link EntityNotFoundExceptionData} class.
 */
class EntityNotFoundExceptionDataTest {

    @Test
    final void testEqualsHashCode() {
        EqualsVerifier.simple().forClass(EntityNotFoundExceptionData.class).verify();
    }

    @Test
    public final void testSerializeDeserialize() {

        // PREPARE
        final EntityIdPath parentPath = new EntityIdPath(new VendorId(UUID.fromString("4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119")));
        final PersonId personId = new PersonId(1);
        final EntityNotFoundException originalEx = new EntityNotFoundException(parentPath, personId);
        final EntityNotFoundExceptionData original = new EntityNotFoundExceptionData(originalEx);

        // TEST
        final EntityNotFoundExceptionData copy = deserialize(serialize(original));

        // VERIFY
        assertThat(copy).isEqualTo(original);
        assertThat(copy.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copy.getShortId()).isEqualTo(originalEx.getShortId());
        final EntityNotFoundException copyEx = copy.toException();
        assertThat(copyEx.getShortId()).isEqualTo(originalEx.getShortId());
        assertThat(copyEx.getParentIdPath()).isEqualTo(originalEx.getParentIdPath());
        assertThat(copyEx.getEntityId()).isEqualTo(originalEx.getEntityId());
        assertThat(copyEx.getMessage()).isEqualTo(originalEx.getMessage());

    }

    @Test
    public final void testMarshalUnmarshal() {

        // PREPARE
        final EntityIdPath parentPath = new EntityIdPath(new VendorId(UUID.fromString("4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119")));
        final PersonId personId = new PersonId(1);
        final EntityNotFoundException originalEx = new EntityNotFoundException(parentPath, personId);
        final EntityNotFoundExceptionData original = new EntityNotFoundExceptionData(originalEx);

        // TEST
        final String xml = marshal(original, createXmlAdapter(), EntityNotFoundExceptionData.class);
        final EntityNotFoundExceptionData copy = unmarshal(xml, createXmlAdapter(), EntityNotFoundExceptionData.class);

        // VERIFY
        assertThat(copy).isEqualTo(original);
        assertThat(copy.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copy.getShortId()).isEqualTo(originalEx.getShortId());
        final EntityNotFoundException copyEx = copy.toException();
        assertThat(copyEx.getShortId()).isEqualTo(originalEx.getShortId());
        assertThat(copyEx.getParentIdPath()).isEqualTo(originalEx.getParentIdPath());
        assertThat(copyEx.getEntityId()).isEqualTo(originalEx.getEntityId());
        assertThat(copyEx.getMessage()).isEqualTo(originalEx.getMessage());

    }

    @Test
    public final void testUnmarshal() {

        // PREPARE
        final EntityIdPath parentPath = new EntityIdPath(new VendorId(UUID.fromString("4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119")));
        final PersonId personId = new PersonId(1);
        final EntityNotFoundException originalEx = new EntityNotFoundException(parentPath, personId);
        final String xml = """
                <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                <entity-not-found-exception>
                    <msg>Person 1 not found in Vendor 4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119</msg>
                    <sid>DDD4J-ENTITY_NOT_FOUND</sid>
                    <parent-id-path>Vendor 4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119</parent-id-path>
                    <entity-id>Person 1</entity-id>
                </entity-not-found-exception>
                """;

        // TEST
        final EntityNotFoundExceptionData copy = unmarshal(xml, createXmlAdapter(), EntityNotFoundExceptionData.class);

        // VERIFY
        final EntityNotFoundException copyEx = copy.toException();
        assertThat(copy.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copy.getShortId()).isEqualTo(originalEx.getShortId());
        assertThat(copyEx.getShortId()).isEqualTo(originalEx.getShortId());
        assertThat(copyEx.getParentIdPath()).isEqualTo(originalEx.getParentIdPath());
        assertThat(copyEx.getEntityId()).isEqualTo(originalEx.getEntityId());
        assertThat(copyEx.getMessage()).isEqualTo(originalEx.getMessage());

    }

    private static XmlAdapter[] createXmlAdapter() {
        return new XmlAdapter[]{new EntityIdPathXmlAdapter(new JaxbTestEntityIdFactory())};
    }

}
