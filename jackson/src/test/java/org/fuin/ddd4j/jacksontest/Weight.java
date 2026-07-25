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

import org.fuin.objects4j.common.ValueObjectWithBaseType;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Value object wrapping a decimal that offers <b>only a public constructor</b> - no static
 * {@code valueOf}. Some generated value objects look exactly like this, so the scan has to fall back
 * to the constructor.
 */
public final class Weight implements ValueObjectWithBaseType<BigDecimal>, Serializable {

    @Serial
    private static final long serialVersionUID = 1000L;

    private final BigDecimal value;

    public Weight(final BigDecimal value) {
        this.value = Objects.requireNonNull(value, "value==null");
    }

    @Override
    public Class<BigDecimal> getBaseType() {
        return BigDecimal.class;
    }

    @Override
    public BigDecimal asBaseType() {
        return value;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Weight other)) {
            return false;
        }
        return value.compareTo(other.value) == 0;
    }

    @Override
    public int hashCode() {
        return value.stripTrailingZeros().hashCode();
    }

    @Override
    public String toString() {
        return value.toPlainString();
    }

}
