package org.fuin.ddd4j.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.fuin.ddd4j.core.EntityIdPath;
import org.fuin.ddd4j.core.EntityNotFoundException;
import org.fuin.ddd4j.jacksontest.PersonId;
import org.fuin.ddd4j.jacksontest.VendorId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.fuin.utils4j.Utils4J.deserialize;
import static org.fuin.utils4j.Utils4J.serialize;

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
    public final void testMarshalUnmarshal() throws Exception {

        final ObjectMapper objectMapper = TestUtils.objectMapper();

        // PREPARE
        final EntityIdPath parentPath = new EntityIdPath(new VendorId(UUID.fromString("4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119")));
        final PersonId personId = new PersonId(1);
        final EntityNotFoundException originalEx = new EntityNotFoundException(parentPath, personId);
        final EntityNotFoundExceptionData original = new EntityNotFoundExceptionData(originalEx);

        // TEST
        final String json = objectMapper.writeValueAsString(original);
        final EntityNotFoundExceptionData copy = objectMapper.readValue(json, EntityNotFoundExceptionData.class);

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
    public final void testUnmarshal() throws Exception {

        final ObjectMapper objectMapper = TestUtils.objectMapper();

        // PREPARE
        final String json = """
                {
                    "msg" : "Person 1 not found in Vendor 4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119",
                    "sid" : "DDD4J-ENTITY_NOT_FOUND",
                    "parent-id-path" : "Vendor 4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119",
                    "entity-id" : "Person 1"
                }
                """;

        // TEST
        final EntityNotFoundExceptionData copy = objectMapper.readValue(json, EntityNotFoundExceptionData.class);

        // VERIFY
        final String copyJson = objectMapper.writeValueAsString(copy);
        assertThatJson(copyJson).isEqualTo(json);

    }

}
