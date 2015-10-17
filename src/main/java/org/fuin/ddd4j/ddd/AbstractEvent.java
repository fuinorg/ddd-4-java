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

import javax.xml.bind.annotation.XmlAttribute;

import org.joda.time.DateTime;

/**
 * Base class for events.
 */
public abstract class AbstractEvent implements Event {

    private static final long serialVersionUID = 1000L;

    @XmlAttribute(name = "event-id")
    private final EventId eventId;

    @XmlAttribute(name = "event-timestamp")
    private final DateTime timestamp;

    /**
     * Default constructor.
     */
    public AbstractEvent() {
        super();
        this.eventId = new EventId();
        this.timestamp = new DateTime();
    }

    @Override
    public final EventId getEventId() {
        return eventId;
    }

    @Override
    public final DateTime getTimestamp() {
        return timestamp;
    }

    // CHECKSTYLE:OFF Generated code
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result)
                + ((eventId == null) ? 0 : eventId.hashCode());
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
