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

import javax.validation.constraints.NotNull;

import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.common.NeverNull;

/**
 * Signals that a stream with that name previously existed but was deleted.
 */
public class StreamDeletedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final StreamId streamId;

	/**
	 * Constructor with all data.
	 * 
	 * @param streamId
	 *            Unique name of the stream.
	 */
	public StreamDeletedException(@NotNull final StreamId streamId) {
		super("Stream '" + streamId + "' previously existed but was deleted");
		Contract.requireArgNotNull("streamId", streamId);
		this.streamId = streamId;
	}

	/**
	 * Returns the unique identifier of the stream.
	 * 
	 * @return Stream that was not found.
	 */
	@NeverNull
	public final StreamId getStreamId() {
		return streamId;
	}

}
