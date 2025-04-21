package org.fuin.ddd4j.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.fuin.ddd4j.core.EncryptionKeyVersionUnknownException;
import org.junit.jupiter.api.Test;

import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
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

        final ObjectMapper objectMapper = TestUtils.objectMapper();

        // PREPARE
        final EncryptionKeyVersionUnknownException originalEx = new EncryptionKeyVersionUnknownException("1");
        final EncryptionKeyVersionUnknownExceptionData original = new EncryptionKeyVersionUnknownExceptionData(originalEx);

        // TEST
        final String json = objectMapper.writeValueAsString(original);
        final EncryptionKeyVersionUnknownExceptionData copy = objectMapper.readValue(json, EncryptionKeyVersionUnknownExceptionData.class);

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
    public final void testUnmarshal() throws Exception {

        final ObjectMapper objectMapper = TestUtils.objectMapper();

        // PREPARE
        final String json = """
                {
                    "msg" : "Unknown keyVersion: 1",
                    "sid" : "DDD4J-ENCRYPTION_KEY_VERSION_UNKNOWN",
                    "key-version" : "1"
                }
                """;

        // TEST
        final EncryptionKeyVersionUnknownExceptionData copy = objectMapper.readValue(json, EncryptionKeyVersionUnknownExceptionData.class);

        // VERIFY
        final String copyJson = objectMapper.writeValueAsString(copy);
        assertThatJson(copyJson).isEqualTo(json);

    }

}
