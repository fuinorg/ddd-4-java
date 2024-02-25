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
package org.fuin.ddd4j.jaxb;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import org.fuin.ddd4j.core.EntityId;
import org.fuin.ddd4j.core.EntityIdFactory;
import javax.annotation.concurrent.ThreadSafe;
import org.fuin.objects4j.common.ValueOfCapable;

/**
 * JAXB adapter for an entity identifier.
 */
@ThreadSafe
public final class EntityIdXmlAdapter extends XmlAdapter<String, EntityId> {

    private final ValueOfCapable<EntityId> vop;

    /**
     * Constructor with factory.
     *
     * @param factory Factory to use.
     */
    public EntityIdXmlAdapter(final EntityIdFactory factory) {
        vop = str -> EntityId.valueOf(factory, str);
    }

    public final EntityId unmarshal(String value) {
        return this.vop.valueOf(value);
    }

    public final String marshal(EntityId value) {
        return value == null ? null : value.asTypedString();
    }


}
