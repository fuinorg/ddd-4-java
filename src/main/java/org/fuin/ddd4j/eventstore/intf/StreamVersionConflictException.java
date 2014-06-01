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

import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.common.NeverNull;
import org.fuin.objects4j.common.UniquelyNumberedException;

/**
 * Signals a conflict between an expected and an actual version.
 */
public final class StreamVersionConflictException extends
	UniquelyNumberedException {

    private static final long serialVersionUID = 1000L;

    private final StreamId streamId;

    private final int expected;

    private final int actual;

    /**
     * Constructor with all data.
     * 
     * @param streamId
     *            Unique name of the stream.
     * @param expected
     *            Expected version.
     * @param actual
     *            Actual version.
     */
    public StreamVersionConflictException(final StreamId streamId,
	    final int expected, final int actual) {
	super(109, "Expected version " + expected + " for stream '" + streamId
		+ "', but was " + actual);
	Contract.requireArgNotNull("streamId", streamId);
	this.streamId = streamId;
	this.expected = expected;
	this.actual = actual;
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

    /**
     * Returns the expected version.
     * 
     * @return Expected version.
     */
    public final int getExpected() {
	return expected;
    }

    /**
     * Returns the actual version.
     * 
     * @return Actual version.
     */
    public final int getActual() {
	return actual;
    }

}
