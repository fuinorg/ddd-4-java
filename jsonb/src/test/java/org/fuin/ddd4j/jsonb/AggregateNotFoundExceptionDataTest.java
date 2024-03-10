package org.fuin.ddd4j.jsonb;

import jakarta.json.bind.Jsonb;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.fuin.ddd4j.core.AggregateNotFoundException;
import org.fuin.ddd4j.jsonbtest.VendorId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fuin.ddd4j.jsonb.TestUtils.jsonb;
import static org.fuin.utils4j.Utils4J.deserialize;
import static org.fuin.utils4j.Utils4J.serialize;

/**
 * Test for the {@link AggregateNotFoundExceptionData} class.
 */
class AggregateNotFoundExceptionDataTest {

    @Test
    final void testEqualsHashCode() {
        EqualsVerifier.simple().forClass(AggregateNotFoundExceptionData.class).verify();
    }

    @Test
    public final void testSerializeDeserialize() {

        // PREPARE
        final AggregateNotFoundException originalEx = new AggregateNotFoundException(VendorId.TYPE,
                new VendorId(UUID.fromString("4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119")));
        final AggregateNotFoundExceptionData original = new AggregateNotFoundExceptionData(originalEx);

        // TEST
        final AggregateNotFoundExceptionData copy = deserialize(serialize(original));

        // VERIFY
        assertThat(copy).isEqualTo(original);
        assertThat(copy.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copy.getShortId()).isEqualTo(originalEx.getShortId());
        final AggregateNotFoundException copyEx = copy.toException();
        assertThat(copyEx.getShortId()).isEqualTo(originalEx.getShortId());
        assertThat(copyEx.getType()).isEqualTo(originalEx.getType());
        assertThat(copyEx.getId()).isEqualTo(originalEx.getId());
        assertThat(copyEx.getMessage()).isEqualTo(originalEx.getMessage());

    }

    @Test
    public final void testMarshalUnmarshal() throws Exception {

        try (final Jsonb jsonb = jsonb()) {

            // PREPARE
            final AggregateNotFoundException originalEx = new AggregateNotFoundException(VendorId.TYPE,
                    new VendorId(UUID.fromString("4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119")));
            final AggregateNotFoundExceptionData original = new AggregateNotFoundExceptionData(originalEx);

            // TEST
            final String json = jsonb.toJson(original, AggregateNotFoundExceptionData.class);
            final AggregateNotFoundExceptionData copy = jsonb.fromJson(json, AggregateNotFoundExceptionData.class);

            // VERIFY
            assertThat(copy).isEqualTo(original);
            assertThat(copy.getMessage()).isEqualTo(originalEx.getMessage());
            assertThat(copy.getShortId()).isEqualTo(originalEx.getShortId());
            final AggregateNotFoundException copyEx = copy.toException();
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
            final AggregateNotFoundException originalEx = new AggregateNotFoundException(VendorId.TYPE,
                    new VendorId(UUID.fromString("4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119")));
            final String json = """
                    {
                        "msg" : "Vendor with id 4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119 not found",
                        "sid" : "DDD4J-AGGREGATE_NOT_FOUND",
                        "aggregate-type" : "Vendor",
                        "aggregate-id" : "4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119"
                    }
                    """;

            // TEST
            final AggregateNotFoundExceptionData copy = jsonb.fromJson(json, AggregateNotFoundExceptionData.class);

            // VERIFY
            final AggregateNotFoundException copyEx = copy.toException();
            assertThat(copy.getMessage()).isEqualTo(originalEx.getMessage());
            assertThat(copy.getShortId()).isEqualTo(originalEx.getShortId());
            assertThat(copyEx.getShortId()).isEqualTo(originalEx.getShortId());
            assertThat(copyEx.getType()).isEqualTo(originalEx.getType());
            assertThat(copyEx.getId()).isEqualTo(originalEx.getId());
            assertThat(copyEx.getMessage()).isEqualTo(originalEx.getMessage());

        }
        
    }

}
