/**
 * Copyright (C) 2013 Future Invent Informationsmanagement GmbH. All rights
 * reserved. <http://www.fuin.org/>
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
 * along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fuin.ddd4j.ddd;

import javax.validation.constraints.NotNull;

import org.fuin.objects4j.common.NeverNull;

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
	@NeverNull
	public Class<T> getAggregateClass();

	/**
	 * Returns a unique name for the aggregate root type. This can be used to
	 * provide a shorted name for an aggregate type. Another good choice may be
	 * the FQN of the {@link #getAggregateClass()} class.
	 * 
	 * @return Name of the type of the aggregate.
	 */
	@NeverNull
	public EntityType getAggregateType();

	/**
	 * Factory method to create a new aggregate. Just creates a new instance
	 * without doing anything else. The aggregate identifier will NOT be set.
	 * 
	 * @return New aggregate instance that is NOT persisted. Use the
	 *         {@link #update(AggregateRoot, MetaData)} method to persist this
	 *         new aggregate.
	 */
	@NeverNull
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
	@NeverNull
	public T read(@NotNull ID id) throws AggregateNotFoundException,
			AggregateDeletedException;

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
	@NeverNull
	public T read(@NotNull ID id, int version)
			throws AggregateNotFoundException, AggregateDeletedException,
			AggregateVersionNotFoundException;

	/**
	 * Saves the changes on an aggregate in the repository.
	 * 
	 * @param aggregate
	 *            Aggregate to store.
	 * @param metaData
	 *            Optional information that is not directly available in the
	 *            event.
	 * 
	 * @throws AggregateVersionConflictException
	 *             The expected version didn't match the actual version.
	 * @throws AggregateDeletedException
	 *             The aggregate with the given identifier was already deleted.
	 * @throws AggregateNotFoundException
	 *             An aggregate with the given identifier was not found.
	 */
	@NeverNull
	public void update(@NotNull T aggregate, MetaData metaData)
			throws AggregateVersionConflictException,
			AggregateNotFoundException, AggregateDeletedException;

	/**
	 * Deletes an aggregate from the repository.
	 * 
	 * @param aggregateId
	 *            Identifier of the aggregate to delete.
	 * @param expectedVersion
	 *            Expected (current) version of the aggregate.
	 * @param metaData
	 *            Additional information that are not directly available in the
	 *            event.
	 * 
	 * @throws AggregateVersionConflictException
	 *             The expected version didn't match the actual version.
	 */
	public void delete(@NotNull ID aggregateId, int expectedVersion)
			throws AggregateVersionConflictException;

}
