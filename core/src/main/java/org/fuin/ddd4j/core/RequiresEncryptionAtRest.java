package org.fuin.ddd4j.core;

import jakarta.validation.constraints.NotNull;
import org.fuin.objects4j.common.ThreadSafe;

/**
 * An event that wants to be stored fully encrypted.
 * All implementations are expected to be thread safe.
 *
 * @param <ID>
 *            Type of the aggregate root identifier.
 */
@ThreadSafe
public interface RequiresEncryptionAtRest<ID extends AggregateRootId> {

    /**
     * Returns the path to the originator of the event.
     *
     * @return List of unique identifiers from aggregate root to the entity that emitted the event.
     */
    @NotNull
    EntityIdPath getEntityIdPath();

    /**
     * Returns the identifier of the entity that caused this event. This is the last ID in the path.
     *
     * @return Entity identifier.
     */
    @NotNull
    ID getEntityId();

}
