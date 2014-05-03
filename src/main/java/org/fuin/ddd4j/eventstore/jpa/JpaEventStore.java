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
package org.fuin.ddd4j.eventstore.jpa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import org.fuin.ddd4j.eventstore.intf.EventData;
import org.fuin.ddd4j.eventstore.intf.EventNotFoundException;
import org.fuin.ddd4j.eventstore.intf.EventStore;
import org.fuin.ddd4j.eventstore.intf.StreamDeletedException;
import org.fuin.ddd4j.eventstore.intf.StreamEventsSlice;
import org.fuin.ddd4j.eventstore.intf.StreamId;
import org.fuin.ddd4j.eventstore.intf.StreamNotFoundException;
import org.fuin.ddd4j.eventstore.intf.StreamVersionConflictException;

/**
 * JPA Implementation of the event store.
 */
public final class JpaEventStore implements EventStore {

	private EntityManager em;

	/**
	 * Constructor with all manadtory data.
	 * 
	 * @param em
	 *            Entity manager.
	 */
	public JpaEventStore(@NotNull final EntityManager em) {
		super();
		this.em = em;
	}

	@Override
	public void open() {
		// Do nothing
	}

	@Override
	public void close() {
		// Do nothing
	}

	@Override
	public final int appendToStream(final StreamId streamId,
			final int expectedVersion, final EventData... events)
			throws StreamVersionConflictException {
		return appendToStream(streamId, expectedVersion, Arrays.asList(events));
	}

	@Override
	public final int appendToStream(final StreamId streamId,
			final int expectedVersion, final List<EventData> events)
			throws StreamVersionConflictException {

		final String sql = "SELECT t FROM Stream t WHERE t.name=:name";
		final TypedQuery<Stream> query = em.createQuery(sql, Stream.class);
		query.setParameter("name", streamId.asString());
		query.setLockMode(LockModeType.PESSIMISTIC_WRITE);

		final List<Stream> streams = query.getResultList();
		final Stream stream;
		if (streams.size() == 0) {
			stream = new Stream(streamId.asString());
			em.persist(stream);
		} else {
			stream = streams.get(0);
			if (stream.isDeleted()) {
				throw new StreamDeletedException(streamId);
			}
			if (stream.getVersion() != expectedVersion) {
				throw new StreamVersionConflictException(streamId,
						expectedVersion, stream.getVersion());
			}
		}
		for (int i = 0; i < events.size(); i++) {
			final EventEntry eventEntry = asEventEntry(events.get(i));
			em.persist(eventEntry);
			final StreamEvent streamEvent = stream.createEvent(eventEntry);
			em.persist(streamEvent);
		}
		return stream.getVersion();

	}

	@Override
	public final EventData readEvent(final StreamId streamId,
			final int eventNumber) throws EventNotFoundException {

		final TypedQuery<StreamEvent> query = em.createQuery(
				"SELECT t FROM StreamEvent t WHERE t.streamName=:streamName "
						+ "AND t.eventNumber=:eventNumber", StreamEvent.class);
		query.setParameter("streamName", streamId.asString());
		query.setParameter("eventNumber", eventNumber);

		try {
			final StreamEvent result = query.getSingleResult();
			return asEventData(result.getEventEntry());
		} catch (final NoResultException ex) {
			throw new EventNotFoundException(streamId, eventNumber);
		}

	}

	@Override
	public final StreamEventsSlice readStreamEventsForward(
			final StreamId streamId, final int start, final int count) {

		return readStreamEvents(streamId, start, count, true);

	}

	@Override
	public final StreamEventsSlice readStreamEventsBackward(
			final StreamId streamId, final int start, final int count) {

		return readStreamEvents(streamId, start, count, false);

	}

	private StreamEventsSlice readStreamEvents(final StreamId streamId,
			final int start, final int count, final boolean forward) {

		// Prepare SQL
		final String sql = "SELECT t FROM StreamEvent t WHERE t.streamName=:streamName"
				+ " ORDER BY t.eventNumber " + sortOrder(forward);
		final TypedQuery<StreamEvent> query = em.createQuery(sql,
				StreamEvent.class);
		query.setParameter("streamName", streamId.asString());
		query.setFirstResult(start - 1);
		query.setMaxResults(count);

		// Execute query
		final List<StreamEvent> resultList = query.getResultList();

		// Return result
		final List<EventData> events = asEventData(resultList);
		final int fromEventNumber = start;
		final int nextEventNumber = (start + events.size());
		final boolean endOfStream = (events.size() < count);

		return new StreamEventsSlice(fromEventNumber, events, nextEventNumber,
				endOfStream);
	}

	private String sortOrder(final boolean asc) {
		if (asc) {
			return "ASC";
		}
		return "DESC";
	}

	@Override
	public final void deleteStream(final StreamId streamId,
			final int expectedVersion) throws StreamVersionConflictException {

		final Stream stream = em.find(Stream.class, streamId.getName(),
				LockModeType.PESSIMISTIC_WRITE);
		if (stream == null) {
			throw new StreamNotFoundException(streamId);
		}
		if (stream.isDeleted()) {
			throw new StreamDeletedException(streamId);
		}
		if (stream.getVersion() != expectedVersion) {
			throw new StreamVersionConflictException(streamId, expectedVersion,
					stream.getVersion());
		}
		stream.delete();

	}

	private List<EventData> asEventData(final List<StreamEvent> resultList) {
		final List<EventData> events = new ArrayList<EventData>();
		for (StreamEvent result : resultList) {
			final EventEntry eventEntry = result.getEventEntry();
			events.add(asEventData(eventEntry));
		}
		return events;
	}

	private EventData asEventData(final EventEntry eventEntry) {
		return new EventData(eventEntry.getId(), eventEntry.getTimestamp(),
				eventEntry.getData(), eventEntry.getMeta());
	}

	private EventEntry asEventEntry(final EventData eventData) {
		return new EventEntry(eventData.getEventId(), eventData.getTimestamp(),
				eventData.getEventData(), eventData.getMetaData());
	}

}
