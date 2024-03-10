package org.fuin.ddd4j.jsonb;

import jakarta.json.bind.Jsonb;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.fuin.ddd4j.core.EncryptionKeyVersionUnknownException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fuin.ddd4j.jsonb.TestUtils.jsonb;
import static org.fuin.utils4j.Utils4J.deserialize;
import static org.fuin.utils4j.Utils4J.serialize;

/**
 * Test for the {@link EncryptionKeyVersionUnknownExceptionData} class.
 */
class EncryptionKeyVersionUnknownExceptionDataTest {

    @Test
    final void testEqualsHashCode() {
        EqualsVerifier.simple().forClass(EncryptionKeyVersionUnknownExceptionData.class).verify();
    }

    @Test
    public final void testSerializeDeserialize() {

        // PREPARE
        final EncryptionKeyVersionUnknownException originalEx = new EncryptionKeyVersionUnknownException("1");
        final EncryptionKeyVersionUnknownExceptionData original = new EncryptionKeyVersionUnknownExceptionData(originalEx);

        // TEST
        final EncryptionKeyVersionUnknownExceptionData copy = deserialize(serialize(original));

        // VERIFY
        assertThat(copy).isEqualTo(original);
        assertThat(copy.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copy.getShortId()).isEqualTo(originalEx.getShortId());
        final EncryptionKeyVersionUnknownException copyEx = copy.toException();
        assertThat(copyEx.getShortId()).isEqualTo(originalEx.getShortId());
        assertThat(copyEx.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copyEx.getKeyVersion()).isEqualTo(originalEx.getKeyVersion());

    }

    @Test
    public final void testMarshalUnmarshal() throws Exception {

        try (final Jsonb jsonb = jsonb()) {

            // PREPARE
            final EncryptionKeyVersionUnknownException originalEx = new EncryptionKeyVersionUnknownException("1");
            final EncryptionKeyVersionUnknownExceptionData original = new EncryptionKeyVersionUnknownExceptionData(originalEx);

            // TEST
            final String json = jsonb.toJson(original, EncryptionKeyVersionUnknownExceptionData.class);
            final EncryptionKeyVersionUnknownExceptionData copy = jsonb.fromJson(json, EncryptionKeyVersionUnknownExceptionData.class);

            // VERIFY
            assertThat(copy).isEqualTo(original);
            assertThat(copy.getMessage()).isEqualTo(originalEx.getMessage());
            assertThat(copy.getShortId()).isEqualTo(originalEx.getShortId());
            final EncryptionKeyVersionUnknownException copyEx = copy.toException();
            assertThat(copyEx.getShortId()).isEqualTo(originalEx.getShortId());
            assertThat(copyEx.getMessage()).isEqualTo(originalEx.getMessage());
            assertThat(copyEx.getKeyVersion()).isEqualTo(originalEx.getKeyVersion());

        }
        
    }

    @Test
    public final void testUnmarshal() throws Exception {

        try (final Jsonb jsonb = jsonb()) {

            // PREPARE
            final EncryptionKeyVersionUnknownException originalEx = new EncryptionKeyVersionUnknownException("1");
            final String json = """
                    {
                        "msg" : "Unknown keyVersion: 1",
                        "sid" : "DDD4J-ENCRYPTION_KEY_VERSION_UNKNOWN",
                        "key-version" : "1"
                    }
                    """;

            // TEST
            final EncryptionKeyVersionUnknownExceptionData copy = jsonb.fromJson(json, EncryptionKeyVersionUnknownExceptionData.class);

            // VERIFY
            final EncryptionKeyVersionUnknownException copyEx = copy.toException();
            assertThat(copy.getMessage()).isEqualTo(originalEx.getMessage());
            assertThat(copy.getShortId()).isEqualTo(originalEx.getShortId());
            assertThat(copyEx.getShortId()).isEqualTo(originalEx.getShortId());
            assertThat(copyEx.getMessage()).isEqualTo(originalEx.getMessage());
            assertThat(copyEx.getKeyVersion()).isEqualTo(originalEx.getKeyVersion());

        }
        
    }

}
