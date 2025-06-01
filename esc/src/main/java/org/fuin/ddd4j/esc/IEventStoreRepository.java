package org.fuin.ddd4j.esc;

import org.fuin.ddd4j.core.AggregateDeletedException;
import org.fuin.ddd4j.core.AggregateNotFoundException;
import org.fuin.ddd4j.core.AggregateRoot;
import org.fuin.ddd4j.core.AggregateRootId;
import org.fuin.ddd4j.core.DomainEvent;
import org.fuin.ddd4j.core.Repository;

import java.util.List;

/**
 * Additional methods related to event store based repositories.
 */
public interface IEventStoreRepository<ID extends AggregateRootId, AGGREGATE extends AggregateRoot<ID>> extends Repository<ID, AGGREGATE> {

    /**
     * Reads all events for the given aggregate starting with a given number.
     *
     * @param aggregateId  Unique identifier of the aggregate to read the events for.
     * @param startVersion First event number to read.
     * @return List of events.
     * @throws AggregateNotFoundException An aggregate with the given identifier was not found.
     * @throws AggregateDeletedException  The aggregate with the given identifier was already deleted.
     */
    List<DomainEvent<?>> readEvents(final ID aggregateId, final int startVersion)
            throws AggregateNotFoundException, AggregateDeletedException;

}
