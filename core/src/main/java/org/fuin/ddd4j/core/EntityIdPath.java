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

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.fuin.objects4j.common.ConstraintViolationException;
import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.core.AbstractStringValueObject;
import org.fuin.objects4j.ui.Label;
import org.fuin.objects4j.ui.Prompt;
import org.fuin.objects4j.ui.ShortLabel;
import org.fuin.objects4j.ui.Tooltip;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * An ordered list of entity identifiers. An aggregate root will be the first entry if it's contained in the list.
 */
@Label("Entity identifier path")
@ShortLabel("EntityIdPath")
@Tooltip("An ordered list of entity identifiers that is separated by '/'. "
        + "An aggregate root will be the first entry if it's contained in the list.")
@Prompt("Customer deb2317d-5b3a-4d56-a9df-689f4c4ff982/Person 123")
public final class EntityIdPath extends AbstractStringValueObject implements Serializable {

    @Serial
    private static final long serialVersionUID = 1000L;

    /**
     * Divides the entity identifiers in the path.
     */
    public static final String PATH_SEPARATOR = "/";

    private final List<EntityId> entityIds;

    /**
     * Constructor with ID array.
     *
     * @param entityIds Entity identifier in correct order (from outer to inner).
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
     * @param ids Entity identifiers in correct order (from outer to inner).
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
        return new ArrayList<>(entityIds).iterator();
    }

    /**
     * Returns the first entity identifier in the path.
     *
     * @param <T> Type of the entity identifier that is returned.
     * @return First entity identifier in the path.
     */
    @SuppressWarnings("unchecked")
    public final <T extends EntityId> T first() {
        return (T) entityIds.get(0);
    }

    /**
     * Returns the last entity identifier in the path.
     *
     * @param <T> Type of the entity identifier that is returned.
     * @return Last entity identifier in the path.
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
            if (!sb.isEmpty()) {
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

    /**
     * Converts a given string into an instance of this class.
     *
     * @param factory Factory used to create enitity identifiers.
     * @param str     String to convert.
     * @return New instance.
     */
    @Nullable
    public static EntityIdPath valueOf(@NotNull EntityIdFactory factory, @Nullable final String str) {
        Contract.requireArgNotNull("factory", factory);
        if (str == null) {
            return null;
        }
        final List<String> entryList = entries(str);
        if (entryList.isEmpty()) {
            throw new IllegalArgumentException("Invalid entity path: '" + str + "'");
        }
        final List<EntityId> ids = new ArrayList<>();
        for (final String entry : entryList) {
            ids.add(EntityId.valueOf(factory, entry));
        }
        return new EntityIdPath(ids);
    }

    /**
     * Check that a given string is a well-formed email address.
     *
     * @param factory Factory used to create enitity identifiers.
     * @param value   Value to check.
     * @return Returns {@literal true} if it's a valid email address else {@literal false} is returned.
     */
    public static boolean isValid(@NotNull EntityIdFactory factory, @Nullable final String value) {
        Contract.requireArgNotNull("factory", factory);
        if (value == null) {
            return true;
        }
        if (value.isEmpty()) {
            return false;
        }
        final List<String> entryList = entries(value);
        if (entryList.isEmpty()) {
            return false;
        }
        for (final String entry : entryList) {
            if (!EntityId.isValid(factory, entry)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the argument is a valid email and throws an exception if this is not the case.
     *
     * @param factory Factory used to create enitity identifiers.
     * @param name    Name of the value for a possible error message.
     * @param value   Value to check.
     * @throws ConstraintViolationException The value was not valid.
     */
    public static void requireArgValid(@NotNull EntityIdFactory factory, @NotNull final String name, @Nullable final String value) throws ConstraintViolationException {
        Contract.requireArgNotNull("factory", factory);
        Contract.requireArgNotNull("name", name);

        if (!isValid(factory, value)) {
            throw new ConstraintViolationException("The argument '" + name + "' is not valid: '" + value + "'");
        }

    }

    private static List<String> entries(final String value) {
        final List<String> list = new ArrayList<>();
        final StringTokenizer tok = new StringTokenizer(value, EntityIdPath.PATH_SEPARATOR);
        while (tok.hasMoreTokens()) {
            final String str = tok.nextToken();
            list.add(str);
        }
        return list;
    }

}
