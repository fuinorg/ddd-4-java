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
package org.fuin.ddd4j.jpa;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.fuin.ddd4j.core.AggregateVersion;

import javax.annotation.concurrent.ThreadSafe;

/**
 * Converts a {@link AggregateVersion}.
 */
@ThreadSafe
@Converter(autoApply = true)
public final class AggregateVersionAttributeConverter implements AttributeConverter<AggregateVersion, Integer> {

    @Override
    public Integer convertToDatabaseColumn(AggregateVersion value) {
        if (value == null) {
            return null;
        }
        return value.asBaseType();
    }

    @Override
    public AggregateVersion convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return AggregateVersion.valueOf(dbData);
    }

}
