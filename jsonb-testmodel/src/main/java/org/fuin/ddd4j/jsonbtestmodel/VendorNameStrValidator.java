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
package org.fuin.ddd4j.jsonbtestmodel;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.NotNull;
import org.fuin.objects4j.common.ConstraintViolationException;

/**
 * Check that a given string is a valid vendor name.
 */
public final class VendorNameStrValidator implements ConstraintValidator<VendorNameStr, String> {

    @Override
    public final void initialize(final VendorNameStr constraintAnnotation) {
    }

    @Override
    public final boolean isValid(final String value, final ConstraintValidatorContext context) {
        return isValid(value);
    }

    /**
     * Check that a given string is a well-formed user id.
     *
     * @param value
     *            Value to check.
     *
     * @return Returns <code>true</code> if it's a valid user id else <code>false</code> is returned.
     */
    public static boolean isValid(final String value) {
        if (value == null) {
            return true;
        }
        final String trimmed = value.trim();
        if ((trimmed.isEmpty()) || (trimmed.length() > 100)) {
            return false;
        }
        return true;
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
    public static void requireArgValid(@NotNull final String name, @NotNull final String value) throws ConstraintViolationException {
        if (!isValid(value)) {
            throw new ConstraintViolationException("The argument '" + name + "' is not valid: '" + value + "'");
        }
    }

}
