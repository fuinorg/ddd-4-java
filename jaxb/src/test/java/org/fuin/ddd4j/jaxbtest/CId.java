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
package org.fuin.ddd4j.jaxbtest;

import org.fuin.ddd4j.core.EntityId;
import org.fuin.ddd4j.core.EntityType;
import org.fuin.ddd4j.core.HasEntityTypeConstant;
import org.fuin.ddd4j.core.StringBasedEntityType;
import org.fuin.objects4j.common.HasPublicStaticIsValidMethod;
import org.fuin.objects4j.common.HasPublicStaticValueOfMethod;

import java.io.Serial;
import java.util.Objects;

@HasPublicStaticIsValidMethod
@HasPublicStaticValueOfMethod
@HasEntityTypeConstant
public class CId implements EntityId {

    @Serial
    private static final long serialVersionUID = 1L;

    public static final EntityType TYPE = new StringBasedEntityType("C");

    private final long id;

    public CId(final long id) {
        this.id = id;
    }

    @Override
    public EntityType getType() {
        return TYPE;
    }

    @Override
    public String asString() {
        return "" + id;
    }

    @Override
    public String asTypedString() {
        return getType() + " " + asString();
    }

    @Override
    public String toString() {
        return "CId [id=" + id + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CId cId = (CId) o;
        return id == cId.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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
            Long.parseLong(value);
            return true;
        } catch (final RuntimeException ex) {
            return false;
        }
    }

    /**
     * Parses an identifier.
     *
     * @param value
     *            Value to convert. A <code>null</code> value returns <code>null</code>.
     *
     * @return Converted value.
     */
    public static CId valueOf(final Long value) {
        if (value == null) {
            return null;
        }
        return new CId(value);
    }

    /**
     * Parses an identifier.
     *
     * @param value
     *            Value to convert. A <code>null</code> value returns <code>null</code>.
     *
     * @return Converted value.
     */
    public static CId valueOf(final String value) {
        if (value == null) {
            return null;
        }
        return new CId(Long.parseLong(value));
    }

}