package org.fuin.ddd4j.jsonb;

import jakarta.json.bind.Jsonb;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.fuin.ddd4j.core.DuplicateEncryptionKeyIdException;
import org.junit.jupiter.api.Test;

import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.fuin.ddd4j.jsonb.TestUtils.jsonb;
import static org.fuin.utils4j.Utils4J.deserialize;
import static org.fuin.utils4j.Utils4J.serialize;

/**
 * Test for the {@link DuplicateEncryptionKeyIdExceptionData} class.
 */
class DuplicateEncryptionKeyIdExceptionDataTest {

    @Test
    final void testEqualsHashCode() {
        EqualsVerifier.simple().forClass(DuplicateEncryptionKeyIdExceptionData.class).verify();
    }

    @Test
    public final void testSerializeDeserialize() {

        // PREPARE
        final DuplicateEncryptionKeyIdException originalEx = new DuplicateEncryptionKeyIdException("xyz");
        final DuplicateEncryptionKeyIdExceptionData original = new DuplicateEncryptionKeyIdExceptionData(originalEx);

        // TEST
        final DuplicateEncryptionKeyIdExceptionData copy = deserialize(serialize(original));

        // VERIFY
        assertThat(copy).isEqualTo(original);
        assertThat(copy.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copy.getShortId()).isEqualTo(originalEx.getShortId());
        final DuplicateEncryptionKeyIdException copyEx = copy.toException();
        assertThat(copyEx.getShortId()).isEqualTo(originalEx.getShortId());
        assertThat(copyEx.getMessage()).isEqualTo(originalEx.getMessage());
        assertThat(copyEx.getKeyId()).isEqualTo(originalEx.getKeyId());

    }

    @Test
    public final void testMarshalUnmarshal() throws Exception {

        try (final Jsonb jsonb = jsonb()) {

            // PREPARE
            final DuplicateEncryptionKeyIdException originalEx = new DuplicateEncryptionKeyIdException("xyz");
            final DuplicateEncryptionKeyIdExceptionData original = new DuplicateEncryptionKeyIdExceptionData(originalEx);

            // TEST
            final String json = jsonb.toJson(original, DuplicateEncryptionKeyIdExceptionData.class);
            final DuplicateEncryptionKeyIdExceptionData copy = jsonb.fromJson(json, DuplicateEncryptionKeyIdExceptionData.class);

            // VERIFY
            assertThat(copy).isEqualTo(original);
            assertThat(copy.getMessage()).isEqualTo(originalEx.getMessage());
            assertThat(copy.getShortId()).isEqualTo(originalEx.getShortId());
            final DuplicateEncryptionKeyIdException copyEx = copy.toException();
            assertThat(copyEx.getShortId()).isEqualTo(originalEx.getShortId());
            assertThat(copyEx.getMessage()).isEqualTo(originalEx.getMessage());
            assertThat(copyEx.getKeyId()).isEqualTo(originalEx.getKeyId());

        }
        
    }

    @Test
    public final void testUnmarshal() throws Exception {

        try (final Jsonb jsonb = jsonb()) {

            // PREPARE
            final String json = """
                    {
                        "msg" : "Duplicate keyId: xyz",
                        "sid" : "DDD4J-DUPLICATE-ENCRYPTION_KEY_ID",
                        "key-id" : "xyz"
                    }
                    """;

            // TEST
            final DuplicateEncryptionKeyIdExceptionData copy = jsonb.fromJson(json, DuplicateEncryptionKeyIdExceptionData.class);

            // VERIFY
            final String copyJson = jsonb.toJson(copy);
            assertThatJson(copyJson).isEqualTo(json);

        }
        
    }

}
