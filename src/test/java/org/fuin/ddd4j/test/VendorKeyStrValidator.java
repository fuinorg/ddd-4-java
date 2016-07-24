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

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.NotNull;

import org.fuin.objects4j.common.ConstraintViolationException;

/**
 * Check that a given string is a well-formed user id.
 */
public final class VendorKeyStrValidator implements
        ConstraintValidator<VendorKeyStr, String> {

    private static final Pattern PATTERN = Pattern
            .compile("V[0-9][0-9][0-9][0-9][0-9]");

    @Override
    public final void initialize(final VendorKeyStr constraintAnnotation) {
    }

    @Override
    public final boolean isValid(final String value,
            final ConstraintValidatorContext context) {
        return isValid(value);
    }

    /**
     * Check that a given string is a well-formed user id.
     * 
     * @param value
     *            Value to check.
     * 
     * @return Returns <code>true</code> if it's a valid user id else
     *         <code>false</code> is returned.
     */
    public static final boolean isValid(final String value) {
        if (value == null) {
            return true;
        }
        if (value.length() != 6) {
            return false;
        }
        return PATTERN.matcher(value.toString()).matches();
    }

    /**
     * Parses the argument and throws an exception if it's not valid.
     * 
     * @param name
     *            Name of the value for a possible error message.
     * @param value
     *            Value to check.
     * 
     * @throws ConstraintViolationException
     *             The value was not valid.
     */
    // CHECKSTYLE:OFF:RedundantThrows
    public static void requireArgValid(@NotNull final String name,
            @NotNull final String value) throws ConstraintViolationException {
        // CHECKSTYLE:ON
        if (!isValid(value)) {
            throw new ConstraintViolationException("The argument '" + name
                    + "' is not valid: '" + value + "'");
        }
    }

}
