/**
 * Copyright (C) 2015 Michael Schnell. All rights reserved. 
 * http://www.fuin.org/
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see http://www.gnu.org/licenses/.
 */
package org.fuin.ddd4j.esrepo;

import jakarta.validation.constraints.NotNull;
import org.fuin.ddd4j.ddd.*;
import org.fuin.esc.api.EventId;
import org.fuin.esc.api.*;
import org.fuin.objects4j.common.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Event store based repository.
 * 
 * @param <ID>
 *            Type of the aggregate root identifier.
 * @param <AGGREGATE>
 *            Type of the aggregate root.
 */
public abstract class EventStoreRepository<ID extends AggregateRootId, AGGREGATE extends AggregateRoot<ID>>
        implements Repository<ID, AGGREGATE> {

    private static final String MAX_AGGREGATE_VERSION_EXCEEDED = "Exceeded maximum number of aggregate versions."
            + " The Event Store operates with 'long' versions but aggregates only can handle 'int' versions.";

    private static final Logger LOG = LoggerFactory.getLogger(EventStoreRepository.class);

    private final EventStore eventStore;

    private final AggregateCache<AGGREGATE> noCache;

    /**
     * Constructor with all mandatory data.
     * 
     * @param eventStore
     *            Event store.
     */
    protected EventStoreRepository(@NotNull final EventStore eventStore) {
        super();

        Contract.requireArgNotNull("eventStore", eventStore);

        this.eventStore = eventStore;
        noCache = new AggregateNoCache<AGGREGATE>();
    }

    @Override
    public final AGGREGATE read(final ID aggregateId) throws AggregateNotFoundException, AggregateDeletedException {

        Contract.requireArgNotNull("aggregateId", aggregateId);
        try {
            AGGREGATE aggregate = getAggregateCache().get(aggregateId, null);
            if (aggregate == null) {
                LOG.debug("Aggregate {} not found in cache", aggregateId.asTypedString());
                aggregate = create();
            }
            return read(aggregate, aggregateId, Integer.MAX_VALUE);
        } catch (final AggregateVersionNotFoundException ex) {
            // Cannot happen because we requested the latest version
            throw new RuntimeException(ex);
        }
    }

    @Override
    public final AGGREGATE read(final ID aggregateId, final int version)
            throws AggregateNotFoundException, AggregateDeletedException, AggregateVersionNotFoundException {

        Contract.requireArgNotNull("aggregateId", aggregateId);

        AGGREGATE aggregate = getAggregateCache().get(aggregateId, version);
        if (aggregate == null) {
            LOG.debug("Aggregate {} not found in cache", aggregateId.asTypedString());
            aggregate = create();
        } else if (aggregate.getVersion() > version) {
            LOG.debug("Aggregate {} found in cache - Requested version {}, but found: {}", aggregateId.asTypedString(), version,
                    aggregate.getVersion());
            aggregate = create();
        } else if (aggregate.getVersion() == version) {
            LOG.debug("Aggregate {} found in cache with requested version: {}", aggregateId.asTypedString(), version);
            return aggregate;
        }
        return read(aggregate, aggregateId, version);
    }

    /**
     * Reads an aggregate.
     * 
     * @param aggregate
     *            Aggregate to load.
     * @param id
     *            Unique identifier of the aggregate.
     * @param targetVersion
     *            Version of the aggregate to load or {@link Integer#MAX_VALUE} to read the latest version.
     * 
     * @return Aggregate in target version.
     * 
     * @throws AggregateNotFoundException
     *             The given aggregate was not found.
     * @throws AggregateDeletedException
     *             The given aggregate was already deleted.
     * @throws AggregateVersionNotFoundException
     *             An aggregate with the requested version does not exist.
     */
    private AGGREGATE read(final AGGREGATE aggregate, final ID id, final int targetAggregateVersion)
            throws AggregateNotFoundException, AggregateDeletedException, AggregateVersionNotFoundException {

        requireNoUncommittedChanges(aggregate);

        LOG.info("Read aggregate: id={}, targetVersion={}", id.asTypedString(), targetAggregateVersion);

        final AggregateStreamId streamId = new AggregateStreamId(getAggregateType(), getIdParamName(), id);
        final int readPageSize = getReadPageSize();

        int sliceStart = aggregate.getVersion() + 1;
        StreamEventsSlice currentSlice;
        do {
            final int sliceCount;
            if (readPageSize <= targetAggregateVersion) {
                sliceCount = readPageSize;
            } else {
                sliceCount = targetAggregateVersion - sliceStart + 1;
            }

            try {
                LOG.debug("Read slice: streamId={}, sliceStart={}, sliceCount={}", streamId, sliceStart, sliceCount);
                currentSlice = getEventStore().readEventsForward(streamId, sliceStart, sliceCount);
                LOG.debug("Result slice: {}", currentSlice);
            } catch (final StreamNotFoundException ex) {
                throw new AggregateNotFoundException(getAggregateType(), id);
            } catch (final StreamDeletedException ex) {
                throw new AggregateDeletedException(getAggregateType(), id);
            }

            for (final CommonEvent commonEvent : currentSlice.getEvents()) {
                final DomainEvent<?> event = (DomainEvent<?>) commonEvent.getData();
                aggregate.loadFromHistory(event);
            }

            sliceStart = intVersion(currentSlice.getNextEventNumber());

        } while ((aggregate.getVersion() != targetAggregateVersion) && !currentSlice.isEndOfStream());

        if ((aggregate.getVersion() != targetAggregateVersion) && (targetAggregateVersion < Integer.MAX_VALUE)) {
            throw new AggregateVersionNotFoundException(getAggregateType(), id, targetAggregateVersion);
        }

        getAggregateCache().put(aggregate.getId(), aggregate);

        return aggregate;
    }

    private void requireNoUncommittedChanges(final AGGREGATE aggregate) {
        if (aggregate.hasUncommitedChanges()) {
            throw new IllegalArgumentException(
                    "The aggregate '" + getAggregateType() + "' (" + aggregate.getId() + ") has uncommitted changes");
        }
    }

    @Override
    public final void update(final AGGREGATE aggregate)
            throws AggregateVersionConflictException, AggregateNotFoundException, AggregateDeletedException {
        update(aggregate, null, null);
    }

    @Override
    public final void update(final AGGREGATE aggregate, final String metaType, final Object metaData)
            throws AggregateVersionConflictException, AggregateNotFoundException, AggregateDeletedException {

        Contract.requireArgNotNull("aggregate", aggregate);

        LOG.info("Update aggregate: id={}, version={}, nextVersion={}", aggregate.getId().asTypedString(), aggregate.getVersion(),
                aggregate.getNextVersion());

        final AggregateStreamId streamId = new AggregateStreamId(getAggregateType(), getIdParamName(), aggregate.getId());

        final List<DomainEvent<?>> events = aggregate.getUncommittedChanges();
        final List<CommonEvent> eventDataList = asCommonEvents(events, metaType, metaData);

        long expectedVersion = expectedVersion(aggregate);
        int retryCount = 0;
        boolean unsaved = true;
        do {
            try {
                final int eventStoreNextVersion = intVersion(getEventStore().appendToStream(streamId, expectedVersion, eventDataList));
                if ((expectedVersion + eventDataList.size()) != eventStoreNextVersion) {
                    throw new IllegalStateException(
                            "Aggregate next version is " + aggregate.getNextVersion() + " but event store's is " + eventStoreNextVersion);
                }
                aggregate.markChangesAsCommitted();
                unsaved = false;
            } catch (final WrongExpectedVersionException ex) {
                LOG.debug("Version conflict: id={}, expected={}, actual={}, retryCount={}", aggregate.getId().asTypedString(),
                        ex.getExpected(), ex.getActual(), retryCount);
                expectedVersion = resolveConflicts(aggregate, integerVersion(ex.getActual()), retryCount++);
            } catch (final StreamDeletedException | StreamNotFoundException ex) {
                throw new AggregateNotFoundException(getAggregateType(), aggregate.getId());
            }

        } while (unsaved);

    }

    @Override
    public void add(final AGGREGATE aggregate) throws AggregateAlreadyExistsException, AggregateDeletedException {
        add(aggregate, null, null);
    }

    @Override
    public void add(final AGGREGATE aggregate, final String metaType, final Object metaData)
            throws AggregateAlreadyExistsException, AggregateDeletedException {

        try {
            update(aggregate, metaType, metaData);
        } catch (final AggregateVersionConflictException ex) {
            throw new AggregateAlreadyExistsException(getAggregateType(), aggregate.getId(), ex.getActual());
        } catch (final AggregateNotFoundException ex) {
            throw new IllegalStateException(ex);
        }

    }

    private int expectedVersion(final AGGREGATE aggregate) {
        if (aggregate.getVersion() == -1) {
            return intVersion(ExpectedVersion.NO_OR_EMPTY_STREAM.getNo());
        }
        return aggregate.getVersion();
    }

    /**
     * Verifies if the changes conflict and returns a new expected number if not.
     * 
     * @param aggregate
     *            Aggregate to failed to be saved.
     * @param actualVersion
     *            Latest version from the event store.
     * @param retryCount
     *            Retry counter.
     * 
     * @return New expected version.
     * 
     * @throws AggregateVersionConflictException
     *             The expected version didn't match the actual version.
     * @throws AggregateDeletedException
     *             The aggregate with the given identifier was already deleted.
     * @throws AggregateNotFoundException
     *             An aggregate with the given identifier was not found.
     */
    private int resolveConflicts(final AGGREGATE aggregate, final Integer actualVersion, final int retryCount)
            throws AggregateVersionConflictException, AggregateNotFoundException, AggregateDeletedException {

        final int latestVersion;
        if (actualVersion == null || actualVersion < 0) {
            // TODO Remove workaround if event store returns latest version.
            // See https://github.com/EventStore/EventStore/issues/1052
            latestVersion = read(aggregate.getId()).getVersion();
        } else {
            latestVersion = actualVersion;
        }

        // Check how many times we should try
        if (retryCount == getMaxTryCount()) {
            throw new AggregateVersionConflictException(getAggregateType(), aggregate.getId(), aggregate.getVersion(), latestVersion);
        }

        // Load unseen events and try to resolve the conflict
        final List<DomainEvent<?>> unseenEvents = readEvents(aggregate.getId(), aggregate.getVersion() + 1);
        if (conflictsResolved(aggregate.getUncommittedChanges(), unseenEvents)) {
            return latestVersion;
        }
        throw new AggregateVersionConflictException(getAggregateType(), aggregate.getId(), aggregate.getVersion(), latestVersion);

    }

    @Override
    public final void delete(final ID aggregateId, final int expectedVersion) throws AggregateVersionConflictException {

        Contract.requireArgNotNull("aggregateId", aggregateId);

        LOG.info("Delete aggregate: id={}, expectedVersion={}", aggregateId.asTypedString(), expectedVersion);

        try {
            final AggregateStreamId streamId = new AggregateStreamId(getAggregateType(), getIdParamName(), aggregateId);
            getEventStore().deleteStream(streamId, expectedVersion, false);
        } catch (final WrongExpectedVersionException ex) {
            throw new AggregateVersionConflictException(getAggregateType(), aggregateId, integerVersion(ex.getExpected()),
                    integerVersion(ex.getActual()));
        } catch (final StreamDeletedException ex) {
            LOG.debug("Aggregate {} was already deleted: {}", aggregateId, ex.getMessage());
        }
    }

    /**
     * Reads all events for the given aggregate starting with a given number.
     * 
     * @param aggregateId
     *            Unique identifier of the aggregate to read the events for.
     * @param startVersion
     *            First event number to read.
     * 
     * @return List of events.
     * 
     * @throws AggregateNotFoundException
     *             An aggregate with the given identifier was not found.
     * @throws AggregateDeletedException
     *             The aggregate with the given identifier was already deleted.
     */
    private List<DomainEvent<?>> readEvents(final ID aggregateId, final int startVersion)
            throws AggregateNotFoundException, AggregateDeletedException {

        LOG.info("Read events: id={}, startVersion={}", aggregateId.asTypedString(), startVersion);

        final List<DomainEvent<?>> list = new ArrayList<DomainEvent<?>>();
        final AggregateStreamId streamId = new AggregateStreamId(getAggregateType(), getIdParamName(), aggregateId);
        final int sliceCount = getReadPageSize();

        int sliceStart = startVersion;
        StreamEventsSlice currentSlice;
        do {

            try {
                LOG.debug("Read slice: streamId={}, sliceStart={}, sliceCount={}", streamId, sliceStart, sliceCount);
                currentSlice = getEventStore().readEventsForward(streamId, sliceStart, sliceCount);
                LOG.debug("Result slice: {}", currentSlice);
            } catch (final StreamNotFoundException ex) {
                throw new AggregateNotFoundException(getAggregateType(), aggregateId);
            } catch (final StreamDeletedException ex) {
                throw new AggregateDeletedException(getAggregateType(), aggregateId);
            }

            for (final CommonEvent commonEvent : currentSlice.getEvents()) {
                final DomainEvent<?> event = (DomainEvent<?>) commonEvent.getData();
                list.add(event);
            }

            sliceStart = intVersion(currentSlice.getNextEventNumber());

        } while (!currentSlice.isEndOfStream());

        return list;
    }

    private List<CommonEvent> asCommonEvents(final List<DomainEvent<?>> events, final String metaType, final Object metaData) {
        final List<CommonEvent> list = new ArrayList<>();
        for (final DomainEvent<?> event : events) {
            final SimpleCommonEvent sce;
            if (metaData == null) {
                sce = new SimpleCommonEvent(new EventId(event.getEventId().asBaseType()), new TypeName(event.getEventType().asBaseType()),
                        event);
            } else {
                if (metaType == null) {
                    throw new IllegalArgumentException("Argument 'metaType' cannot be null if 'metaData' is provided (non-null)");
                }
                sce = new SimpleCommonEvent(new EventId(event.getEventId().asBaseType()), new TypeName(event.getEventType().asBaseType()),
                        event, new TypeName(metaType), metaData);
            }
            list.add(sce);
        }
        return list;
    }

    private int intVersion(final long version) {
        if (version > Integer.MAX_VALUE) {
            throw new IllegalStateException(MAX_AGGREGATE_VERSION_EXCEEDED);
        }
        return (int) version;
    }

    private Integer integerVersion(final Long version) {
        if (version == null) {
            return null;
        }
        if (version > Integer.MAX_VALUE) {
            throw new IllegalStateException(MAX_AGGREGATE_VERSION_EXCEEDED);
        }
        return version.intValue();
    }

    /**
     * Checks if the uncommitted changes conflicts with unseen changes from the event store and tries to solve the problem. This method may
     * be overwritten by concrete implementation. Returns FALSE as default if not overwritten in subclasses.
     * 
     * @param uncommittedChanges
     *            Uncommitted changes.
     * @param unseenEvents
     *            Unseen changes from the event store.
     * 
     * @return TRUE if there are no conflicting changes, else FALSE (conflict couldn't be resolved).
     */
    // CHECKSTYLE:OFF:DesignForExtension OK because method has no logic (just a
    // boolean).
    protected boolean conflictsResolved(final List<DomainEvent<?>> uncommittedChanges, final List<DomainEvent<?>> unseenEvents) {
        // CHECKSTYLE:ON:DesignForExtension
        return false;
    }

    /**
     * Returns the number of tries that should be done to resolve a version conflict. This method may be overwritten by concrete
     * implementation. Returns <code>3</code> as default if not overwritten in subclasses.
     * 
     * @return Number of tries.
     */
    // CHECKSTYLE:OFF:DesignForExtension OK because method has no logic (just an
    // integer).
    protected int getMaxTryCount() {
        // CHECKSTYLE:ON:DesignForExtension
        return 3;
    }

    /**
     * Returns the aggregate cache. This method may be overwritten by concrete implementation. Returns no cache as default if not
     * overwritten in subclasses.
     * 
     * @return Cache.
     */
    // CHECKSTYLE:OFF:DesignForExtension OK because method has no logic (just a
    // simple default impl).
    @NotNull
    protected AggregateCache<AGGREGATE> getAggregateCache() {
        // CHECKSTYLE:ON:DesignForExtension
        return noCache;
    }

    /**
     * Returns the number of events to read in a slice. This method may be overwritten by concrete implementation. Returns <code>100</code>
     * as default if not overwritten in subclasses.
     * 
     * @return Page size.
     */
    // CHECKSTYLE:OFF:DesignForExtension OK because method has no logic (just an
    // integer).
    public int getReadPageSize() {
        // CHECKSTYLE:ON:DesignForExtension
        return 100;
    }

    /**
     * Returns the underlying event store.
     * 
     * @return Event store implementation.
     */
    @NotNull
    protected final EventStore getEventStore() {
        return eventStore;
    }

    /**
     * Returns the parameter name for the unique identifier.
     * 
     * @return Name to be used as parameter.
     */
    @NotNull
    protected abstract String getIdParamName();

}
