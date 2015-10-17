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
import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.vo.ValueObject;

/**
 * Unique identifier based on a UUID.
 */
public abstract class AbstractUUIDVO implements ValueObject,
        Comparable<AbstractUUIDVO>, Serializable {

    private static final long serialVersionUID = 1000L;

    private final UUID uuid;

    private transient String uuidStr;

    /**
     * Default constructor.
     */
    public AbstractUUIDVO() {
        super();
        uuid = UUID.randomUUID();
    }

    /**
     * Constructor with UUID.
     * 
     * @param uuid
     *            UUID.
     */
    public AbstractUUIDVO(@NotNull final UUID uuid) {
        super();
        Contract.requireArgNotNull("uuid", uuid);
        this.uuid = uuid;
    }

    @Override
    public final int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AbstractUUIDVO other = (AbstractUUIDVO) obj;
        return uuid.equals(other.uuid);
    }

    @Override
    public final int compareTo(final AbstractUUIDVO other) {
        return uuid.compareTo(other.uuid);
    }

    /**
     * Returns the UUID as string.
     * 
     * @return String representation.
     */
    public final String asString() {
        if (uuidStr == null) {
            uuidStr = uuid.toString();
        }
        return uuidStr;
    }

    @Override
    public final String toString() {
        return asString();
    }

}
