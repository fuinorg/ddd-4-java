package org.fuin.ddd4j.jsonb;

import jakarta.json.bind.Jsonb;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.fuin.ddd4j.core.AggregateDeletedException;
import org.fuin.ddd4j.jsonbtest.VendorId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.fuin.ddd4j.jsonb.TestUtils.jsonb;
import static org.fuin.utils4j.Utils4J.deserialize;
import static org.fuin.utils4j.Utils4J.serialize;

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
    public final void testMarshalUnmarshal() throws Exception {

        try (final Jsonb jsonb = jsonb()) {

            // PREPARE
            final AggregateDeletedException originalEx = new AggregateDeletedException(VendorId.TYPE,
                    new VendorId(UUID.fromString("4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119")));
            final AggregateDeletedExceptionData original = new AggregateDeletedExceptionData(originalEx);

            // TEST
            final String json = jsonb.toJson(original, AggregateDeletedExceptionData.class);
            final AggregateDeletedExceptionData copy = jsonb.fromJson(json, AggregateDeletedExceptionData.class);

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
        
    }

    @Test
    public final void testUnmarshal() throws Exception {

        try (final Jsonb jsonb = jsonb()) {

            // PREPARE
            final String json = """
                    {
                        "msg" : "Vendor with id 4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119 already deleted",
                        "sid" : "DDD4J-AGGREGATE_DELETED",
                        "aggregate-type" : "Vendor",
                        "aggregate-id" : "4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119"
                    }
                    """;

            // TEST
            final AggregateDeletedExceptionData copy = jsonb.fromJson(json, AggregateDeletedExceptionData.class);

            // VERIFY
            final String copyJson = jsonb.toJson(copy);
            assertThatJson(copyJson).isEqualTo(json);

        }
        
    }

}
