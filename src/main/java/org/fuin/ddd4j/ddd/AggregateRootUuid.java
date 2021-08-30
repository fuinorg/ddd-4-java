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

import java.util.UUID;
import java.util.regex.Pattern;

import jakarta.validation.constraints.NotNull;

import org.fuin.objects4j.common.ConstraintViolationException;
import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.vo.ValueObjectWithBaseType;

/**
 * UUID based aggregate root identifier.
 */
public abstract class AggregateRootUuid implements AggregateRootId, Comparable<AggregateRootUuid>, ValueObjectWithBaseType<UUID> {

    private static final long serialVersionUID = 1000L;

    private static final Pattern PATTERN = Pattern
            .compile("\\{?\\p{XDigit}{8}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{12}\\}?");

    private final EntityType entityType;

    private final UUID uuid;

    /**
     * Constructor with entity type.
     * 
     * @param entityType
     *            Entity type.
     */
    public AggregateRootUuid(@NotNull final EntityType entityType) {
        this(entityType, UUID.randomUUID());
    }

    /**
     * Constructor with UUID.
     * 
     * @param entityType
     *            Entity type.
     * @param uuid
     *            UUID.
     */
    public AggregateRootUuid(@NotNull final EntityType entityType, @NotNull final UUID uuid) {
        super();
        Contract.requireArgNotNull("entityType", entityType);
        Contract.requireArgNotNull("uuid", uuid);
        this.entityType = entityType;
        this.uuid = uuid;
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + entityType.asString().hashCode();
        result = prime * result + uuid.hashCode();
        return result;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof AggregateRootUuid)) {
            return false;
        }
        final AggregateRootUuid other = (AggregateRootUuid) obj;
        if (!entityType.asString().equals(other.entityType.asString())) {
            return false;
        }
        return uuid.equals(other.uuid);
    }

    @Override
    public final int compareTo(final AggregateRootUuid other) {
        final int c = entityType.asString().compareTo(other.entityType.asString());
        if (c != 0) {
            return c;
        }
        return uuid.compareTo(other.uuid);
    }

    @Override
    public final String toString() {
        return uuid.toString();
    }

    @Override
    public final String asString() {
        return toString();
    }

    @Override
    public final String asTypedString() {
        return entityType + " " + uuid;
    }

    @Override
    public final EntityType getType() {
        return entityType;
    }

    @Override
    public final Class<UUID> getBaseType() {
        return UUID.class;
    }

    @Override
    public final UUID asBaseType() {
        return uuid;
    }

    /**
     * Verifies that a given string can be converted into the type.
     * 
     * @param value
     *            Value to validate.
     * 
     * @return Returns <code>true</code> if it's a valid type else <code>false</code>.
     */
    public static boolean isValid(final String value) {
        if (value == null) {
            return true;
        }
        if (value.length() != 36) {
            return false;
        }
        return PATTERN.matcher(value).matches();
    }

    /**
     * Verifies if the argument is valid and throws an exception if this is not the case.
     * 
     * @param name
     *            Name of the value for a possible error message.
     * @param value
     *            Value to check.
     * 
     * @throws ConstraintViolationException
     *             The value was not valid.
     */
    public static void requireArgValid(@NotNull final String name, @NotNull final String value) throws ConstraintViolationException {

        if (!isValid(value)) {
            throw new ConstraintViolationException("The argument '" + name + "' is not valid: '" + value + "'");
        }

    }

}
