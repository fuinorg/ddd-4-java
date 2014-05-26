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
package org.fuin.ddd4j.eventstore.intf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.fuin.objects4j.common.Immutable;
import org.fuin.objects4j.common.NeverNull;

/**
 *
 */
@Immutable
public final class StreamEventsSlice {

    private final int fromEventNumber;

    private final int nextEventNumber;

    private final boolean endOfStream;

    private final List<EventData> events;

    /**
     * Constructor with all data.
     * 
     * @param fromEventNumber
     *            The starting point (represented as a sequence number) of the
     *            read.
     * @param events
     *            The events read. The list is internally copied to avoid
     *            external dependencies.
     * @param nextEventNumber
     *            The next event number that can be read.
     * @param endOfStream
     *            Determines whether or not this is the end of the stream.
     */
    public StreamEventsSlice(final int fromEventNumber,
            final List<EventData> events, final int nextEventNumber,
            final boolean endOfStream) {

        this.fromEventNumber = fromEventNumber;
        if (events == null || events.size() == 0) {
            this.events = new ArrayList<EventData>();
        } else {
            this.events = new ArrayList<EventData>(events);
        }
        this.nextEventNumber = nextEventNumber;
        this.endOfStream = endOfStream;
    }

    /**
     * Returns the starting point (represented as a sequence number) of the read
     * operation.
     * 
     * @return Event number.
     */
    public int getFromEventNumber() {
        return fromEventNumber;
    }

    /**
     * Returns the events read.
     * 
     * @return Unmodifiable list of events.
     */
    @NeverNull
    public List<EventData> getEvents() {
        return Collections.unmodifiableList(events);
    }

    /**
     * Returns the next event number that can be read.
     * 
     * @return Next event number.
     */
    public int getNextEventNumber() {
        return nextEventNumber;
    }

    /**
     * Returns a boolean representing whether or not this is the end of the
     * stream.
     * 
     * @return TRUE if this is the end of the stream, else FALSE.
     */
    public boolean isEndOfStream() {
        return endOfStream;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("fromEventNumber", fromEventNumber)
                .append("nextEventNumber", nextEventNumber)
                .append("endOfStream", endOfStream)
                .append("events.size", events.size()).toString();
    }

}
