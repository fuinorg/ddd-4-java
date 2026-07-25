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

import org.fuin.objects4j.common.AsStringCapable;
import org.fuin.objects4j.common.HasPublicStaticValueOfMethod;
import org.fuin.objects4j.common.ValueObjectWithBaseType;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Value object wrapping a long that has a static factory method for its base type <b>and</b> one for a
 * string. It is {@link AsStringCapable}, which must <b>not</b> make it a string based value object -
 * the base type decides, so it stays a JSON number.
 */
@HasPublicStaticValueOfMethod(param = Long.class)
@HasPublicStaticValueOfMethod
public final class Quantity implements ValueObjectWithBaseType<Long>, AsStringCapable, Serializable {

    @Serial
    private static final long serialVersionUID = 1000L;

    private final Long value;

    public Quantity(final Long value) {
        this.value = Objects.requireNonNull(value, "value==null");
    }

    public static Quantity valueOf(final Long value) {
        return value == null ? null : new Quantity(value);
    }

    public static Quantity valueOf(final String value) {
        return value == null ? null : new Quantity(Long.valueOf(value));
    }

    @Override
    public Class<Long> getBaseType() {
        return Long.class;
    }

    @Override
    public Long asBaseType() {
        return value;
    }

    @Override
    public String asString() {
        return value.toString();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Quantity other)) {
            return false;
        }
        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return asString();
    }

}
