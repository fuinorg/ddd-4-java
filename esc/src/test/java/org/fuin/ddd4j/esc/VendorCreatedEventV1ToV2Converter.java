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
package org.fuin.ddd4j.esc;

import org.fuin.ddd4j.jsonbtestmodel.VendorCreatedEvent;
import org.fuin.ddd4j.jsonbtestmodel.VendorId;
import org.fuin.ddd4j.jsonbtestmodel.VendorKey;
import org.fuin.ddd4j.jsonbtestmodel.VendorName;
import org.fuin.ddd4j.jsonbtestmodel.VendorRef;
import org.fuin.esc.api.Converter;
import org.fuin.utils4j.TestOmitted;

import java.util.UUID;

/**
 * Up-casts a stored {@link VendorCreatedEventV1} (flat strings) to the current
 * {@link VendorCreatedEvent} (v2) whose payload is the structured {@link VendorRef} of typed value objects.
 * Registered under {@link VendorCreatedEvent#SER_TYPE} for the version step {@code "1" -> "2"}.
 */
@TestOmitted("Only a test fixture")
public final class VendorCreatedEventV1ToV2Converter implements Converter<VendorCreatedEventV1, VendorCreatedEvent> {

    @Override
    public Class<VendorCreatedEventV1> getSourceType() {
        return VendorCreatedEventV1.class;
    }

    @Override
    public Class<VendorCreatedEvent> getTargetType() {
        return VendorCreatedEvent.class;
    }

    @Override
    public VendorCreatedEvent convert(final VendorCreatedEventV1 source) {
        final VendorId id = new VendorId(UUID.fromString(source.getId()));
        final VendorKey key = new VendorKey(source.getKey());
        final VendorName name = new VendorName(source.getName());
        return new VendorCreatedEvent(new VendorRef(id, key, name));
    }

}
