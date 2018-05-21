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
package org.fuin.ddd4j.ddd;

import java.time.ZonedDateTime;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import javax.annotation.Nullable;
import org.fuin.objects4j.ui.Label;
import org.fuin.objects4j.ui.Prompt;
import org.fuin.objects4j.ui.ShortLabel;
import org.fuin.objects4j.ui.Tooltip;

import com.migesok.jaxb.adapter.javatime.ZonedDateTimeXmlAdapter;

/**
 * Base class for events. Equals and hash code are solely based on the event id.
 */
public abstract class AbstractEvent implements Event {

    private static final long serialVersionUID = 1000L;

    @XmlJavaTypeAdapter(EventIdConverter.class)
    @XmlElement(name = "event-id")
    private EventId eventId;

    @Label("Timestamp")
    @ShortLabel("Time")
    @Tooltip("Date/Time the event was created")
    @Prompt("2016-12-31T23:59:59+02:00")
    @XmlJavaTypeAdapter(ZonedDateTimeXmlAdapter.class)
    @XmlElement(name = "event-timestamp")
    private ZonedDateTime timestamp;

    @Label("Correlation Identifier")
    @ShortLabel("CorrID")
    @Tooltip("Event this one correlates to")
    @XmlJavaTypeAdapter(EventIdConverter.class)
    @XmlElement(name = "correlation-id")
    private EventId correlationId;

    @Label("Causation Identifier")
    @ShortLabel("CauseID")
    @Tooltip("Event that caused this one")
    @XmlJavaTypeAdapter(EventIdConverter.class)
    @XmlElement(name = "causation-id")
    private EventId causationId;

    /**
     * Default constructor.
     */
    public AbstractEvent() {
        this(null, null);
    }

    /**
     * Constructor with event this one responds to. Convenience method to set the correlation and causation
     * identifiers correctly.
     * 
     * @param respondTo
     *            Causing event.
     */
    public AbstractEvent(@NotNull final Event respondTo) {
        this(respondTo.getCorrelationId(), respondTo.getEventId());
    }

    /**
     * Constructor with optional data.
     * 
     * @param correlationId
     *            Correlation ID.
     * @param causationId
     *            ID of the event that caused this one.
     */
    public AbstractEvent(@Nullable final EventId correlationId, @Nullable final EventId causationId) {
        super();
        this.eventId = new EventId();
        this.timestamp = ZonedDateTime.now();
        this.correlationId = correlationId;
        this.causationId = causationId;
    }

    @Override
    public final EventId getEventId() {
        return eventId;
    }

    @Override
    public final ZonedDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public final EventId getCorrelationId() {
        return correlationId;
    }

    @Override
    public final EventId getCausationId() {
        return causationId;
    }

    // CHECKSTYLE:OFF Generated code
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((eventId == null) ? 0 : eventId.hashCode());
        return result;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AbstractEvent other = (AbstractEvent) obj;
        if (eventId == null) {
            if (other.eventId != null) {
                return false;
            }
        } else if (!eventId.equals(other.eventId)) {
            return false;
        }
        return true;
    }
    // CHECKSTYLE:ON

}
