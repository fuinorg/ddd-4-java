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

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.fuin.objects4j.common.ConstraintViolationException;
import org.fuin.objects4j.common.Contract;
import jakarta.annotation.Nullable;
import org.fuin.objects4j.vo.AbstractIntegerValueObject;

import java.util.Scanner;

/**
 * Version of an aggregate.
 */
public final class AggregateVersion extends AbstractIntegerValueObject {

    private static final long serialVersionUID = 1000L;

    private Integer value;

    /**
     * Constructor with value.
     * 
     * @param version
     *            Version.
     */
    public AggregateVersion(@NotNull @Min(0) final Integer version) {
        super();
        Contract.requireArgNotNull("version", version);
        Contract.requireArgMin("version", version, 0);
        this.value = version;
    }

    @Override
    public final Integer asBaseType() {
        return value;
    }

    /**
     * Returns the information if a given integer is a valid version.
     * 
     * @param value
     *            Value to check. A <code>null</code> value returns <code>true</code>.
     * 
     * @return TRUE if it's a valid version, else FALSE.
     */
    public static boolean isValid(final Integer value) {
        if (value == null) {
            return true;
        }
        return value >= 0;
    }

    /**
     * Returns the information if a given string is a valid version.
     * 
     * @param value
     *            Value to check. A <code>null</code> value returns <code>true</code>.
     * 
     * @return TRUE if it's a valid version, else FALSE.
     */
    public static boolean isValid(final String value) {
        if (value == null) {
            return true;
        }
        try (Scanner scanner = new Scanner(value)) {
            if (!scanner.hasNextInt()) {
                return false;
            }
        }
        return isValid(Integer.valueOf(value));
    }

    /**
     * Parses a version identifier.
     * 
     * @param value
     *            Value to convert. A <code>null</code> value returns <code>null</code>.
     * 
     * @return Converted value.
     */
    public static AggregateVersion valueOf(final Integer value) {
        if (value == null) {
            return null;
        }
        return new AggregateVersion(value);
    }

    /**
     * Parses a version identifier.
     * 
     * @param value
     *            Value to convert. A <code>null</code> value returns <code>null</code>.
     * 
     * @return Converted value.
     */
    public static AggregateVersion valueOf(final String value) {
        if (value == null) {
            return null;
        }
        return valueOf(Integer.valueOf(value));
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
    public static void requireArgValid(@NotNull final String name, @Nullable final Integer value) throws ConstraintViolationException {

        if (!isValid(value)) {
            throw new ConstraintViolationException("The argument '" + name + "' is not valid: " + value);
        }

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
    public static void requireArgValid(@NotNull final String name, @Nullable final String value) throws ConstraintViolationException {

        if (!isValid(value)) {
            throw new ConstraintViolationException("The argument '" + name + "' is not valid: '" + value + "'");
        }

    }

}
