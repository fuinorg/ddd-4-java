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

import org.fuin.ddd4j.ddd.*;

import java.util.ArrayList;
import java.util.List;

// CHECKSTYLE:OFF
public final class ARoot extends BaseRoot<AId, ARoot> {

    private AId id;

    private List<BEntity> childs;

    private AbstractDomainEvent<?> lastEvent;

    public ARoot() {
        super();
    }

    public ARoot(final AId id) {
        super();
        apply(new ACreatedEvent(id));
    }

    @Override
    public AId getId() {
        return id;
    }

    @Override
    public EntityType getType() {
        return AId.TYPE;
    }

    @ChildEntityLocator
    private BEntity find(final BId bid) {
        for (final BEntity child : childs) {
            if (child.getId().equals(bid)) {
                return child;
            }
        }
        return null;
    }

    public void addB(final BId bid) throws DuplicateEntityException {
        // Verify business rules
        final BEntity found = find(bid);
        if (found != null) {
            // An entity can only be added once to it's parent
            throw new DuplicateEntityException(new EntityIdPath(id), bid);
        }

        // Apply event
        apply(new BAddedEvent(id, bid));
    }

    public void addC(final BId bid, final CId cid) throws DuplicateEntityException, EntityNotFoundException {
        // Delegate processing to child entity
        final BEntity found = find(bid);
        if (found == null) {
            throw new EntityNotFoundException(new EntityIdPath(id), bid);
        }
        found.add(cid);
    }

    public void doItC(final BId bid, final CId cid) throws EntityNotFoundException {
        // Delegate processing to child entity
        final BEntity found = find(bid);
        if (found == null) {
            throw new EntityNotFoundException(new EntityIdPath(id), bid);
        }
        found.doIt(cid);
    }

    @ApplyEvent
    public void applyEvent(final ACreatedEvent event) {
        this.id = event.getId();
        this.childs = new ArrayList<BEntity>();
        lastEvent = event;
    }

    @ApplyEvent
    public void applyEvent(final BAddedEvent event) {
        childs.add(new BEntity(this, event.getBId()));
        lastEvent = event;
    }

    public AbstractDomainEvent<?> getLastEvent() {
        return lastEvent;
    }

    public BEntity getFirstChild() {
        return childs.get(0);
    }

}
// CHECKSTYLE:OFF
