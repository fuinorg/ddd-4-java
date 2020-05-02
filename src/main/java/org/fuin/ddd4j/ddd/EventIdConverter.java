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

import java.util.UUID;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.fuin.objects4j.common.ThreadSafe;
import org.fuin.objects4j.vo.AbstractValueObjectConverter;

/**
 * JAXB and JPA converter for an entity identifier.
 */
@ThreadSafe
@Converter(autoApply = true)
public final class EventIdConverter extends AbstractValueObjectConverter<UUID, EventId> implements AttributeConverter<EventId, UUID> {

    @Override
    public Class<EventId> getValueObjectClass() {
        return EventId.class;
    }

    @Override
    public final EventId toVO(final UUID value) {
        return new EventId(value);
    }

    @Override
    public Class<UUID> getBaseTypeClass() {
        return UUID.class;
    }

    @Override
    public final boolean isValid(final UUID value) {
        return true;
    }

    @Override
    public final UUID fromVO(final EventId value) {
        if (value == null) {
            return null;
        }
        return value.asBaseType();
    }

}
