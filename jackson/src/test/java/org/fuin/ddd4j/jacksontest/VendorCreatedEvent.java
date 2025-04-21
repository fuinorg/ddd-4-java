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

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import org.fuin.ddd4j.core.EntityIdPath;
import org.fuin.ddd4j.core.EventType;
import org.fuin.ddd4j.jackson.AbstractDomainEvent;
import org.fuin.esc.api.HasSerializedDataTypeConstant;
import org.fuin.esc.api.SerializedDataType;

import java.io.Serial;

/**
 * A vendor entity was created.
 */
@HasSerializedDataTypeConstant
public final class VendorCreatedEvent extends AbstractDomainEvent<VendorId> {

    @Serial
    private static final long serialVersionUID = 1000L;

    /** Unique name of the event used to store it - Should never change. */
    public static final EventType TYPE = new EventType(VendorCreatedEvent.class.getSimpleName());

    /** Unique name of the serialized event. */
    public static final SerializedDataType SER_TYPE = new SerializedDataType(TYPE.asBaseType());

    @JsonProperty("vendor")
    private VendorRef vendorRef;

    /**
     * Default constructor only for deserialization.
     */
    protected VendorCreatedEvent() {
        super();
    }

    /**
     * Constructor with event data.
     *
     * @param vendorRef
     *            Vendor reference.
     */
    public VendorCreatedEvent(@NotNull final VendorRef vendorRef) {
        super(new EntityIdPath(vendorRef.getId()));
        this.vendorRef = vendorRef;
    }

    @Override
    public final EventType getEventType() {
        return TYPE;
    }

    /**
     * Returns the vendor reference.
     *
     * @return Vendor reference.
     */
    @NotNull
    public final VendorRef getVendorRef() {
        return vendorRef;
    }

    @Override
    public final String toString() {
        return "Created vendor '" + vendorRef + "'";
    }

}
