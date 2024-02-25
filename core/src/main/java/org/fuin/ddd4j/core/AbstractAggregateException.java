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

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.fuin.objects4j.common.Contract;

import java.io.Serial;

/**
 * An aggregate already exists when trying to create it.
 */
public abstract class AbstractAggregateException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    private final EntityType type;

    private final AggregateRootId id;

    /**
     * Constructor with all data.
     *
     * @param message Error message.
     * @param type    Type of the aggregate.
     * @param id      Unique identifier of the aggregate.
     */
    public AbstractAggregateException(@NotEmpty final String message,
                                      @NotNull final EntityType type,
                                      @NotNull final AggregateRootId id) {
        super(message);
        Contract.requireArgNotEmpty("message", message);
        Contract.requireArgNotNull("aggregateType", type);
        Contract.requireArgNotNull("aggregateId", id);
        this.type = type;
        this.id = id;
    }

    /**
     * Returns the type of the aggregate.
     *
     * @return Type.
     */
    @NotNull
    public final EntityType getType() {
        return type;
    }

    /**
     * Returns the unique identifier of the aggregate.
     *
     * @return Stream with version conflict.
     */
    @NotNull
    public final AggregateRootId getId() {
        return id;
    }

}
