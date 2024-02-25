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
import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.common.ExceptionShortIdentifable;

import java.io.Serial;

import static org.fuin.ddd4j.core.Ddd4JUtils.SHORT_ID_PREFIX;

/**
 * Signals that an entity was not found.
 */
public final class EntityNotFoundException extends Exception implements ExceptionShortIdentifable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Unique short identifier of this exception.
     */
    public static final String SHORT_ID = SHORT_ID_PREFIX + "-ENTITY_NOT_FOUND";

    private final EntityIdPath parentIdPath;

    private final EntityId entityId;

    /**
     * Constructor with all data.
     *
     * @param parentIdPath Path from root to parent or {@literal null} if the entity identifier is a root aggregate ID.
     * @param entityId     Unique identifier of the entity that was not found.
     */
    public EntityNotFoundException(@Nullable final EntityIdPath parentIdPath, @NotNull final EntityId entityId) {
        super(parentIdPath == null ? entityId.asTypedString() + " not found"
                : entityId.asTypedString() + " not found in " + parentIdPath.asString());
        Contract.requireArgNotNull("entityId", entityId);
        this.parentIdPath = parentIdPath;
        this.entityId = entityId;
    }

    /**
     * Constructor with entity identifier path.
     *
     * @param entityIdPath Entity identifier path (from root to entity).
     */
    public EntityNotFoundException(@NotNull final EntityIdPath entityIdPath) {
        this(entityIdPath.parent(), entityIdPath.last());
    }

    @Override
    public String getShortId() {
        return SHORT_ID;
    }

    /**
     * Returns the path from root to parent.
     *
     * @return Path.
     */
    @Nullable
    public EntityIdPath getParentIdPath() {
        return parentIdPath;
    }

    /**
     * Returns the unique identifier of the entity.
     *
     * @return Unknown entity identifier.
     */
    @NotNull
    public EntityId getEntityId() {
        return entityId;
    }

}
