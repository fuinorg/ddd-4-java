package org.fuin.ddd4j.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.fuin.objects4j.crypto.DuplicateEncryptionKeyIdException;
import org.junit.jupiter.api.Test;

import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
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

        final ObjectMapper objectMapper = TestUtils.objectMapper();

        // PREPARE
        final DuplicateEncryptionKeyIdException originalEx = new DuplicateEncryptionKeyIdException("xyz");
        final DuplicateEncryptionKeyIdExceptionData original = new DuplicateEncryptionKeyIdExceptionData(originalEx);

        // TEST
        final String json = objectMapper.writeValueAsString(original);
        final DuplicateEncryptionKeyIdExceptionData copy = objectMapper.readValue(json, DuplicateEncryptionKeyIdExceptionData.class);

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
    public final void testUnmarshal() throws Exception {

        final ObjectMapper objectMapper = TestUtils.objectMapper();

        // PREPARE
        final String json = """
                {
                    "msg" : "Duplicate keyId: xyz",
                    "sid" : "OBJECTS4J-CRYPTO-DUPLICATE-ENCRYPTION_KEY_ID",
                    "key-id" : "xyz"
                }
                """;

        // TEST
        final DuplicateEncryptionKeyIdExceptionData copy = objectMapper.readValue(json, DuplicateEncryptionKeyIdExceptionData.class);

        // VERIFY
        final String copyJson = objectMapper.writeValueAsString(copy);
        assertThatJson(copyJson).isEqualTo(json);

    }

}
