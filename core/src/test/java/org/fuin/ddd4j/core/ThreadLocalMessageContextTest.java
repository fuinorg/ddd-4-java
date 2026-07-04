package org.fuin.ddd4j.core;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the {@link ThreadLocalMessageContext} class.
 */
public class ThreadLocalMessageContextTest {

    private final ThreadLocalMessageContext testee = new ThreadLocalMessageContext();

    @AfterEach
    public void tearDown() {
        testee.clear();
    }

    @Test
    public void testEmptyByDefault() {

        // TEST & VERIFY
        assertThat(testee.getCurrentMessageId()).isEmpty();
        assertThat(testee.getCurrentCorrelationId()).isEmpty();
        assertThat(ThreadLocalMessageContext.currentMessageId()).isEmpty();
        assertThat(ThreadLocalMessageContext.currentCorrelationId()).isEmpty();

    }

    @Test
    public void testSetCurrentMessageStartsNewConversation() {

        // PREPARE
        final EventId messageId = new EventId();

        // TEST: a single-argument call seeds a new causal chain (correlation = message id)
        testee.setCurrentMessage(messageId);

        // VERIFY
        assertThat(testee.getCurrentMessageId()).contains(messageId);
        assertThat(testee.getCurrentCorrelationId()).contains(messageId);

    }

    @Test
    public void testSetCurrentMessageWithinConversation() {

        // PREPARE
        final EventId messageId = new EventId();
        final EventId correlationId = new EventId();

        // TEST
        testee.setCurrentMessage(messageId, correlationId);

        // VERIFY
        assertThat(testee.getCurrentMessageId()).contains(messageId);
        assertThat(testee.getCurrentCorrelationId()).contains(correlationId);
        assertThat(ThreadLocalMessageContext.currentMessageId()).contains(messageId);
        assertThat(ThreadLocalMessageContext.currentCorrelationId()).contains(correlationId);

    }

    @Test
    public void testClear() {

        // PREPARE
        testee.setCurrentMessage(new EventId(), new EventId());

        // TEST
        testee.clear();

        // VERIFY
        assertThat(testee.getCurrentMessageId()).isEmpty();
        assertThat(testee.getCurrentCorrelationId()).isEmpty();

    }

}
