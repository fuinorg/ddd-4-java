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

import org.jspecify.annotations.Nullable;
import org.fuin.objects4j.common.ThreadSafe;

import java.util.Optional;

/**
 * Cache for aggregates of the same type.
 * All implementations are expected to be thread safe.
 *
 * @param <AGGREGATE>
 *            Type of the aggregate.
 */
@ThreadSafe
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
    @Nullable
    default AGGREGATE get(AggregateRootId aggregateId, @Nullable Integer version) {
        return get(null, aggregateId, version);
    }

    /**
     * Tries to read an aggregate of a tenant with the given identifier from the cache.
     *
     * @param tenantId
     *            Unique tenant identifier.
     * @param aggregateId
     *            Aggregate to load.
     * @param version
     *            Version to load or <code>null</code> for latest.
     *
     * @return Cached aggregate or <code>null</code> if it was not found in the cache.
     */
    @Nullable
    AGGREGATE get(@Nullable TenantId tenantId, AggregateRootId aggregateId, @Nullable Integer version);

    /**
     * Puts an aggregate with the given identifier in the cache.
     *
     * @param aggregateId
     *            Aggregate to load.
     * @param aggregate
     *            Aggregate to cache.
     */
    default void put(AggregateRootId aggregateId, AGGREGATE aggregate) {
        put(null, aggregateId, aggregate);
    }

    /**
     * Puts an aggregate of a tenant with the given identifier in the cache.
     *
     * @param tenantId
     *            Unique tenant identifier.
     * @param aggregateId
     *            Aggregate to load.
     * @param aggregate
     *            Aggregate to cache.
     */
    void put(@Nullable TenantId tenantId, AggregateRootId aggregateId, AGGREGATE aggregate);

    /**
     * Removes the aggregate with the given identifier from the cache.
     *
     * @param aggregateId
     *            Aggregate to remove from cache.
     */
    default void remove(AggregateRootId aggregateId) {
        remove(null, aggregateId);
    }

    /**
     * Removes the aggregate of a tenant with the given identifier from the cache.
     *
     * @param tenantId
     *            Unique tenant identifier.
     * @param aggregateId
     *            Aggregate to remove from cache.
     */
    void remove(@Nullable TenantId tenantId, AggregateRootId aggregateId);

}
