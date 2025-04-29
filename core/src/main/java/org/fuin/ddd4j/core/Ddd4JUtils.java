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

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.zip.Adler32;

/**
 * Utility methods and constants.
 */
public final class Ddd4JUtils {

    /**
     * Prefix for unique short identifiers.
     */
    public static final String SHORT_ID_PREFIX = "DDD4J";

    /**
     * Private by intention.
     */
    private Ddd4JUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates an Adler32 checksum based on event type names.
     *
     * @param eventTypes
     *            Types to calculate a checksum for.
     *
     * @return Checksum based on all names.
     */
    public static long calculateChecksum(final Collection<EventType> eventTypes) {
        final Adler32 checksum = new Adler32();
        for (final EventType eventType : eventTypes) {
            checksum.update(eventType.asBaseType().getBytes(StandardCharsets.US_ASCII));
        }
        return checksum.getValue();
    }

}
