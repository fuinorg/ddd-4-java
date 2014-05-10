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
 * Signals that an event with the given number was not found.
 */
public class EventNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    private final StreamId streamId;

    private final int version;

    /**
     * Constructor with all data.
     * 
     * @param streamId
     *            Unique identifier of the stream.
     * @param eventNumber
     *            Number of the event that was not found.
     */
    public EventNotFoundException(@NotNull final StreamId streamId,
	    final int eventNumber) {
	super("Version " + eventNumber + " does not exist on stream '"
		+ streamId + "'");

	Contract.requireArgNotNull("streamId", streamId);

	this.streamId = streamId;
	this.version = eventNumber;
    }

    /**
     * Returns the unique ID of the stream.
     * 
     * @return Stream with version that was not found.
     */
    @NeverNull
    public final StreamId getStreamId() {
	return streamId;
    }

    /**
     * Returns the number of the event.
     * 
     * @return Number that was not found.
     */
    public final int getVersion() {
	return version;
    }

}
