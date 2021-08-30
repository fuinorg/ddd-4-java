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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.fuin.objects4j.common.ConstraintViolationException;
import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.ui.Label;
import org.fuin.objects4j.ui.Prompt;
import org.fuin.objects4j.ui.ShortLabel;
import org.fuin.objects4j.ui.Tooltip;
import org.fuin.objects4j.vo.AbstractStringValueObject;

/**
 * An ordered list of entity identifiers. An aggregate root will be the first entry if it's contained in the list.
 */
@Label("Entity identifier path")
@ShortLabel("EntityIdPath")
@Tooltip("An ordered list of entity identifiers that is separated by '/'. "
        + "An aggregate root will be the first entry if it's contained in the list.")
@Prompt("Customer deb2317d-5b3a-4d56-a9df-689f4c4ff982/Person 123")
@XmlJavaTypeAdapter(EntityIdPathConverter.class)
public final class EntityIdPath extends AbstractStringValueObject implements Serializable {

    private static final long serialVersionUID = 1000L;

    /** Divides the entity identifiers in the path. */
    public static final String PATH_SEPARATOR = "/";

    private final List<EntityId> entityIds;

    /**
     * Constructor with ID array.
     * 
     * @param entityIds
     *            Entity identifier in correct order (from outer to inner).
     */
    public EntityIdPath(final EntityId... entityIds) {
        super();
        Contract.requireArgNotNull("entityIds", entityIds);
        if (entityIds.length == 0) {
            throw new ConstraintViolationException("Identifier array cannot be empty");
        }
        for (final EntityId entityId : entityIds) {
            if (entityId == null) {
                throw new ConstraintViolationException("Identifiers in the array cannot be null");
            }
        }
        this.entityIds = new ArrayList<>();
        this.entityIds.addAll(Arrays.asList(entityIds));
    }

    /**
     * Constructor with ID list.
     * 
     * @param ids
     *            Entity identifiers in correct order (from outer to inner).
     */
    public EntityIdPath(final List<EntityId> ids) {
        Contract.requireArgNotNull("ids", ids);
        if (ids.isEmpty()) {
            throw new ConstraintViolationException("Identifier list cannot be empty");
        }
        for (final EntityId entityId : ids) {
            if (entityId == null) {
                throw new ConstraintViolationException("Identifiers in the list cannot be null");
            }
        }
        this.entityIds = new ArrayList<>();
        this.entityIds.addAll(ids);
    }

    /**
     * Creates a NEW list of the entity identifiers contained in the entity id path and returns an iterator on it. This means deleting an
     * element using the {@link Iterator#remove()} method will NOT remove something from this entity id path.
     * 
     * @return Iterator on a new list instance.
     */
    public final Iterator<EntityId> iterator() {
        return new ArrayList<EntityId>(entityIds).iterator();
    }

    /**
     * Returns the first entity identifier in the path.
     * 
     * @return First entity identifier in the path.
     * 
     * @param <T>
     *            Type of the entity identifier that is returned.
     */
    @SuppressWarnings("unchecked")
    public final <T extends EntityId> T first() {
        return (T) entityIds.get(0);
    }

    /**
     * Returns the last entity identifier in the path.
     * 
     * @return Last entity identifier in the path.
     * 
     * @param <T>
     *            Type of the entity identifier that is returned.
     */
    @SuppressWarnings("unchecked")
    public final <T extends EntityId> T last() {
        return (T) entityIds.get(entityIds.size() - 1);
    }

    /**
     * Returns the path without the first entry.
     * 
     * @return Rest or NULL if the path has only one element.
     */
    public final EntityIdPath rest() {
        if (entityIds.size() == 1) {
            return null;
        }
        final List<EntityId> list = new ArrayList<>();
        for (int i = 1; i < entityIds.size(); i++) {
            list.add(entityIds.get(i));
        }
        return new EntityIdPath(list);
    }

    /**
     * Returns the parent path without the last entry.
     * 
     * @return Parent identifier path or NULL if this is an aggregate root ID.
     */
    public EntityIdPath parent() {
        if (entityIds.size() == 1) {
            return null;
        }
        final List<EntityId> list = new ArrayList<>();
        for (int i = 0; i < entityIds.size() - 1; i++) {
            list.add(entityIds.get(i));
        }
        return new EntityIdPath(list);
    }

    /**
     * Returns the number of elements in the path.
     * 
     * @return Number of identifiers contained in the path.
     */
    public final int size() {
        return entityIds.size();
    }

    @Override
    public final String asBaseType() {
        final StringBuilder sb = new StringBuilder();
        for (final EntityId entityId : entityIds) {
            if (sb.length() > 0) {
                sb.append(PATH_SEPARATOR);
            }
            sb.append(entityId.asTypedString());
        }
        return sb.toString();
    }

    @Override
    public final String toString() {
        return asBaseType();
    }

}
