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

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.fuin.objects4j.common.NeverNull;

/**
 * Identifies a stream event.
 */
public class StreamEventId implements Serializable {

	private static final long serialVersionUID = 1000L;

	private String streamName;

	private Integer eventNumber;

	/**
	 * Default constructor for JPA. <b><i>CAUTION:</i> DO NOT USE IN APPLICATION CODE.</b>
	 */
	public StreamEventId() {
		super();
	}
	
	/**
	 * Constructor with all required data.
	 * 
	 * @param streamName
	 *            Name of the stream.
	 * @param eventNumber
	 *            Number of the event within the stream.
	 */
	public StreamEventId(@NotNull final String streamName,
			@NotNull final Integer eventNumber) {
		super();
		this.streamName = streamName;
		this.eventNumber = eventNumber;
	}

	/**
	 * Returns the name of the stream.
	 * 
	 * @return Unique stream name.
	 */
	@NeverNull
	public String getStreamName() {
		return streamName;
	}

	/**
	 * Returns the numner of the event within the stream.
	 * 
	 * @return Order of the event in the stream.
	 */
	@NeverNull
	public Integer getEventNumber() {
		return eventNumber;
	}

	@Override
	public String toString() {
		return streamName + "-" + eventNumber;
	}

	// CHECKSTYLE:OFF Generated code
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((streamName == null) ? 0 : streamName.hashCode());
		result = prime * result + ((eventNumber == null) ? 0 : eventNumber.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StreamEventId other = (StreamEventId) obj;
		if (streamName == null) {
			if (other.streamName != null)
				return false;
		} else if (!streamName.equals(other.streamName))
			return false;
		if (eventNumber == null) {
			if (other.eventNumber != null)
				return false;
		} else if (!eventNumber.equals(other.eventNumber))
			return false;
		return true;
	}
	// CHECKSTYLE:ON

}
