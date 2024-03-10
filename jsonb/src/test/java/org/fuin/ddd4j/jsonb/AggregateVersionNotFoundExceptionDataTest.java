package org.fuin.ddd4j.jsonb;

import jakarta.json.bind.Jsonb;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.fuin.ddd4j.core.AggregateVersionNotFoundException;
import org.fuin.ddd4j.jsonbtest.VendorId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fuin.ddd4j.jsonb.TestUtils.jsonb;
import static org.fuin.utils4j.Utils4J.deserialize;
import static org.fuin.utils4j.Utils4J.serialize;

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
    public final void testMarshalUnmarshal() throws Exception {

        try (final Jsonb jsonb = jsonb()) {

            // PREPARE
            final AggregateVersionNotFoundException originalEx = new AggregateVersionNotFoundException(VendorId.TYPE,
                    new VendorId(UUID.fromString("4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119")), 102);
            final AggregateVersionNotFoundExceptionData original = new AggregateVersionNotFoundExceptionData(originalEx);

            // TEST
            final String json = jsonb.toJson(original, AggregateVersionNotFoundExceptionData.class);
            final AggregateVersionNotFoundExceptionData copy = jsonb.fromJson(json, AggregateVersionNotFoundExceptionData.class);

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
        
    }

    @Test
    public final void testUnmarshal() throws Exception {

        try (final Jsonb jsonb = jsonb()) {

            // PREPARE
            final AggregateVersionNotFoundException originalEx = new AggregateVersionNotFoundException(VendorId.TYPE,
                    new VendorId(UUID.fromString("4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119")), 102);
            final String json = """
                    {
                        "msg" : "Requested version 102 for Vendor (4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119) does not exist",
                        "sid" : "DDD4J-AGGREGATE_VERSION_NOT_FOUND",
                        "aggregate-type" : "Vendor",
                        "aggregate-id" : "4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119",
                        "version" : "102"
                    }
                    """;

            // TEST
            final AggregateVersionNotFoundExceptionData copy = jsonb.fromJson(json, AggregateVersionNotFoundExceptionData.class);

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
        
    }

}
