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

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

/**
 * An event in a specific event stream. Connects the stream with the event
 * entries.
 */
@MappedSuperclass
public abstract class StreamEvent {

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "EVENTS_ID", unique = true, nullable = false, updatable = false)
	private EventEntry eventEntry;

	/**
	 * Protected default constructor only required for JPA.
	 */
	protected StreamEvent() {
		super();
	}

	/**
	 * Constructs a stream event.
	 * 
	 * @param eventEntry
	 *            Event entry with the actual event data.
	 */
	public StreamEvent(final EventEntry eventEntry) {
		super();
		this.eventEntry = eventEntry;
	}

	/**
	 * Returns the event entry contained in this JPA entity.
	 * 
	 * @return Event entry.
	 */
	public final EventEntry getEventEntry() {
		return eventEntry;
	}

}
