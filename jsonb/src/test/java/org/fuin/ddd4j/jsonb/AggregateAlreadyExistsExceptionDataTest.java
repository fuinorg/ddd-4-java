package org.fuin.ddd4j.jsonb;

import jakarta.json.bind.Jsonb;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.fuin.ddd4j.core.AggregateAlreadyExistsException;
import org.fuin.ddd4j.jsonbtest.VendorId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.fuin.ddd4j.jsonb.TestUtils.jsonb;
import static org.fuin.utils4j.Utils4J.deserialize;
import static org.fuin.utils4j.Utils4J.serialize;

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
        assertThat(copyEx.getShortId()).isEqualTo(originalEx.getShortId());
        assertThat(copyEx.getType()).isEqualTo(originalEx.getType());
        assertThat(copyEx.getId()).isEqualTo(originalEx.getId());
        assertThat(copyEx.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copyEx.getVersion()).isEqualTo(originalEx.getVersion());

    }

    @Test
    public final void testMarshalUnmarshal() throws Exception {

        try (final Jsonb jsonb = jsonb()) {

            // PREPARE
            final AggregateAlreadyExistsException originalEx = new AggregateAlreadyExistsException(VendorId.TYPE,
                    new VendorId(UUID.fromString("4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119")), 102);
            final AggregateAlreadyExistsExceptionData original = new AggregateAlreadyExistsExceptionData(originalEx);

            // TEST
            final String json = jsonb.toJson(original, AggregateAlreadyExistsExceptionData.class);
            final AggregateAlreadyExistsExceptionData copy = jsonb.fromJson(json, AggregateAlreadyExistsExceptionData.class);

            // VERIFY
            assertThat(copy).isEqualTo(original);
            final AggregateAlreadyExistsException copyEx = copy.toException();
            assertThat(copyEx.getShortId()).isEqualTo(originalEx.getShortId());
            assertThat(copyEx.getType()).isEqualTo(originalEx.getType());
            assertThat(copyEx.getId()).isEqualTo(originalEx.getId());
            assertThat(copyEx.getMessage()).isEqualTo(originalEx.getMessage());
            assertThat(copyEx.getVersion()).isEqualTo(originalEx.getVersion());

        }

    }

    @Test
    public final void testUnmarshal() throws Exception {

        try (final Jsonb jsonb = jsonb()) {

            // PREPARE
            final String json = """
                    {
                        "msg" : "Vendor 4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119 already exists (version=102)",
                        "sid" : "DDD4J-AGGREGATE_ALREADY_EXISTS",
                        "aggregate-type" : "Vendor",
                        "aggregate-id" : "4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119",
                        "version" : 102
                    }
                    """;

            // TEST
            final AggregateAlreadyExistsExceptionData copy = jsonb.fromJson(json, AggregateAlreadyExistsExceptionData.class);

            // VERIFY
            final String copyJson = jsonb.toJson(copy);
            assertThatJson(copyJson).isEqualTo(json);

        }

    }

}
