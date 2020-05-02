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

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.common.Immutable;
import org.fuin.objects4j.vo.AbstractStringValueObject;

/**
 * Entity type based on a string with a maximum length of 255 characters.
 */
@Immutable
public final class StringBasedEntityType extends AbstractStringValueObject implements EntityType {

    private static final long serialVersionUID = 1000L;

    @NotEmpty
    @Size(max = 255)
    private final String str;

    /**
     * Constructor with unique name to use.
     * 
     * @param str
     *            Type name of an aggregate that is unique within all types of the context
     */
    public StringBasedEntityType(@NotEmpty @Size(max = 255) final String str) {
        Contract.requireArgNotEmpty("str", str);
        Contract.requireArgMaxLength("str", str, 255);
        this.str = str;
    }

    @Override
    public final String asBaseType() {
        return str;
    }

    @Override
    public final String toString() {
        return str;
    }

}
