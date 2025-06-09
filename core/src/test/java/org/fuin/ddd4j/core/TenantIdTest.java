package org.fuin.ddd4j.core;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test for the {@link TenantId} class.
 */
class TenantIdTest {

    @Test
    void testOK() {
        assertThat(new TenantId("ab").name()).isEqualTo("ab");
        assertThat(new TenantId("abcdefghij").name()).isEqualTo("abcdefghij");
        assertThat(new TenantId("a1").name()).isEqualTo("a1");
        assertThat(new TenantId("a_1").name()).isEqualTo("a_1");
        assertThat(new TenantId("a_b_1").name()).isEqualTo("a_b_1");
    }

    @Test
    void testMinLengthFails() {
        assertThatThrownBy(() -> new TenantId("a"))
                .hasMessage("Min length for name is 2 characters, but was: 1 (a)");
    }

    @Test
    void testMaxLengthFails() {
        assertThatThrownBy(() -> new TenantId("abcdefghijk"))
                .hasMessage("Max length for name is 10 characters, but was: 11 (abcdefghijk)");
    }

    @Test
    void testWrongPattern() {
        final String messagePrefix = "Expected pattern";
        assertThatThrownBy(() -> new TenantId("A1")).hasMessageContaining(messagePrefix);
        assertThatThrownBy(() -> new TenantId("_B")).hasMessageContaining(messagePrefix);
        assertThatThrownBy(() -> new TenantId("aB")).hasMessageContaining(messagePrefix);
        assertThatThrownBy(() -> new TenantId("Ab")).hasMessageContaining(messagePrefix);
        assertThatThrownBy(() -> new TenantId("x-1")).hasMessageContaining(messagePrefix);
        assertThatThrownBy(() -> new TenantId("x ")).hasMessageContaining(messagePrefix);
        assertThatThrownBy(() -> new TenantId("  ")).hasMessageContaining(messagePrefix);
        assertThatThrownBy(() -> new TenantId(" x")).hasMessageContaining(messagePrefix);
    }

}