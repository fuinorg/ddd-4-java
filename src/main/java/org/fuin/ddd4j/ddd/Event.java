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
package org.fuin.ddd4j.ddd;

import java.io.Serializable;
import java.util.Date;

import org.fuin.objects4j.common.NeverNull;

public interface Event extends Serializable {

	/**
	 * Returns the identifier of the event.
	 * 
	 * @return Unique identifier event.
	 */
	@NeverNull
	public EventId getEventId();

	/**
	 * Returns the type of the event (What happened).
	 * 
	 * @return A text unique for all events of an aggregate.
	 */
	@NeverNull
	public EventType getEventType();

	/**
	 * Date, time and time zone the event was created.
	 * 
	 * @return Event creation date and time.
	 */
	public Date getTimestamp();

}
