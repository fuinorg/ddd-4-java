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

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The entity identifier path should contain a defined order and type of entries.
 * <p>
 * A path begins at an aggregate root and names the chain of children down to the thing it addresses, so
 * the shape is written the same way:
 * <pre>
 * &#64;ExpectedEntityIdPath({&#64;Segment(type = AnnualTransactionsId.class),
 *                        &#64;Segment(type = AccountTransactionId.class)})
 * </pre>
 * A step takes exactly one identifier unless it states a range - {@link Segment#min()} and
 * {@link Segment#max()} - which is what an entity containing another of its own kind needs.
 */
@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExpectedEntityIdPathValidator.class})
@Documented
public @interface ExpectedEntityIdPath {

    String message() default "ExpectedEntityIdPath validation failed: ${validatedValue}";

    /**
     * Expected path shape, as an ordered list of steps.
     *
     * @return Ordered list of expected segments.
     */
    Segment[] value();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
