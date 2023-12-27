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

import jakarta.persistence.Converter;
import org.fuin.objects4j.common.ThreadSafe;
import org.fuin.objects4j.vo.AbstractValueObjectConverter;

/**
 * Converts an aggregate version into an integer and back (JAXB and JPA).
 */
@ThreadSafe
@Converter(autoApply = true)
public final class AggregateVersionConverter extends AbstractValueObjectConverter<Integer, AggregateVersion> {

    @Override
    public final Class<Integer> getBaseTypeClass() {
        return Integer.class;
    }

    @Override
    public final Class<AggregateVersion> getValueObjectClass() {
        return AggregateVersion.class;
    }

    @Override
    public final boolean isValid(final Integer value) {
        return AggregateVersion.isValid(value);
    }

    @Override
    public final AggregateVersion toVO(final Integer value) {
        return AggregateVersion.valueOf(value);
    }

    @Override
    public final Integer fromVO(final AggregateVersion value) {
        return value.asBaseType();
    }

}
