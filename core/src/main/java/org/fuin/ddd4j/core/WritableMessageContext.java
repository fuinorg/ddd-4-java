package org.fuin.ddd4j.core;

import org.fuin.objects4j.common.ThreadSafe;

/**
 * A {@link MessageContext} where the currently processed message can be set and reset. A handler sets the
 * inbound message before it reacts to it and clears the context afterwards.
 * <p>
 * <b>CAUTION:</b> Implementation must be thread-safe!
 * </p>
 */
@ThreadSafe
public interface WritableMessageContext extends MessageContext {

    /**
     * Marks the given message as currently being processed and starts a new causal chain by using the
     * message identifier as its own correlation identifier.
     *
     * @param messageId Identifier of the message that is being handled.
     */
    void setCurrentMessage(EventId messageId);

    /**
     * Marks the given message as currently being processed within an existing conversation.
     *
     * @param messageId     Identifier of the message that is being handled (becomes the causation id of
     *                      produced events).
     * @param correlationId Correlation identifier of the conversation (copied to produced events).
     */
    void setCurrentMessage(EventId messageId, EventId correlationId);

    /**
     * Clears the current message context.
     */
    void clear();

}
