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
 * Category marker (see {@link EventCategory}) for a domain event that <b>recalls an entity from exile</b>:
 * the entity that an {@link ExileEvent} hid or deactivated is active again (for example
 * {@code TenantResumedEvent}). It is the counterpart of {@link ExileEvent}, and only that: an
 * {@link ExodusEvent} removes an entity for good, so nothing ever returns from one.
 * <p>
 * A view can select {@code ReturnFromExileEvent} as a category to react to every such "unhide /
 * reactivate" event across aggregates - which is what a read model dropping a row on exile needs in
 * order to put it back.
 * <p>
 * All implementations are expected to be thread safe.
 */
@ThreadSafe
public interface ReturnFromExileEvent extends EventCategory {

}
