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

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.fuin.ddd4j.eventstore.intf.Data;
import org.fuin.objects4j.common.DateTimeAdapter;
import org.fuin.objects4j.common.NeverNull;
import org.joda.time.DateTime;

/**
 * Stores an event and it's meta data.
 */
@Table(name = "EVENTS")
@Entity
@SequenceGenerator(name = "EventEntrySequenceGenerator", sequenceName = "EVENTS_SEQ", allocationSize = 1000)
public class EventEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EventEntrySequenceGenerator")
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "EVENT_ID", length = 100, nullable = false, columnDefinition = "VARCHAR(100)")
    private String eventId;

    /** Date, time and zone the event was created. */
    @Convert(converter = DateTimeAdapter.class)
    @Column(name = "TIMESTAMP", nullable = false)
    private DateTime timestamp;

    @Embedded
    @NotNull
    private Data data;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "type", column = @Column(name = "META_TYPE")),
            @AttributeOverride(name = "version", column = @Column(name = "META_VERSION")),
            @AttributeOverride(name = "mimeType", column = @Column(name = "META_MIME_TYPE")),
            @AttributeOverride(name = "encoding", column = @Column(name = "META_ENCODING")),
            @AttributeOverride(name = "raw", column = @Column(name = "META_RAW")) })
    private Data meta;

    /**
     * Protected default constructor only required for JPA.
     */
    protected EventEntry() {
        super();
    }

    /**
     * Constructor without meta data.
     * 
     * @param eventId
     *            Unique identifier of the event. Generated on the client and
     *            used to achieve idempotence when trying to append the same
     *            event multiple times.
     * @param timestamp
     *            Date, time and zone the event was created.
     * @param data
     *            Data of the event.
     */
    public EventEntry(@NotNull final String eventId,
            @NotNull final DateTime timestamp, @NotNull final Data data) {
        this(eventId, timestamp, data, null);
    }

    /**
     * Constructor with all data.
     * 
     * @param eventId
     *            Unique identifier of the event. Generated on the client and
     *            used to achieve idempotence when trying to append the same
     *            event multiple times.
     * @param timestamp
     *            Date, time and zone the event was created.
     * @param data
     *            Data of the event.
     * @param meta
     *            Meta data (Optional).
     */
    public EventEntry(@NotNull final String eventId,
            @NotNull final DateTime timestamp, @NotNull final Data data,
            final Data meta) {
        super();
        this.eventId = eventId;
        this.timestamp = timestamp;
        this.data = data;
        this.meta = meta;
    }

    /**
     * Returns the unique identifier of the entry.
     * 
     * @return Unique entry ID.
     */
    public final Long getId() {
        return id;
    }

    /**
     * Returns the unique identifier of the event. Generated on the client and
     * used to achieve idempotence when trying to append the same event multiple
     * times.
     * 
     * @return Unique event ID.
     */
    @NeverNull
    public final String getEventId() {
        return eventId;
    }

    /**
     * Returns the time when the event was created.
     * 
     * @return Date, time and zone of event's creation.
     */
    @NeverNull
    public final DateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Returns the data of the event.
     * 
     * @return The event.
     */
    @NeverNull
    public final Data getData() {
        return data;
    }

    /**
     * Returns the meta data of the event (Optional).
     * 
     * @return The event's meta data or NULL.
     */
    public final Data getMeta() {
        return meta;
    }

}
