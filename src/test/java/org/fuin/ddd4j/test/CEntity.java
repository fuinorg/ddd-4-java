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
package org.fuin.ddd4j.test;

import org.fuin.ddd4j.ddd.AbstractEntity;
import org.fuin.ddd4j.ddd.EntityType;
import org.fuin.ddd4j.ddd.ApplyEvent;

// CHECKSTYLE:OFF
public class CEntity extends AbstractEntity<AId, ARoot, CId> {

    private final ARoot root;

    private final BId parentId;

    private final CId id;

    private CEvent lastEvent;

    public CEntity(final ARoot root, final BId parentId, final CId id) {
        super(root);
        this.root = root;
        this.parentId = parentId;
        this.id = id;
    }

    @Override
    public CId getId() {
        return id;
    }

    @Override
    public EntityType getType() {
        return CId.TYPE;
    }

    public void doIt() {
        apply(new CEvent(root.getId(), parentId, id));
    }

    @ApplyEvent
    public void applyEvent(final CEvent event) {
        lastEvent = event;
    }

    public CEvent getLastEvent() {
        return lastEvent;
    }

}
// CHECKSTYLE:ON
