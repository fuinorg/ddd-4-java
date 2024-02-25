package org.fuin.ddd4j.core;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for the {@link EventId} class.
 */
public class EventIdTest {

    @Test
    void testCreate() {
        final UUID id = UUID.randomUUID();
        assertThat(new EventId(id).asBaseType()).isEqualTo(id);
        assertThat(new EventId(id).asString()).isEqualTo(id.toString());
    }

    @Test
    void testValueOf() {
        final String idStr = "630649f9-b166-4cec-b767-bdcdd38b5a00";
        assertThat(EventId.valueOf(null)).isNull();
        assertThat(EventId.valueOf(idStr)).isEqualTo(new EventId(UUID.fromString(idStr)));
    }

}
