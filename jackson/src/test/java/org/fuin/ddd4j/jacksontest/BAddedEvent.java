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

import org.fuin.ddd4j.core.EntityIdPath;
import org.fuin.ddd4j.core.EventType;
import org.fuin.ddd4j.jackson.AbstractDomainEvent;
import org.fuin.esc.api.HasSerializedDataTypeConstant;
import org.fuin.esc.api.SerializedDataType;
import org.fuin.esc.api.TypeName;

import java.io.Serial;

@HasSerializedDataTypeConstant
public class BAddedEvent extends AbstractDomainEvent<AId> {

    @Serial
    private static final long serialVersionUID = 1L;

    /** Unique name of the event. */
    public static final TypeName TYPE = new TypeName("BAddedEvent");

    /** Unique name of the serialized event. */
    public static final SerializedDataType SER_TYPE = new SerializedDataType(TYPE.asBaseType());

    private static final EventType EVENT_TYPE = new EventType(TYPE.asBaseType());

    private final BId bid;

    public BAddedEvent(final AId aid, final BId bid) {
        super(new EntityIdPath(aid));
        this.bid = bid;
    }

    @Override
    public EventType getEventType() {
        return EVENT_TYPE;
    }

    public BId getBId() {
        return bid;
    }

}
