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
package org.fuin.ddd4j.jsonb;

import jakarta.json.bind.adapter.JsonbAdapter;
import org.fuin.ddd4j.core.EntityId;
import org.fuin.ddd4j.core.EntityIdFactory;
import javax.annotation.concurrent.ThreadSafe;
import org.fuin.objects4j.common.ValueOfCapable;

/**
 * JAXB and JSON converter for an entity identifier.
 */
@ThreadSafe
public final class EntityIdJsonbAdapter implements JsonbAdapter<EntityId, String> {

    private final ValueOfCapable<EntityId> vop;

    /**
     * Constructor with factory.
     *
     * @param factory Factory to use.
     */
    public EntityIdJsonbAdapter(final EntityIdFactory factory) {
        vop = str -> EntityId.valueOf(factory, str);
    }

    @Override
    public EntityId adaptFromJson(String value) {
        return this.vop.valueOf(value);
    }

    @Override
    public String adaptToJson(EntityId value) {
        return value == null ? null : value.asTypedString();
    }

}
