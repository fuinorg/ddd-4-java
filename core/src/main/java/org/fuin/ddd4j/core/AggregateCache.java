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

/**
 * Cache for aggregates of the same type.
 *
 * @param <AGGREGATE>
 *            Type of the aggregate.
 */
public interface AggregateCache<AGGREGATE> {

    /**
     * Tries to read the aggregate with the given identifier from the cache.
     *
     * @param aggregateId
     *            Aggregate to load.
     * @param version
     *            Version to load or <code>null</code> for latest.
     *
     * @return Cached aggregate or <code>null</code> if it was not found in the cache.
     */
    AGGREGATE get(@NotNull AggregateRootId aggregateId, Integer version);

    /**
     * Puts an aggregate with the given identifier in the cache.
     *
     * @param aggregateId
     *            Aggregate to load.
     * @param aggregate
     *            Aggregate to cache.
     */
    void put(@NotNull AggregateRootId aggregateId, @NotNull AGGREGATE aggregate);

    /**
     * Removes the aggregate with the given identifier from the cache.
     *
     * @param aggregateId
     *            Aggregate to remove from cache.
     */
    void remove(@NotNull AggregateRootId aggregateId);

}
