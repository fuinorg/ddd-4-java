package org.fuin.ddd4j.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.fuin.ddd4j.core.AggregateVersionConflictException;
import org.fuin.ddd4j.jacksontest.VendorId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.fuin.utils4j.Utils4J.deserialize;
import static org.fuin.utils4j.Utils4J.serialize;

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
    public final void testMarshalUnmarshal() throws Exception {

        final ObjectMapper objectMapper = TestUtils.objectMapper();

        // PREPARE
        final AggregateVersionConflictException originalEx = new AggregateVersionConflictException(VendorId.TYPE,
                new VendorId(UUID.fromString("4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119")), 102, 103);
        final AggregateVersionConflictExceptionData original = new AggregateVersionConflictExceptionData(originalEx);

        // TEST
        final String json = objectMapper.writeValueAsString(original);
        final AggregateVersionConflictExceptionData copy = objectMapper.readValue(json, AggregateVersionConflictExceptionData.class);

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
    public final void testUnmarshal() throws Exception {

        final ObjectMapper objectMapper = TestUtils.objectMapper();

        // PREPARE
        final String json = """
                {
                    "msg" : "Expected version 102 for Vendor (4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119), but was 103",
                    "sid" : "DDD4J-AGGREGATE_VERSION_CONFLICT",
                    "aggregate-type" : "Vendor",
                    "aggregate-id" : "4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119",
                    "expected-version" : 102,
                    "actual-version" : 103
                }
                """;

        // TEST
        final AggregateVersionConflictExceptionData copy = objectMapper.readValue(json, AggregateVersionConflictExceptionData.class);

        // VERIFY
        final String copyJson = objectMapper.writeValueAsString(copy);
        assertThatJson(copyJson).isEqualTo(json);

    }

}
