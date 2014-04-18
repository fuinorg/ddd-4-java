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

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.common.NeverNull;

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

	@NotNull
	@Column(name = "EVENT_NO", nullable = false)
	private int eventNumber;

	
	/**
	 * Protected default constructor only required for JPA.
	 */
	protected StreamEvent() {
		super();
	}

	/**
	 * Constructs a stream event.
	 * 
	 * @param eventNumber
	 *            Number of the event.
	 * @param eventEntry
	 *            Event entry with the actual event data.
	 */
	public StreamEvent(final int eventNumber, @NotNull final EventEntry eventEntry) {
		super();
		
		Contract.requireArgNotNull("eventEntry", eventEntry);
		
		this.eventEntry = eventEntry;
		this.eventNumber = eventNumber;
	}

	/**
	 * Returns the event entry contained in this JPA entity.
	 * 
	 * @return Event entry.
	 */
	@NeverNull
	public final EventEntry getEventEntry() {
		return eventEntry;
	}

	/**
	 * Returns the number of the event.
	 * 
	 * @return Event number.
	 */
	public final int getEventNumber() {
		return eventNumber;
	}
	
}
