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
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.common.NeverNull;

/**
 * Connects the stream with the event entries.
 */
@Table(name = "STREAM_EVENTS")
@Entity
@IdClass(StreamEventId.class)
public class StreamEvent {

	@Id
	@NotNull
	@Column(name = "STREAM_NAME", nullable = false, updatable = false, length = 250)
	private String streamName;

	@Id
	@NotNull
	@Column(name = "EVENT_NUMBER", nullable = false, updatable = false)
	private Integer eventNumber;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "EVENTS_ID", nullable = false, updatable = false)
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
	 * @param streamName
	 *            Stream the event belongs to.
	 * @param eventNumber
	 *            Number of the event in the stream.
	 * @param eventEntry
	 *            Event entry with the actual event data.
	 */
	public StreamEvent(@NotNull final String streamName,
			@NotNull final Integer eventNumber,
			@NotNull final EventEntry eventEntry) {
		super();

		Contract.requireArgNotNull("streamName", streamName);
		Contract.requireArgNotNull("eventNumber", eventNumber);
		Contract.requireArgNotNull("eventEntry", eventEntry);

		this.eventEntry = eventEntry;
		this.streamName = streamName;
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
	 * Returns the stream the event belongs to.
	 * 
	 * @return Stream name.
	 */
	@NeverNull
	public final String getStreamName() {
		return streamName;
	}

	/**
	 * Returns the number of the event.
	 * 
	 * @return Event number.
	 */
	@NeverNull
	public final Integer getEventNumber() {
		return eventNumber;
	}

}
