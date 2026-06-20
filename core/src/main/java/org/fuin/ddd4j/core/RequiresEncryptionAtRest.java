package org.fuin.ddd4j.core;

import jakarta.validation.constraints.NotNull;

/**
 * An event that wants to be stored fully encrypted.
 *
 * @param <ID>
 *            Type of the aggregate root identifier.
 */
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
