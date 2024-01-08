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
package org.fuin.ddd4j.ddd;

import jakarta.validation.constraints.NotNull;
import jakarta.annotation.Nullable;

/**
 * Repository that supports CRUD operations for an aggregate.
 * 
 * @param <ID>
 *            Type of the aggregate root identifier.
 * @param <T>
 *            Type of the aggregate.
 */
public interface Repository<ID extends AggregateRootId, T extends AggregateRoot<ID>> {

    /**
     * Returns the class of the aggregate in the repository.
     * 
     * @return Aggregate class.
     */
    @NotNull
    public Class<T> getAggregateClass();

    /**
     * Returns a unique name for the aggregate root type. This can be used to provide a shorted name for an aggregate type. Another good
     * choice may be the FQN of the {@link #getAggregateClass()} class.
     * 
     * @return Name of the type of the aggregate.
     */
    @NotNull
    public EntityType getAggregateType();

    /**
     * Factory method to create a new aggregate. Just creates a new instance without doing anything else. The aggregate identifier will NOT
     * be set.
     * 
     * @return New aggregate instance that is NOT persisted. Use the {@link #update(AggregateRoot, String, Object)} method to persist this
     *         new aggregate.
     */
    @NotNull
    public T create();

    /**
     * Reads the latest version of an aggregate.
     * 
     * @param id
     *            Unique aggregate identifier.
     * 
     * @return Aggregate.
     * 
     * @throws AggregateNotFoundException
     *             An aggregate with the given identifier was not found.
     * @throws AggregateDeletedException
     *             The aggregate with the given identifier was already deleted.
     */
    @NotNull
    public T read(@NotNull ID id) throws AggregateNotFoundException, AggregateDeletedException;

    /**
     * Reads a given version of an aggregate.
     * 
     * @param id
     *            Unique aggregate identifier.
     * @param version
     *            Version to read.
     * 
     * @return Aggregate.
     * 
     * @throws AggregateNotFoundException
     *             An aggregate with the given identifier was not found.
     * @throws AggregateDeletedException
     *             The aggregate with the given identifier was already deleted.
     * @throws AggregateVersionNotFoundException
     *             An aggregate with the requested version does not exist.
     */
    @NotNull
    public T read(@NotNull ID id, int version)
            throws AggregateNotFoundException, AggregateDeletedException, AggregateVersionNotFoundException;

    /**
     * Saves the changes on an aggregate in the repository including some meta data.
     * 
     * @param aggregate
     *            Aggregate to store.
     * @param metaType
     *            Optional unique name that identifies the type of meta data.
     * @param metaData
     *            Optional information that is not directly available in the event.
     * 
     * @throws AggregateVersionConflictException
     *             The expected version didn't match the actual version.
     * @throws AggregateDeletedException
     *             The aggregate with the given identifier was already deleted.
     * @throws AggregateNotFoundException
     *             An aggregate with the given identifier was not found.
     */
    public void update(@NotNull T aggregate, @Nullable String metaType, @Nullable Object metaData)
            throws AggregateVersionConflictException, AggregateNotFoundException, AggregateDeletedException;

    /**
     * Adds a new aggregate to the repository without any meta data. The method will fail if an aggregate with the same ID already exists.
     * 
     * @param aggregate
     *            Aggregate to add.
     * 
     * @throws AggregateAlreadyExistsException
     *             The aggregate with the given version could not be created because it already exists.
     * @throws AggregateDeletedException
     *             The aggregate with the given identifier was already deleted.
     */
    public void add(@NotNull T aggregate) throws AggregateAlreadyExistsException, AggregateDeletedException;

    /**
     * Adds a new aggregate to the repository with some meta data. The method will fail if an aggregate with the same ID already exists.
     * 
     * @param aggregate
     *            Aggregate to add.
     * @param metaType
     *            Optional unique name that identifies the type of meta data.
     * @param metaData
     *            Optional information that is not directly available in the event.
     * 
     * @throws AggregateAlreadyExistsException
     *             The aggregate with the given version could not be created because it already exists.
     * @throws AggregateDeletedException
     *             The aggregate with the given identifier was already deleted.
     */
    public void add(@NotNull T aggregate, @Nullable String metaType, @Nullable Object metaData)
            throws AggregateAlreadyExistsException, AggregateDeletedException;

    /**
     * Saves the changes on an aggregate in the repository without any meta data.
     * 
     * @param aggregate
     *            Aggregate to store.
     * 
     * @throws AggregateVersionConflictException
     *             The expected version didn't match the actual version.
     * @throws AggregateDeletedException
     *             The aggregate with the given identifier was already deleted.
     * @throws AggregateNotFoundException
     *             An aggregate with the given identifier was not found.
     */
    public void update(@NotNull T aggregate)
            throws AggregateVersionConflictException, AggregateNotFoundException, AggregateDeletedException;

    /**
     * Deletes an aggregate from the repository. If the aggregate was already deleted, the method will do nothing.
     * 
     * @param aggregateId
     *            Identifier of the aggregate to delete.
     * @param expectedVersion
     *            Expected (current) version of the aggregate.
     * 
     * @throws AggregateVersionConflictException
     *             The expected version didn't match the actual version.
     */
    public void delete(@NotNull ID aggregateId, int expectedVersion) throws AggregateVersionConflictException;

}
