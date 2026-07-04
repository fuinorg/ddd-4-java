package org.fuin.ddd4j.core;

import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.common.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Optional;

/**
 * Container for the currently processed message based on a {@link ThreadLocal}. Event builders consult the
 * static {@link #currentCorrelationId()} / {@link #currentMessageId()} accessors to auto-propagate the
 * correlation and causation identifiers onto the events they create.
 */
@ThreadSafe
public class ThreadLocalMessageContext implements WritableMessageContext {

    private static final Logger LOG = LoggerFactory.getLogger(ThreadLocalMessageContext.class);

    private static final ThreadLocal<EventId> CURRENT_MESSAGE = new InheritableThreadLocal<>();

    private static final ThreadLocal<EventId> CURRENT_CORRELATION = new InheritableThreadLocal<>();

    private static final String MESSAGE_ID_KEY = "messageId";

    private static final String CORRELATION_ID_KEY = "correlationId";

    @Override
    public Optional<EventId> getCurrentCorrelationId() {
        return currentCorrelationId();
    }

    @Override
    public Optional<EventId> getCurrentMessageId() {
        return currentMessageId();
    }

    @Override
    public void setCurrentMessage(final EventId messageId) {
        setCurrentMessage(messageId, messageId);
    }

    @Override
    public void setCurrentMessage(final EventId messageId, final EventId correlationId) {
        Contract.requireArgNotNull("messageId", messageId);
        Contract.requireArgNotNull("correlationId", correlationId);
        CURRENT_MESSAGE.set(messageId);
        CURRENT_CORRELATION.set(correlationId);
        MDC.put(MESSAGE_ID_KEY, messageId.asString());
        MDC.put(CORRELATION_ID_KEY, correlationId.asString());
        LOG.debug("Message: messageId={}, correlationId={}", messageId, correlationId);
    }

    @Override
    public void clear() {
        CURRENT_MESSAGE.remove();
        CURRENT_CORRELATION.remove();
        MDC.remove(MESSAGE_ID_KEY);
        MDC.remove(CORRELATION_ID_KEY);
        LOG.debug("Cleared message context");
    }

    /**
     * Returns the correlation identifier of the message currently bound to the calling thread.
     *
     * @return Current correlation identifier or empty.
     */
    public static Optional<EventId> currentCorrelationId() {
        return Optional.ofNullable(CURRENT_CORRELATION.get());
    }

    /**
     * Returns the identifier of the message currently bound to the calling thread.
     *
     * @return Current message identifier or empty.
     */
    public static Optional<EventId> currentMessageId() {
        return Optional.ofNullable(CURRENT_MESSAGE.get());
    }

}
