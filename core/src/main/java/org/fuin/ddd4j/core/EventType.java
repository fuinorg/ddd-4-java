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

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.core.AbstractStringValueObject;

import javax.annotation.concurrent.Immutable;
import java.io.Serial;

/**
 * Identifies an event type within an aggregate type.
 */
@Immutable
public class EventType extends AbstractStringValueObject {

    @Serial
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
    public EventType(@NotEmpty @Size(max = 255) final String str) {
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
        return asBaseType();
    }

}
