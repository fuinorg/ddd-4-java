package org.fuin.ddd4j.jsonb;

import jakarta.json.bind.Jsonb;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.fuin.ddd4j.core.DecryptionFailedException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fuin.ddd4j.jsonb.TestUtils.jsonb;
import static org.fuin.utils4j.Utils4J.deserialize;
import static org.fuin.utils4j.Utils4J.serialize;

/**
 * Test for the {@link DecryptionFailedExceptionData} class.
 */
class DecryptionFailedExceptionDataTest {

    @Test
    final void testEqualsHashCode() {
        EqualsVerifier.simple().forClass(DecryptionFailedExceptionData.class).verify();
    }

    @Test
    public final void testSerializeDeserialize() throws Exception {

        // PREPARE
        final RuntimeException cause = new RuntimeException("Foo Bar");
        final DecryptionFailedException originalEx = new DecryptionFailedException(cause);
        final DecryptionFailedExceptionData original = new DecryptionFailedExceptionData(originalEx);

        // TEST
        final DecryptionFailedExceptionData copy = deserialize(serialize(original));

        // VERIFY
        assertThat(copy).isEqualTo(original);
        assertThat(copy.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copy.getShortId()).isEqualTo(originalEx.getShortId());
        final DecryptionFailedException copyEx = copy.toException();
        assertThat(copyEx.getShortId()).isEqualTo(originalEx.getShortId());
        assertThat(copyEx.getMessage()).isEqualTo(originalEx.getMessage());

    }

    @Test
    public final void testMarshalUnmarshal() throws Exception {

        try (final Jsonb jsonb = jsonb()) {

            // PREPARE
            final RuntimeException cause = new RuntimeException("Foo Bar");
            final DecryptionFailedException originalEx = new DecryptionFailedException(cause);
            final DecryptionFailedExceptionData original = new DecryptionFailedExceptionData(originalEx);

            // TEST
            final String json = jsonb.toJson(original, DecryptionFailedExceptionData.class);
            final DecryptionFailedExceptionData copy = jsonb.fromJson(json, DecryptionFailedExceptionData.class);

            // VERIFY
            assertThat(copy).isEqualTo(original);
            assertThat(copy.getMessage()).isEqualTo(originalEx.getMessage());
            assertThat(copy.getShortId()).isEqualTo(originalEx.getShortId());
            final DecryptionFailedException copyEx = copy.toException();
            assertThat(copyEx.getShortId()).isEqualTo(originalEx.getShortId());
            assertThat(copyEx.getMessage()).isEqualTo(originalEx.getMessage());

        }
        
    }

    @Test
    public final void testUnmarshal() throws Exception {

        try (final Jsonb jsonb = jsonb()) {

            // PREPARE
            final RuntimeException cause = new RuntimeException("Foo Bar");
            final DecryptionFailedException originalEx = new DecryptionFailedException(cause);
            final String json = """
                    {
                        "msg" : "Decryption failed: Foo Bar",
                        "sid" : "DDD4J-DECRYPTION_FAILED"
                    }
                    """;

            // TEST
            final DecryptionFailedExceptionData copy = jsonb.fromJson(json, DecryptionFailedExceptionData.class);

            // VERIFY
            final DecryptionFailedException copyEx = copy.toException();
            assertThat(copy.getMessage()).isEqualTo(originalEx.getMessage());
            assertThat(copy.getShortId()).isEqualTo(originalEx.getShortId());
            assertThat(copyEx.getShortId()).isEqualTo(originalEx.getShortId());
            assertThat(copyEx.getMessage()).isEqualTo(originalEx.getMessage());

        }
        
    }

}
