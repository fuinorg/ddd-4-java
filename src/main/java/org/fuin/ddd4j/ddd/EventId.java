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

import javax.annotation.concurrent.Immutable;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.ui.Label;
import org.fuin.objects4j.ui.Prompt;
import org.fuin.objects4j.ui.ShortLabel;
import org.fuin.objects4j.ui.Tooltip;
import org.fuin.objects4j.vo.AbstractUuidValueObject;

/**
 * Universal unique event identifier.
 */
@Immutable
@Label("Event Identifier")
@ShortLabel("EventID")
@Tooltip("Identifies an event universally unique")
@Prompt("bb05f34d-4eac-4f6a-b3c2-5c89269720f3")
@XmlJavaTypeAdapter(EventIdConverter.class)
public class EventId extends AbstractUuidValueObject implements TechnicalId {

    private static final long serialVersionUID = 1000L;

    private final UUID uuid;

    /**
     * Default constructor.
     */
    public EventId() {
        super();
        uuid = UUID.randomUUID();
    }

    /**
     * Constructor with UUID.
     * 
     * @param uuid
     *            UUID.
     */
    public EventId(@NotNull final UUID uuid) {
        super();
        Contract.requireArgNotNull("uuid", uuid);
        this.uuid = uuid;
    }

    @Override
    public final UUID asBaseType() {
        return uuid;
    }

    @Override
    public final String toString() {
        return uuid.toString();
    }

}
