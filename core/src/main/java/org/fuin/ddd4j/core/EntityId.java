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
import org.fuin.objects4j.common.AsStringCapable;
import org.fuin.objects4j.common.ConstraintViolationException;

/**
 * Identifies an entity within all entities of the same type.
 */
public interface EntityId extends TechnicalId, AsStringCapable {

    /**
     * Returns the type represented by this identifier.
     *
     * @return Type of entity.
     */
    EntityType getType();

    /**
     * Returns the entity identifier as string.
     *
     * @return Entity identifier.
     */
    String asString();

    /**
     * Returns the entity identifier as string with type and identifier.
     *
     * @return Type and identifier.
     */
    String asTypedString();


    /**
     * Verifies that the given value can be converted into a value object using the factory.
     * A <code>null</code> parameter will return <code>true</code>.
     *
     * @param factory
     *            Factory used to create enitity identifiers.
     * @param value
     *            Value to check.
     *
     * @return <code>true</code> if the value can be converted, else <code>false</code>.
     */
    static boolean isValid(@NotNull final EntityIdFactory factory, @Nullable final String value) {
        if (value == null) {
            return true;
        }
        final int p = value.indexOf(' ');
        if (p == -1) {
            return false;
        }
        final String type = value.substring(0, p);
        final String id = value.substring(p + 1);
        if (!factory.containsType(type)) {
            return false;
        }
        return factory.isValid(type, id);
    }

    /**
     * Verifies if the argument is valid and throws an exception if this is not the case.
     *
     * @param factory
     *            Factory used to create enitity identifiers.
     * @param name
     *            Name of the value for a possible error message.
     * @param value
     *            Value to check.
     *
     * @throws ConstraintViolationException
     *             The value was not valid.
     */
    static void requireArgValid(@NotNull final EntityIdFactory factory,
                                @NotNull final String name,
                                @Nullable final String value) throws ConstraintViolationException {

        if (!isValid(factory, value)) {
            throw new ConstraintViolationException("The argument '" + name + "' is not valid: '" + value + "'");
        }

    }

    /**
     * Converts a string into an entity identifier. A <code>null</code> parameter will return <code>null</code>.
     *
     * @param factory
     *            Factory used to create enitity identifiers.
     * @param value
     *            Representation of the entity identifier as string.
     *
     * @return Value object.
     */
    static EntityId valueOf(@NotNull final EntityIdFactory factory, @Nullable final String value) {
        if (value == null) {
            return null;
        }
        final int p = value.indexOf(' ');
        if (p == -1) {
            // Error
            return null;
        }
        final String type = value.substring(0, p);
        final String id = value.substring(p + 1);
        return factory.createEntityId(type, id);
    }

}
