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

import jakarta.validation.constraints.NotNull;
import org.fuin.objects4j.common.ExceptionShortIdentifable;

import java.io.Serial;

import static org.fuin.ddd4j.core.Ddd4JUtils.SHORT_ID_PREFIX;

/**
 * Signals that an aggregate of a given type and identifier was deleted from the repository.
 */
public final class AggregateDeletedException extends AbstractAggregateException implements ExceptionShortIdentifable {

    /**
     * Unique name of the element to use for XML and JSON marshalling/unmarshalling.
     */
    public static final String ELEMENT_NAME = "aggregate-deleted-exception";

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Unique short identifier of this exception.
     */
    public static final String SHORT_ID = SHORT_ID_PREFIX + "-AGGREGATE_DELETED";

    /**
     * Constructor with all data.
     *
     * @param type Type of the aggregate.
     * @param id   Unique identifier of the aggregate.
     */
    public AggregateDeletedException(@NotNull final EntityType type, @NotNull final AggregateRootId id) {
        super(type.asString() + " with id " + id.asString() + " already deleted", type, id);
    }

    /**
     * Constructor with string data.
     *
     * @param type Type of the aggregate.
     * @param id   Unique identifier of the aggregate.
     */
    public AggregateDeletedException(@NotNull final String type, @NotNull final String id) {
        super(type + " with id " + id + " already deleted", type, id);
    }

    @Override
    public final String getShortId() {
        return SHORT_ID;
    }

}
