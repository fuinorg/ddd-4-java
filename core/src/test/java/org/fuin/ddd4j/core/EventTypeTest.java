package org.fuin.ddd4j.core;

import org.fuin.objects4j.common.ConstraintViolationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test for the {@link EventType} class.
 */
public class EventTypeTest {

    @Test
    void testCreateOK() {
        final String type = "EventA";
        assertThat(new EventType(type).asBaseType()).isEqualTo(type);
        assertThat(new EventType(type).asString()).isEqualTo(type);
    }

    @Test
    void testCreateFailure() {

        assertThatThrownBy(() -> new EventType(null))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("The argument 'str' cannot be null");

        assertThatThrownBy(() -> new EventType(""))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("The argument 'str' cannot be empty");

        assertThatThrownBy(() -> new EventType("a".repeat(256)))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Max length of argument 'str' is 255, but was: 256");

    }

}
