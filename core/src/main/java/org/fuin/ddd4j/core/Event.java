/**
 * Copyright (C) 2015 Michael Schnell. All rights reserved.
 * http://www.fuin.org/
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see http://www.gnu.org/licenses/.
 */
package org.fuin.ddd4j.core;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * Something that happened in the system.
 */
public interface Event extends Serializable {

    /**
     * Returns the identifier of the event.
     *
     * @return Unique identifier event.
     */
    @NotNull
    EventId getEventId();

    /**
     * Returns the type of the event (What happened).
     *
     * @return A text unique for all events of an aggregate.
     */
    @NotNull
    EventType getEventType();

    /**
     * Date, time and time zone the event was created.
     *
     * @return Event creation date and time.
     */
    @NotNull
    ZonedDateTime getEventTimestamp();

    /**
     * Correlation identifier.
     *
     * @return Context of the event.
     */
    @Nullable
    EventId getCorrelationId();

    /**
     * Causation identifier.
     *
     * @return Identifier of the evtn that caused this one.
     */
    @Nullable
    EventId getCausationId();

}
