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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Scanner;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;

import org.fuin.objects4j.common.ConstraintViolationException;
import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.vo.ValueObjectWithBaseType;

/**
 * Integer based entity identifier. Valid values are {@value 1} to {@link Integer#MAX_VALUE}.
 */
public abstract class EntityIntegerId implements EntityId, Comparable<EntityIntegerId>, ValueObjectWithBaseType<Integer> {

    private static final long serialVersionUID = 1000L;

    private final EntityType entityType;

    private final Integer id;

    /**
     * Constructor with value.
     * 
     * @param entityType
     *            Entity type.
     * @param id
     *            Identifier base value.
     */
    public EntityIntegerId(@NotNull final EntityType entityType, @NotNull final Integer id) {
        super();
        Contract.requireArgNotNull("entityType", entityType);
        Contract.requireArgNotNull("id", id);
        Contract.requireArgMin("id", id, 1);
        this.entityType = entityType;
        this.id = id;
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + entityType.asString().hashCode();
        result = prime * result + id.hashCode();
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
        if (!(obj instanceof EntityIntegerId)) {
            return false;
        }
        final EntityIntegerId other = (EntityIntegerId) obj;
        if (!entityType.asString().equals(other.entityType.asString())) {
            return false;
        }
        return id.equals(other.id);
    }

    @Override
    public final int compareTo(final EntityIntegerId other) {
        final int c = entityType.asString().compareTo(other.entityType.asString());
        if (c != 0) {
            return c;
        }
        return id.compareTo(other.id);
    }

    @Override
    public final String toString() {
        return id.toString();
    }

    @Override
    public final String asString() {
        return toString();
    }

    @Override
    public final String asTypedString() {
        return entityType + " " + id;
    }

    @Override
    public final EntityType getType() {
        return entityType;
    }

    @Override
    public final Class<Integer> getBaseType() {
        return Integer.class;
    }

    @Override
    public final Integer asBaseType() {
        return id;
    }

    // CHECKSTYLE:OFF

    /**
     * Ensures that the string can be converted into the type.
     */
    @Target({ ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = { Validator.class })
    @Documented
    public static @interface EntityIntegerIdStr {

        String message() default "{org.fuin.ddd4j.ddd.EntityIntegerId.message}";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};

    }

    /**
     * Validates if a string is compliant with the type.
     */
    public static final class Validator implements ConstraintValidator<EntityIntegerIdStr, String> {

        @Override
        public final void initialize(final EntityIntegerIdStr annotation) {
            // Not used
        }

        @Override
        public final boolean isValid(final String value, final ConstraintValidatorContext context) {
            return EntityIntegerId.isValid(value);
        }

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
        try (final Scanner scanner = new Scanner(value)) {
            if (!scanner.hasNextInt()) {
                return false;
            }
        }
        return Integer.valueOf(value) > 0;
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

    // CHECKSTYLE:ON

}
