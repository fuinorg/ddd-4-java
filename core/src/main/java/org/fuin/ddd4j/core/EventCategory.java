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

import org.fuin.objects4j.common.ThreadSafe;

/**
 * Base marker for interfaces that <b>categorize</b> a domain event for projection selection.
 * A view can subscribe to a category - any interface extending this marker - instead of
 * enumerating every concrete event type, so it automatically picks up new event types
 * that belong to the category.
 * <p>
 * The framework records the simple names of the {@link EventCategory} interfaces an
 * event implements into the event's metadata (see {@link Ddd4JUtils#eventCategories(Object)});
 * the event store's projection engine then selects an event by category by matching those names.
 * The predefined lifecycle categories are {@link GenesisEvent} (creation), {@link ExileEvent}
 * (soft delete), {@link ReturnFromExileEvent} (the recall that undoes a soft delete) and
 * {@link ExodusEvent} (hard delete); applications may add their own by declaring further
 * interfaces that extend this marker.
 * <p>
 * All implementations are expected to be thread safe.
 */
@ThreadSafe
public interface EventCategory {

}
