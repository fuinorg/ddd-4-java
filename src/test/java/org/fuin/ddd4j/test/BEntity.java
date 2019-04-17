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
package org.fuin.ddd4j.test;

import java.util.ArrayList;
import java.util.List;

import org.fuin.ddd4j.ddd.AbstractDomainEvent;
import org.fuin.ddd4j.ddd.AbstractEntity;
import org.fuin.ddd4j.ddd.ApplyEvent;
import org.fuin.ddd4j.ddd.ChildEntityLocator;
import org.fuin.ddd4j.ddd.DuplicateEntityException;
import org.fuin.ddd4j.ddd.EntityIdPath;
import org.fuin.ddd4j.ddd.EntityNotFoundException;
import org.fuin.ddd4j.ddd.EntityType;

// CHECKSTYLE:OFF
public final class BEntity extends AbstractEntity<AId, ARoot, BId> {

    private final BId id;

    private final List<CEntity> childs;

    private AbstractDomainEvent<?> lastEvent;

    public BEntity(final ARoot root, final AggregateRootImpl impl, final BId id) {
        super(root, impl);
        this.id = id;
        this.childs = new ArrayList<CEntity>();
    }

    @Override
    public BId getId() {
        return id;
    }

    @Override
    public EntityType getType() {
        return BId.TYPE;
    }

    @ChildEntityLocator
    private CEntity find(final CId bid) {
        for (final CEntity child : childs) {
            if (child.getId().equals(bid)) {
                return child;
            }
        }
        return null;
    }

    /**
     * Adds a child entity to this instance.
     * 
     * @param cid
     *            Unique identifier of the child.
     * 
     * @throws DuplicateEntityException
     *             The child already exists.
     */
    public void add(final CId cid) throws DuplicateEntityException {

        // Verify business rules
        final CEntity found = find(cid);
        if (found != null) {
            // An entity can only be added once to it's parent
            throw new DuplicateEntityException(getEntityIdPath(), cid);
        }

        // Apply event
        apply(new CAddedEvent(getRoot().getId(), id, cid));
    }

    /**
     * Method that calls a child entity.
     * 
     * @param cid
     *            Child entity to call the doIt() method on.
     * @throws EntityNotFoundException
     */
    public void doIt(final CId cid) throws EntityNotFoundException {
        final CEntity found = find(cid);
        if (found == null) {
            throw new EntityNotFoundException(getEntityIdPath(), cid);
        }
        found.doIt();
    }

    @ApplyEvent
    public void applyEvent(final CAddedEvent event) {
        childs.add(new CEntity(getRoot(), getAggregateRootImpl(), id, event.getCId()));
        lastEvent = event;
    }

    // Test method
    public AbstractDomainEvent<?> getLastEvent() {
        return lastEvent;
    }

    // Test method
    public CEntity getFirstChild() {
        return childs.get(0);
    }

    private EntityIdPath getEntityIdPath() {
        return new EntityIdPath(getRootId(), id);
    }

}
// CHECKSTYLE:ON
