package org.fuin.ddd4j.jsonb;

import jakarta.json.bind.Jsonb;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.fuin.ddd4j.core.EncryptionKeyIdUnknownException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fuin.ddd4j.jsonb.TestUtils.jsonb;
import static org.fuin.utils4j.Utils4J.deserialize;
import static org.fuin.utils4j.Utils4J.serialize;

/**
 * Test for the {@link EncryptionKeyIdUnknownExceptionData} class.
 */
class EncryptionKeyIdUnknownExceptionDataTest {

    @Test
    final void testEqualsHashCode() {
        EqualsVerifier.simple().forClass(EncryptionKeyIdUnknownExceptionData.class).verify();
    }

    @Test
    public final void testSerializeDeserialize() {

        // PREPARE
        final EncryptionKeyIdUnknownException originalEx = new EncryptionKeyIdUnknownException("xyz");
        final EncryptionKeyIdUnknownExceptionData original = new EncryptionKeyIdUnknownExceptionData(originalEx);

        // TEST
        final EncryptionKeyIdUnknownExceptionData copy = deserialize(serialize(original));

        // VERIFY
        assertThat(copy).isEqualTo(original);
        assertThat(copy.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copy.getShortId()).isEqualTo(originalEx.getShortId());
        final EncryptionKeyIdUnknownException copyEx = copy.toException();
        assertThat(copyEx.getShortId()).isEqualTo(originalEx.getShortId());
        assertThat(copyEx.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copyEx.getKeyId()).isEqualTo(originalEx.getKeyId());

    }

    @Test
    public final void testMarshalUnmarshal() throws Exception {

        try (final Jsonb jsonb = jsonb()) {

            // PREPARE
            final EncryptionKeyIdUnknownException originalEx = new EncryptionKeyIdUnknownException("xyz");
            final EncryptionKeyIdUnknownExceptionData original = new EncryptionKeyIdUnknownExceptionData(originalEx);

            // TEST
            final String json = jsonb.toJson(original, EncryptionKeyIdUnknownExceptionData.class);
            final EncryptionKeyIdUnknownExceptionData copy = jsonb.fromJson(json, EncryptionKeyIdUnknownExceptionData.class);

            // VERIFY
            assertThat(copy).isEqualTo(original);
            assertThat(copy.getMessage()).isEqualTo(originalEx.getMessage());
            assertThat(copy.getShortId()).isEqualTo(originalEx.getShortId());
            final EncryptionKeyIdUnknownException copyEx = copy.toException();
            assertThat(copyEx.getShortId()).isEqualTo(originalEx.getShortId());
            assertThat(copyEx.getMessage()).isEqualTo(originalEx.getMessage());
            assertThat(copyEx.getKeyId()).isEqualTo(originalEx.getKeyId());

        }
        
    }

    @Test
    public final void testUnmarshal() throws Exception {

        try (final Jsonb jsonb = jsonb()) {

            // PREPARE
            final EncryptionKeyIdUnknownException originalEx = new EncryptionKeyIdUnknownException("xyz");
            final String json = """
                    {
                        "msg" : "Unknown keyId: xyz",
                        "sid" : "DDD4J-ENCRYPTION_KEY_ID_UNKNOWN",
                        "key-id" : "xyz"
                    }
                    """;

            // TEST
            final EncryptionKeyIdUnknownExceptionData copy = jsonb.fromJson(json, EncryptionKeyIdUnknownExceptionData.class);

            // VERIFY
            final EncryptionKeyIdUnknownException copyEx = copy.toException();
            assertThat(copy.getMessage()).isEqualTo(originalEx.getMessage());
            assertThat(copy.getShortId()).isEqualTo(originalEx.getShortId());
            assertThat(copyEx.getShortId()).isEqualTo(originalEx.getShortId());
            assertThat(copyEx.getMessage()).isEqualTo(originalEx.getMessage());
            assertThat(copyEx.getKeyId()).isEqualTo(originalEx.getKeyId());

        }
        
    }

}
