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

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.fuin.objects4j.vo.ValueObjectWithBaseType;

/**
 * Universal unique event identifier.
 */
@XmlJavaTypeAdapter(EventIdConverter.class)
public class EventId extends AbstractUUIDVO implements
        ValueObjectWithBaseType<String>, TechnicalId {

    private static final long serialVersionUID = 1000L;

    /**
     * Default constructor.
     */
    public EventId() {
        super();
    }

    /**
     * Constructor with UUID.
     * 
     * @param uuid
     *            UUID.
     */
    public EventId(@NotNull final UUID uuid) {
        super(uuid);
    }

    @Override
    public final String asBaseType() {
        return asString();
    }

    @Override
    public final Class<String> getBaseType() {
        return String.class;
    }

}
