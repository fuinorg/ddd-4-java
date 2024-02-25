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

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

/**
 * Domain event published by an entity.
 *
 * @param <ID>
 *            Type of the identifier.
 */
public interface DomainEvent<ID extends EntityId> extends Event {

    /**
     * Returns the path to the originator of the event.
     *
     * @return List of unique identifiers from aggregate root to the entity that emitted the event.
     */
    @NotNull
    EntityIdPath getEntityIdPath();

    /**
     * Returns the identifier of the entity that caused this event. This is the last ID in the path.
     *
     * @return Entity identifier.
     */
    @NotNull
    ID getEntityId();

    /**
     * Returns the version of the aggregate the entity belongs to.
     *
     * @return Aggregate version at the time the event was raised.
     */
    @Nullable
    AggregateVersion getAggregateVersion();

    /**
     * Returns the aggregate version as integer. This is a null-safe shortcut for <code>getAggregateVersion().asBaseType()</code>-
     *
     * @return Expected version or {@literal null}.
     */
    @Nullable
    Integer getAggregateVersionInteger();

}
