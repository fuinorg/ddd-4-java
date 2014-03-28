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
package org.fuin.ddd4j.eventstore.intf;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.common.Immutable;
import org.fuin.objects4j.common.NeverNull;

/**
 * Event.
 */
@Immutable
public final class EventData {

	private final String eventId;

	private final Date timestamp;

	private final Data eventData;

	private final Data metaData;

	/**
	 * Constructor with all data.
	 * 
	 * @param eventId
	 *            The ID of the event, used as part of the idempotent write
	 *            check.
	 * @param timestamp
	 *            Date, time an time zone the event was created.
	 * @param eventData
	 *            Event data.
	 * @param metaData
	 *            Meta data.
	 */
	public EventData(@NotNull final String eventId,
			@NotNull final Date timestamp, final Data eventData,
			final Data metaData) {
		super();

		Contract.requireArgNotNull("eventId", eventId);
		Contract.requireArgNotNull("timestamp", timestamp);
		Contract.requireArgNotNull("eventData", eventData);

		this.eventId = eventId;
		this.timestamp = timestamp;
		this.eventData = eventData;
		this.metaData = metaData;
	}

	/**
	 * Returns the ID of the event, used as part of the idempotent write check.
	 * 
	 * @return Unique event identifier.
	 */
	@NeverNull
	public final String getEventId() {
		return eventId;
	}

	/**
	 * Returns the date, time and time zone the event was created.
	 * 
	 * @return Timestamp.
	 */
	@NeverNull
	public final Date getTimestamp() {
		return timestamp;
	}

	/**
	 * Returns the event data.
	 * 
	 * @return Event data.
	 */
	public final Data getEventData() {
		return eventData;
	}

	/**
	 * Returns the meta data.
	 * 
	 * @return Meta data.
	 */
	public final Data getMetaData() {
		return metaData;
	}

}
