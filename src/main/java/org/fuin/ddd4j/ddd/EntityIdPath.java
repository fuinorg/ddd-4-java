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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.vo.ValueObjectWithBaseType;

/**
 * An ordered list of entity identifiers. An aggregate root will be the first
 * entry if it's contained in the list.
 */
@XmlJavaTypeAdapter(EntityIdPathConverter.class)
public final class EntityIdPath implements ValueObjectWithBaseType<String>,
	Serializable {

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
	    throw new IllegalArgumentException(
		    "Identifier array cannot be empty");
	}
	this.entityIds = new ArrayList<EntityId>();
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
	if (ids.size() == 0) {
	    throw new IllegalArgumentException(
		    "Identifier list cannot be empty");
	}
	this.entityIds = new ArrayList<EntityId>();
	this.entityIds.addAll(ids);
    }

    /**
     * Creates a NEW list of the entity identifiers contained in the entity id
     * path and returns an iterator on it. This means deleting an element using
     * the {@link Iterator#remove()} method will NOT remove something from this
     * entity id path.
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
	final List<EntityId> list = new ArrayList<EntityId>();
	for (int i = 1; i < entityIds.size(); i++) {
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

    /**
     * Returns the path as string.
     * 
     * @return Path.
     */
    public final String asString() {
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
    public final String asBaseType() {
	return asString();
    }

    @Override
    public final Class<String> getBaseType() {
	return String.class;
    }

}
