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
import org.fuin.objects4j.common.ExceptionShortIdentifable;

import java.io.Serial;
import java.util.Objects;

import static org.fuin.ddd4j.core.Ddd4JUtils.SHORT_ID_PREFIX;

/**
 * Signals that an entity already existed in its parent.
 */
public final class DuplicateEntityException extends Exception implements ExceptionShortIdentifable {

    /**
     * Unique name of the element to use for XML and JSON marshalling/unmarshalling.
     */
    public static final String ELEMENT_NAME = "duplicate-entity-exception";

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Unique short identifier of this exception.
     */
    public static final String SHORT_ID = SHORT_ID_PREFIX + "-DUPLICATE_ENTITY";

    private final String parentIdPath;

    private final String entityId;

    /**
     * Constructor with all data.
     *
     * @param parentIdPath Path from root to parent.
     * @param entityId     Unique identifier of the entity that already existed.
     */
    public DuplicateEntityException(@NotNull final EntityIdPath parentIdPath, @NotNull final EntityId entityId) {
        this(parentIdPath.asString(), entityId.asTypedString());
    }

    /**
     * Constructor with entity identifier path.
     *
     * @param entityIdPath Entity identifier path (from root to entity). Required to contain at least two elements.
     */
    public DuplicateEntityException(@NotNull final EntityIdPath entityIdPath) {
        this(Objects.requireNonNull(entityIdPath.parent(), "entityIdPath has not parent"), entityIdPath.last());
    }

    /**
     * Constructor with string data.
     *
     * @param parentIdPath Path from root to parent.
     * @param entityId     Unique identifier of the entity that already existed.
     */
    public DuplicateEntityException(@NotNull final String parentIdPath, @NotNull final String entityId) {
        super(entityId + " already exists in " + parentIdPath);
        this.parentIdPath = parentIdPath;
        this.entityId = entityId;
    }

    @Override
    public final String getShortId() {
        return SHORT_ID;
    }

    /**
     * Returns the path from root to parent.
     *
     * @return Path.
     */
    public final String getParentIdPath() {
        return parentIdPath;
    }

    /**
     * Returns the unique identifier of the entity.
     *
     * @return Unknown entity identifier.
     */
    public final String getEntityId() {
        return entityId;
    }


}
