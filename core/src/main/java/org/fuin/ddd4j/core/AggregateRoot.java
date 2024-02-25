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

import java.util.List;

/**
 * Dedicated entity of a group of entities (The group is called "Aggregate") that guarantees the consistency of changes being made within
 * the group by forbidding external objects from holding direct references to its members.
 *
 * @param <ID>
 *            Type of the aggregate root identifier.
 */
public interface AggregateRoot<ID extends AggregateRootId> extends Entity<ID> {

    /**
     * Returns the unique aggregate root identifier.
     *
     * @return Identifier.
     */
    @Override
    ID getId();

    /**
     * Returns a list of uncommitted changes.
     *
     * @return List of events that were not persisted yet.
     */
    @NotNull
    List<DomainEvent<?>> getUncommittedChanges();

    /**
     * Returns the information if the aggregate has uncommited changes.
     *
     * @return TRUE if the aggregate will return a non-empty list for {@link #getUncommittedChanges()}, else FALSE.
     */
    boolean hasUncommitedChanges();

    /**
     * Clears the internal change list and sets the new version number.
     */
    void markChangesAsCommitted();

    /**
     * Returns the current version of the aggregate.
     *
     * @return Current version that does NOT included uncommitted changes.
     */
    int getVersion();

    /**
     * Returns the next version of the aggregate.
     *
     * @return Next version that includes all currently uncommitted changes.
     */
    int getNextVersion();


    /**
     * Returns the next version useful when creating an event for being applied:<br>
     * <code>apply(new MyEvent( ... , getNextApplyVersion()))</code>.
     *
     * @return Version for the event.
     */
    AggregateVersion getNextApplyVersion();


    /**
     * Loads the aggregate with historic events.
     *
     * @param history
     *            List of historic events.
     */
    void loadFromHistory(@NotNull DomainEvent<?>... history);

    /**
     * Loads the aggregate with historic events.
     *
     * @param history
     *            List of historic events.
     */
    void loadFromHistory(@NotNull List<DomainEvent<?>> history);

}
