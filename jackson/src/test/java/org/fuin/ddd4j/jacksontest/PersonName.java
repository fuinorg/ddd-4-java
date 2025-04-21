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
package org.fuin.ddd4j.jacksontest;

import jakarta.validation.constraints.NotNull;
import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.common.ValueObjectWithBaseType;
import org.fuin.objects4j.core.AbstractStringValueObject;

import javax.annotation.concurrent.Immutable;
import java.io.Serial;

/**
 * A person name.
 */
@Immutable
public final class PersonName extends AbstractStringValueObject implements ValueObjectWithBaseType<String> {

    @Serial
    private static final long serialVersionUID = 1000L;

    @NotNull
    private final String name;

    /**
     * Constructor with string.
     *
     * @param name
     *            Name.
     */
    public PersonName(@NotNull final String name) {
        super();
        Contract.requireArgNotNull("name", name);
        Contract.requireArgMinLength("name", name, 1);
        Contract.requireArgMaxLength("name", name, 100);
        this.name = name;
    }

    @Override
    public final String asBaseType() {
        return name;
    }

    @Override
    public final String toString() {
        return name;
    }

    /**
     * Returns the information if a given string is a valid name.
     *
     * @param value
     *            Value to check. A <code>null</code> value returns <code>true</code>.
     *
     * @return TRUE if it's a valid key, else FALSE.
     */
    public static boolean isValid(final String value) {
        if (value == null) {
            return true;
        }
        if (value.isEmpty() || value.length() > 100) {
            return false;
        }
        return true;
    }

    /**
     * Parses a name.
     *
     * @param value
     *            Value to convert. A <code>null</code> value returns <code>null</code>.
     *
     * @return Converted value.
     */
    public static PersonName valueOf(final String value) {
        if (value == null) {
            return null;
        }
        return new PersonName(value);
    }

}
