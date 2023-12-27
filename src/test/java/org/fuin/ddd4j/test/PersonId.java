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

import jakarta.validation.constraints.NotNull;
import org.fuin.ddd4j.ddd.EntityId;
import org.fuin.ddd4j.ddd.EntityType;
import org.fuin.ddd4j.ddd.StringBasedEntityType;
import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.common.Immutable;
import org.fuin.objects4j.vo.AbstractIntegerValueObject;

/**
 * Unique identifier of a person.
 */
@Immutable
public final class PersonId extends AbstractIntegerValueObject implements EntityId {

    private static final long serialVersionUID = 1000L;

    /** Type of entity this identifier represents. */
    public static final EntityType ENTITY_TYPE = new StringBasedEntityType("Person");

    private Integer value;

    /**
     * Default constructor.
     */
    public PersonId() {
        super();
    }

    /**
     * Constructor with ID.
     * 
     * @param id
     *            ID.
     */
    public PersonId(@NotNull final Integer id) {
        super();
        Contract.requireArgNotNull("id", id);
        this.value = id;
    }

    @Override
    public final EntityType getType() {
        return ENTITY_TYPE;
    }

    @Override
    public final String asTypedString() {
        return getType() + " " + asString();
    }

    @Override
    public final Integer asBaseType() {
        return value;
    }

    @Override
    public final String asString() {
        return "" + value;
    }

    @Override
    public final String toString() {
        return asString();
    }

    /**
     * Returns the information if a given string is a valid person identifier.
     * 
     * @param value
     *            Value to check. A <code>null</code> value returns <code>true</code>.
     * 
     * @return TRUE if it's a valid ID, else FALSE.
     */
    public static boolean isValid(final String value) {
        if (value == null) {
            return true;
        }
        try {
            // TODO Use regular expression - Exception is no good style
            Integer.parseInt(value);
            return true;
        } catch (final RuntimeException ex) {
            return false;
        }
    }

    /**
     * Parses a person identifier.
     * 
     * @param value
     *            Value to convert. A <code>null</code> value returns <code>null</code>.
     * 
     * @return Converted value.
     */
    public static PersonId valueOf(final Integer value) {
        if (value == null) {
            return null;
        }
        return new PersonId(value);
    }

}
