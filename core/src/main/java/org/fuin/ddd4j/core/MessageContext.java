package org.fuin.ddd4j.core;

import org.fuin.objects4j.common.ThreadSafe;

import java.util.Optional;

/**
 * Ambient context that carries the correlation and causation information of the message (command or event)
 * that is currently being processed. Produced events pick these values up automatically so the causal graph
 * described by {@link Event#getCorrelationId()} / {@link Event#getCausationId()} can be reconstructed without
 * per-call-site discipline.
 * <p>
 * <b>CAUTION:</b> Implementation must be thread-safe!
 * </p>
 */
@ThreadSafe
public interface MessageContext {

    /**
     * Returns the correlation identifier of the conversation the current message belongs to. This value is
     * copied to the {@link Event#getCorrelationId()} of events produced while the message is being handled.
     *
     * @return Current correlation identifier.
     */
    Optional<EventId> getCurrentCorrelationId();

    /**
     * Returns the identifier of the message that is currently being processed. This value is used as the
     * {@link Event#getCausationId()} of events produced while the message is being handled.
     *
     * @return Current message identifier.
     */
    Optional<EventId> getCurrentMessageId();

}
