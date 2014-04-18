/**
 * Copyright (C) 2013 Future Invent Informationsmanagement GmbH. All rights
 * reserved. <http://www.fuin.org/>
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
 * along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fuin.ddd4j.esrepo;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.fuin.ddd4j.ddd.AggregateCache;
import org.fuin.ddd4j.ddd.AggregateDeletedException;
import org.fuin.ddd4j.ddd.AggregateNoCache;
import org.fuin.ddd4j.ddd.AggregateNotFoundException;
import org.fuin.ddd4j.ddd.AggregateRoot;
import org.fuin.ddd4j.ddd.AggregateRootId;
import org.fuin.ddd4j.ddd.AggregateVersionConflictException;
import org.fuin.ddd4j.ddd.AggregateVersionNotFoundException;
import org.fuin.ddd4j.ddd.Deserializer;
import org.fuin.ddd4j.ddd.DeserializerRegistry;
import org.fuin.ddd4j.ddd.DomainEvent;
import org.fuin.ddd4j.ddd.Event;
import org.fuin.ddd4j.ddd.MetaData;
import org.fuin.ddd4j.ddd.Repository;
import org.fuin.ddd4j.ddd.Serializer;
import org.fuin.ddd4j.eventstore.intf.Data;
import org.fuin.ddd4j.eventstore.intf.EventData;
import org.fuin.ddd4j.eventstore.intf.EventStore;
import org.fuin.ddd4j.eventstore.intf.StreamDeletedException;
import org.fuin.ddd4j.eventstore.intf.StreamEventsSlice;
import org.fuin.ddd4j.eventstore.intf.StreamNotFoundException;
import org.fuin.ddd4j.eventstore.intf.StreamVersionConflictException;
import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.common.NeverNull;

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

	private final EventStore eventStore;

	private final Serializer serializer;

	private final DeserializerRegistry registry;

	private final AggregateCache<AGGREGATE> noCache;

	/**
	 * Constructor with all data.
	 * 
	 * @param eventStore Event store.
	 * @param serializer Serializer to use.
	 * @param registry Registry used to locate deserializers.
	 */
	protected EventStoreRepository(@NotNull final EventStore eventStore,
			@NotNull final Serializer serializer, @NotNull final DeserializerRegistry registry) {
		super();
		
		Contract.requireArgNotNull("eventStore", eventStore);
		Contract.requireArgNotNull("serializer", serializer);
		Contract.requireArgNotNull("registry", registry);
		
		this.eventStore = eventStore;
		this.serializer = serializer;
		this.registry = registry;
		noCache = new AggregateNoCache<AGGREGATE>();
	}

	@Override
	public final AGGREGATE read(final ID aggregateId)
			throws AggregateNotFoundException, AggregateDeletedException {

		Contract.requireArgNotNull("aggregateId", aggregateId);

		try {
			AGGREGATE aggregate = getAggregateCache().get(aggregateId);
			if (aggregate == null) {
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
			throws AggregateNotFoundException, AggregateDeletedException,
			AggregateVersionNotFoundException {

		Contract.requireArgNotNull("aggregateId", aggregateId);

		AGGREGATE aggregate = getAggregateCache().get(aggregateId);
		if (aggregate == null) {
			aggregate = create();
		} else if (aggregate.getVersion() > version) {
			aggregate = create();
		} else if (aggregate.getVersion() == version) {
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
	 *            Version of the aggregate to load or {@link Integer#MAX_VALUE}
	 *            to read the latest version.
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
	private AGGREGATE read(final AGGREGATE aggregate, final ID id,
			final int targetVersion) throws AggregateNotFoundException,
			AggregateDeletedException, AggregateVersionNotFoundException {

		requireNoUncommittedChanges(aggregate);

		final AggregateStreamId<ID> streamId = new AggregateStreamId<ID>(
				getAggregateType(), getIdParamName(), id);
		final int readPageSize = getReadPageSize();

		int sliceStart = aggregate.getVersion();
		StreamEventsSlice currentSlice;
		do {
			final int sliceInc;
			if (readPageSize <= targetVersion) {
				sliceInc = readPageSize;
			} else {
				sliceInc = targetVersion - sliceStart + 1;
			}
			final int sliceCount = sliceStart + sliceInc;

			try {
				currentSlice = getEventStore().readStreamEventsForward(
						streamId, sliceStart, sliceCount);
			} catch (final StreamNotFoundException ex) {
				throw new AggregateNotFoundException(getAggregateType(), id);
			} catch (final StreamDeletedException ex) {
				throw new AggregateDeletedException(getAggregateType(), id);
			}

			for (final EventData eventData : currentSlice.getEvents()) {
				final Data data = eventData.getEventData();
				final DomainEvent<?> event = deserialize(data);
				aggregate.loadFromHistory(event);
			}

			sliceStart = currentSlice.getNextEventNumber();

		} while ((targetVersion >= currentSlice.getNextEventNumber())
				&& !currentSlice.isEndOfStream());

		if ((aggregate.getVersion() != targetVersion)
				&& (targetVersion < Integer.MAX_VALUE)) {
			throw new AggregateVersionNotFoundException(getAggregateType(), id,
					targetVersion);
		}

		getAggregateCache().put(aggregate.getId(), aggregate);

		return aggregate;
	}

	private void requireNoUncommittedChanges(final AGGREGATE aggregate) {
		if (aggregate.hasUncommitedChanges()) {
			throw new IllegalArgumentException("The aggregate '"
					+ getAggregateType() + "' (" + aggregate.getId()
					+ ") has uncommitted changes");
		}
	}

	@Override
	public final void update(final AGGREGATE aggregate, final MetaData metaData)
			throws AggregateVersionConflictException,
			AggregateNotFoundException, AggregateDeletedException {

		Contract.requireArgNotNull("aggregate", aggregate);

		final AggregateStreamId<ID> streamId = new AggregateStreamId<ID>(
				getAggregateType(), getIdParamName(), aggregate.getId());

		final List<DomainEvent<?>> events = aggregate.getUncommittedChanges();
		final List<EventData> eventDataList = asEventDataList(events, metaData);

		int retryCount = 0;
		boolean unsaved = true;
		do {

			try {
				final int aggregateNextVersion = aggregate.getNextVersion();
				final long eventStoreNextVersion = getEventStore()
						.appendToStream(streamId, aggregate.getVersion(),
								eventDataList);
				if (aggregateNextVersion != eventStoreNextVersion) {
					throw new IllegalStateException(
							"Next aggregate version is " + aggregateNextVersion
									+ " but event store's next is "
									+ eventStoreNextVersion);
				}
				aggregate.markChangesAsCommitted();
				unsaved = false;
			} catch (final StreamVersionConflictException ex) {
				resolveConflicts(aggregate, ex.getExpected(), ex.getActual(),
						retryCount++);
			}

		} while (unsaved);

	}

	private void resolveConflicts(final AGGREGATE aggregate,
			final int expectedVersion, final int actualVersion,
			final int retryCount) throws AggregateVersionConflictException,
			AggregateNotFoundException, AggregateDeletedException {

		// Check how many times we should try
		if (retryCount == getMaxTryCount()) {
			throw new AggregateVersionConflictException(getAggregateType(),
					aggregate.getId(), expectedVersion, actualVersion);
		}

		// Load unseen events and try to resolve the conflict
		final List<DomainEvent<?>> unseenEvents = readEvents(aggregate.getId(),
				expectedVersion + 1, actualVersion);
		if (!conflictsResolved(aggregate.getUncommittedChanges(), unseenEvents)) {
			throw new AggregateVersionConflictException(getAggregateType(),
					aggregate.getId(), expectedVersion, actualVersion);
		}

	}

	@Override
	public final void delete(final ID aggregateId, final int expectedVersion)
			throws AggregateVersionConflictException {

		Contract.requireArgNotNull("aggregateId", aggregateId);

		try {
			final AggregateStreamId<ID> streamId = new AggregateStreamId<ID>(
					getAggregateType(), getIdParamName(), aggregateId);
			getEventStore().deleteStream(streamId, expectedVersion);
		} catch (final StreamVersionConflictException ex) {
			throw new AggregateVersionConflictException(getAggregateType(),
					aggregateId, ex.getExpected(), ex.getActual());
		}
	}

	/**
	 * Reads a list of events for the given aggregate.
	 * 
	 * @param aggregateId
	 *            Unique identifier of the aggregate to read the events for.
	 * @param startVersion
	 *            First event number to read.
	 * @param targetVersion
	 *            Last event number to read.
	 * 
	 * @return List of events.
	 * 
	 * @throws AggregateNotFoundException
	 *             An aggregate with the given identifier was not found.
	 * @throws AggregateDeletedException
	 *             The aggregate with the given identifier was already deleted.
	 */
	public final List<DomainEvent<?>> readEvents(final ID aggregateId,
			final int startVersion, final int targetVersion)
			throws AggregateNotFoundException, AggregateDeletedException {

		final List<DomainEvent<?>> list = new ArrayList<DomainEvent<?>>();
		final AggregateStreamId<ID> streamId = new AggregateStreamId<ID>(
				getAggregateType(), getIdParamName(), aggregateId);
		final int readPageSize = getReadPageSize();

		int sliceStart = startVersion;
		StreamEventsSlice currentSlice;
		do {
			final int sliceInc;
			if (readPageSize <= targetVersion) {
				sliceInc = readPageSize;
			} else {
				sliceInc = targetVersion - sliceStart + 1;
			}
			final int sliceCount = sliceStart + sliceInc;

			try {
				currentSlice = getEventStore().readStreamEventsForward(
						streamId, sliceStart, sliceCount);
			} catch (final StreamNotFoundException ex) {
				throw new AggregateNotFoundException(getAggregateType(),
						aggregateId);
			} catch (final StreamDeletedException ex) {
				throw new AggregateDeletedException(getAggregateType(),
						aggregateId);
			}

			for (final EventData eventData : currentSlice.getEvents()) {
				final Data data = eventData.getEventData();
				final DomainEvent<?> event = deserialize(data);
				list.add(event);
			}

			sliceStart = currentSlice.getNextEventNumber();

		} while ((targetVersion >= currentSlice.getNextEventNumber())
				&& !currentSlice.isEndOfStream());

		return list;
	}

	private List<EventData> asEventDataList(final List<DomainEvent<?>> events,
			final MetaData metaData) {

		final Data md = serialize(metaData);
		final List<EventData> eventDataList = new ArrayList<EventData>(
				events.size());
		for (final Event event : events) {
			final Data ed = serialize(event);
			eventDataList.add(new EventData(event.getEventId().asString(),
					event.getTimestamp(), ed, md));
		}
		return eventDataList;
	}

	private Data serialize(final Object data) {
		if (data == null) {
			return null;
		}
		final Serializer serializer = getSerializer();
		return new Data(serializer.getType(), serializer.getVersion(),
				serializer.getMimeType(), serializer.getEncoding(),
				serializer.marshal(data));
	}

	private <T> T deserialize(final Data data) {
		final Deserializer deserializer = getDeserializerRegistry()
				.getDeserializer(data.getType(), data.getVersion(),
						data.getMimeType(), data.getEncoding());
		return deserializer.unmarshal(data.getRaw());

	}

	/**
	 * Checks if the uncommitted changes conflicts with unseen changes from the
	 * event store and tries to solve the problem. This method may be
	 * overwritten by concrete implementation. Returns FALSE as default if not
	 * overwritten in subclasses.
	 * 
	 * @param uncommittedChanges
	 *            Uncommitted changes.
	 * @param unseenEvents
	 *            Unseen changes from the event store.
	 * 
	 * @return TRUE if there are no conflicting changes, else FALSE (conflict
	 *         couldn't be resolved).
	 */
	// CHECKSTYLE:OFF:DesignForExtension OK because method has no logic (just a
	// boolean).
	protected boolean conflictsResolved(
			final List<DomainEvent<?>> uncommittedChanges,
			final List<DomainEvent<?>> unseenEvents) {
		// CHECKSTYLE:ON:DesignForExtension
		return false;
	}

	/**
	 * Returns the number of tries that should be done to resolve a version
	 * conflict. This method may be overwritten by concrete implementation.
	 * Returns <code>3</code> as default if not overwritten in subclasses.
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
	 * Returns the aggregate cache. This method may be overwritten by concrete
	 * implementation. Returns no cache as default if not overwritten in
	 * subclasses.
	 * 
	 * @return Cache.
	 */
	// CHECKSTYLE:OFF:DesignForExtension OK because method has no logic (just a
	// simple default impl).
	@NeverNull
	protected AggregateCache<AGGREGATE> getAggregateCache() {
		// CHECKSTYLE:ON:DesignForExtension
		return noCache;
	}

	/**
	 * Returns the number of events to read in a slice. This method may be
	 * overwritten by concrete implementation. Returns <code>100</code> as
	 * default if not overwritten in subclasses.
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
	@NeverNull
	protected final EventStore getEventStore() {
		return eventStore;
	}

	/**
	 * Returns the serializer to use.
	 * 
	 * @return Serializer.
	 */
	@NeverNull
	protected final Serializer getSerializer() {
		return serializer;
	}

	/**
	 * Returns a registry of deserializers.
	 * 
	 * @return Registry with known deserializers.
	 */
	@NeverNull
	protected final DeserializerRegistry getDeserializerRegistry() {
		return registry;
	}

	/**
	 * Returns the parameter name for the unique identifier.
	 * 
	 * @return Name to be used as parameter.
	 */
	@NeverNull
	protected abstract String getIdParamName();

}
