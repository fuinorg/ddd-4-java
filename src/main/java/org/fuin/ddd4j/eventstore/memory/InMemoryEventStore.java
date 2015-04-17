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
package org.fuin.ddd4j.eventstore.memory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fuin.ddd4j.eventstore.intf.EventData;
import org.fuin.ddd4j.eventstore.intf.EventNotFoundException;
import org.fuin.ddd4j.eventstore.intf.EventStore;
import org.fuin.ddd4j.eventstore.intf.ProjectionNotWritableException;
import org.fuin.ddd4j.eventstore.intf.StreamDeletedException;
import org.fuin.ddd4j.eventstore.intf.StreamEventsSlice;
import org.fuin.ddd4j.eventstore.intf.StreamId;
import org.fuin.ddd4j.eventstore.intf.StreamNotFoundException;
import org.fuin.ddd4j.eventstore.intf.StreamVersionConflictException;
import org.fuin.objects4j.common.Contract;

/**
 * In-memory event store for testing purposes.
 */
public final class InMemoryEventStore implements EventStore {

    private Map<String, List<EventData>> streams;

    private List<String> deleted;

    /**
     * Default constructor.
     */
    public InMemoryEventStore() {
        super();
        this.streams = new HashMap<String, List<EventData>>();
        this.deleted = new ArrayList<String>();
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
    public final EventData readEvent(final StreamId streamId,
            final int eventNumber) throws EventNotFoundException,
            StreamNotFoundException, StreamDeletedException {

        Contract.requireArgNotNull("streamId", streamId);
        Contract.requireArgMin("eventNumber", eventNumber, 1);
        requireStreamNotDeleted(streamId);

        final List<EventData> events = readEvents(streamId, false);
        if (eventNumber > events.size()) {
            throw new EventNotFoundException(streamId, eventNumber);
        }
        return events.get(eventNumber - 1);

    }

    @Override
    public final StreamEventsSlice readStreamEventsForward(
            final StreamId streamId, final int start, final int count)
            throws StreamNotFoundException, StreamDeletedException {

        Contract.requireArgNotNull("streamId", streamId);
        Contract.requireArgMin("start", start, 1);
        Contract.requireArgMin("count", count, 1);
        requireStreamNotDeleted(streamId);

        final List<EventData> all = readEvents(streamId, false);

        final List<EventData> events = new ArrayList<EventData>();
        final int from = start - 1;
        final int to = from + count;
        for (int i = from; ((i < to) && (i < all.size())); i++) {
            events.add(all.get(i));
        }

        final int fromEventNumber = start;
        final int nextEventNumber = (start + events.size());
        final boolean endOfStream = (events.size() < count);

        return new StreamEventsSlice(fromEventNumber, events, nextEventNumber,
                endOfStream);

    }

    @Override
    public final StreamEventsSlice readStreamEventsBackward(
            final StreamId streamId, final int start, final int count)
            throws StreamNotFoundException, StreamDeletedException {

        Contract.requireArgNotNull("streamId", streamId);
        Contract.requireArgMin("start", start, 1);
        Contract.requireArgMin("count", count, 1);
        requireStreamNotDeleted(streamId);

        final List<EventData> all = readEvents(streamId, false);

        final List<EventData> events = new ArrayList<EventData>();
        final int from = start - 1;
        final int to = from - count;
        for (int i = from; ((i > to) && (i > 0)); i--) {
            events.add(all.get(i));
        }

        final int fromEventNumber = start;
        final int nextEventNumber = (start - events.size());
        final boolean endOfStream = (events.size() < count);

        return new StreamEventsSlice(fromEventNumber, events, nextEventNumber,
                endOfStream);

    }

    @Override
    public final void deleteStream(final StreamId streamId,
            final int expectedVersion) throws StreamNotFoundException,
            StreamVersionConflictException, StreamDeletedException {

        Contract.requireArgNotNull("streamId", streamId);
        Contract.requireArgMin("expectedVersion", expectedVersion, 1);
        requireStreamNotDeleted(streamId);
        readEvents(streamId, expectedVersion, false);

        deleted.add(streamId.asString());
        streams.remove(streamId.asString());

    }

    @Override
    public final int appendToStream(final StreamId streamId,
            final int expectedVersion, final List<EventData> events)
            throws StreamNotFoundException, StreamVersionConflictException,
            StreamDeletedException {

        Contract.requireArgNotNull("streamId", streamId);
        Contract.requireArgMin("expectedVersion", expectedVersion, 0);
        Contract.requireArgNotNull("events", events);
        requireStreamNotDeleted(streamId);
        requireStreamWriteable(streamId);

        final List<EventData> all = readEvents(streamId, expectedVersion, true);
        for (final EventData event : events) {
            all.add(event);
        }

        return all.size();
    }

    @Override
    public final int appendToStream(final StreamId streamId,
            final int expectedVersion, final EventData... events)
            throws StreamNotFoundException, StreamVersionConflictException,
            StreamDeletedException {

        Contract.requireArgNotNull("events", events);

        return appendToStream(streamId, expectedVersion, Arrays.asList(events));

    }

    private List<EventData> readEvents(final StreamId streamId, final boolean create)
            throws StreamNotFoundException {
        List<EventData> events = streams.get(streamId.asString());
        if (events == null) {
            if (create) {
                events = new ArrayList<EventData>();
                streams.put(streamId.asString(), events);
            } else {
                throw new StreamNotFoundException(streamId);
            }
        }
        return events;
    }

    private List<EventData> readEvents(final StreamId streamId,
            final int expectedVersion, final boolean create) throws StreamVersionConflictException,
            StreamNotFoundException {
        final List<EventData> events = readEvents(streamId, create);
        if (expectedVersion > events.size()) {
            throw new StreamVersionConflictException(streamId, expectedVersion,
                    events.size());
        }
        return events;
    }

    private void requireStreamNotDeleted(final StreamId streamId)
            throws StreamDeletedException {
        if (deleted.contains(streamId.asString())) {
            throw new StreamDeletedException(streamId);
        }
    }

    private void requireStreamWriteable(final StreamId streamId) {
        if (streamId.isProjection()) {
            throw new ProjectionNotWritableException(streamId);
        }
    }

}
