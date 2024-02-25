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
 * An aggregate already exists when trying to create it.
 */
public final class AggregateAlreadyExistsException extends AbstractVersionedAggregateException implements ExceptionShortIdentifable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Unique short identifier of this exception.
     */
    public static final String SHORT_ID = SHORT_ID_PREFIX + "-AGGREGATE_ALREADY_EXISTS";

    /**
     * Constructor with all data.
     *
     * @param type    Type of the aggregate.
     * @param id      Unique identifier of the aggregate.
     * @param version Actual version.
     */
    public AggregateAlreadyExistsException(@NotNull final EntityType type, @NotNull final AggregateRootId id, final int version) {
        super(type.asString() + " " + id.asString() + " already exists (version=" + version + ")", type, id, version);
    }

    @Override
    public String getShortId() {
        return SHORT_ID;
    }
}
