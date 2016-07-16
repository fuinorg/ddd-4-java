/**
 * Copyright (C) 2015 Michael Schnell. All rights reserved. 
 * http://www.fuin.org/
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
 * along with this library. If not, see http://www.gnu.org/licenses/.
 */
package org.fuin.ddd4j.common;

import org.fuin.ddd4j.ddd.AggregateDeletedException;
import org.fuin.ddd4j.ddd.AggregateNotFoundException;
import org.fuin.ddd4j.ddd.AggregateVersionConflictException;
import org.fuin.ddd4j.ddd.AggregateVersionNotFoundException;

/**
 * Utility methods and constants.
 */
public final class CommonUtils {

    /** Prefix for unique short identifiers. */
    public static final String SHORT_ID_PREFIX = "DDD4J";

    /** Classes used for JAX-B serialization. */
    public static final Class<?>[] JAXB_CLASSES = new Class<?>[] { 
            AggregateDeletedException.class,
            AggregateNotFoundException.class,
            AggregateVersionConflictException.class,
            AggregateVersionNotFoundException.class
    };

    /**
     * Private by intention.
     */
    private CommonUtils() {
        throw new UnsupportedOperationException();
    }

}
