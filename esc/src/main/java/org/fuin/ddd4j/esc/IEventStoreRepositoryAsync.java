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
package org.fuin.ddd4j.esc;

import org.fuin.ddd4j.core.AggregateAlreadyExistsException;
import org.fuin.ddd4j.core.AggregateDeletedException;
import org.fuin.ddd4j.core.AggregateNotFoundException;
import org.fuin.ddd4j.core.AggregateRoot;
import org.fuin.ddd4j.core.AggregateRootId;
import org.fuin.ddd4j.core.AggregateVersionConflictException;
import org.fuin.ddd4j.core.AggregateVersionNotFoundException;
import org.fuin.ddd4j.core.DomainEvent;
import org.fuin.ddd4j.core.EntityType;
import org.fuin.ddd4j.core.TenantContext;
import org.fuin.objects4j.common.ThreadSafetyUndefined;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Asynchronous CRUD repository for an event sourced aggregate. Mirrors {@link IEventStoreRepository} but the
 * I/O operations return a {@link CompletableFuture}: any domain failure (e.g.
 * {@link AggregateNotFoundException}) is delivered by completing the future <i>exceptionally</i> instead of by
 * a checked {@code throws}.
 *
 * @param <ID>        Type of the aggregate root identifier.
 * @param <AGGREGATE> Type of the aggregate root.
 */
@ThreadSafetyUndefined
public interface IEventStoreRepositoryAsync<ID extends AggregateRootId, AGGREGATE extends AggregateRoot<ID>> {

    /**
     * Returns the class of the aggregate in the repository.
     *
     * @return Aggregate class.
     */
    Class<AGGREGATE> getAggregateClass();

    /**
     * Returns a unique name for the aggregate root type.
     *
     * @return Name of the type of the aggregate.
     */
    EntityType getAggregateType();

    /**
     * Factory method that creates a new, non-persisted aggregate instance (without setting the identifier).
     *
     * @return New aggregate instance.
     */
    AGGREGATE create();

    /**
     * Returns the context to retrieve the tenant (if there is one).
     *
     * @return Context that has the tenant or empty if it's no multi-tenant environment.
     */
    default Optional<TenantContext> getTenantContext() {
        return Optional.empty();
    }

    /**
     * Reads the latest version of an aggregate. The future completes exceptionally with
     * {@link AggregateNotFoundException} or {@link AggregateDeletedException}.
     *
     * @param id Unique aggregate identifier.
     * @return Future with the aggregate.
     */
    CompletableFuture<AGGREGATE> read(ID id);

    /**
     * Reads a given version of an aggregate. The future completes exceptionally with
     * {@link AggregateNotFoundException}, {@link AggregateDeletedException} or
     * {@link AggregateVersionNotFoundException}.
     *
     * @param id      Unique aggregate identifier.
     * @param version Version to read (or {@code null} for the latest version).
     * @return Future with the aggregate.
     */
    CompletableFuture<AGGREGATE> read(ID id, @Nullable Integer version);

    /**
     * Saves the changes on an aggregate without any metadata. The future completes exceptionally with
     * {@link AggregateVersionConflictException}, {@link AggregateNotFoundException} or
     * {@link AggregateDeletedException}.
     *
     * @param aggregate Aggregate to store.
     * @return Future that completes when the changes were saved.
     */
    CompletableFuture<Void> update(AGGREGATE aggregate);

    /**
     * Saves the changes on an aggregate including some metadata. The future completes exceptionally with
     * {@link AggregateVersionConflictException}, {@link AggregateNotFoundException} or
     * {@link AggregateDeletedException}.
     *
     * @param aggregate Aggregate to store.
     * @param metaType  Optional unique name that identifies the type of metadata.
     * @param metaData  Optional information that is not directly available in the event.
     * @return Future that completes when the changes were saved.
     */
    CompletableFuture<Void> update(AGGREGATE aggregate, @Nullable String metaType, @Nullable Object metaData);

    /**
     * Adds a new aggregate without any metadata. The future completes exceptionally with
     * {@link AggregateAlreadyExistsException} or {@link AggregateDeletedException}.
     *
     * @param aggregate Aggregate to add.
     * @return Future that completes when the aggregate was added.
     */
    CompletableFuture<Void> add(AGGREGATE aggregate);

    /**
     * Adds a new aggregate with some metadata. The future completes exceptionally with
     * {@link AggregateAlreadyExistsException} or {@link AggregateDeletedException}.
     *
     * @param aggregate Aggregate to add.
     * @param metaType  Optional unique name that identifies the type of metadata.
     * @param metaData  Optional information that is not directly available in the event.
     * @return Future that completes when the aggregate was added.
     */
    CompletableFuture<Void> add(AGGREGATE aggregate, @Nullable String metaType, @Nullable Object metaData);

    /**
     * Deletes an aggregate. If it was already deleted, the operation does nothing. The future completes
     * exceptionally with {@link AggregateVersionConflictException}.
     *
     * @param aggregateId     Identifier of the aggregate to delete.
     * @param expectedVersion Expected (current) version of the aggregate (or {@code null} for any version).
     * @return Future that completes when the aggregate was deleted.
     */
    CompletableFuture<Void> delete(ID aggregateId, @Nullable Integer expectedVersion);

    /**
     * Reads all events for the given aggregate starting with a given number. The future completes exceptionally
     * with {@link AggregateNotFoundException} or {@link AggregateDeletedException}.
     *
     * @param aggregateId  Unique identifier of the aggregate to read the events for.
     * @param startVersion First event number to read.
     * @return Future with the list of events.
     */
    CompletableFuture<List<DomainEvent<?>>> readEvents(ID aggregateId, int startVersion);

}
