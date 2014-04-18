/**
 * Copyright (C) 2013 Future Invent Informationsmanagement GmbH. All rights
 * reserved. <http://www.fuin.org/>
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
 * along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fuin.ddd4j.test;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

//CHECKSTYLE:OFF
/**
 * The string has to be a valid vendor ID like "V00000".
 * <ul>
 * <li>Exact 6 characters in length</li>
 * <li>First character is a 'V'</li>
 * <li>Rest of the characters is numeric (0-9)</li>
 * </ul>
 */
@Target({ ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD,
		ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { VendorKeyStrValidator.class })
@Documented
public @interface VendorKeyStr {

	String message() default "{org.fuin.ddd4j.ddd.VendorKeyStr.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
// CHECKSTYLE:ON
