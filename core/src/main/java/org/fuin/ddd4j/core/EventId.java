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
package org.fuin.ddd4j.core;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.fuin.objects4j.common.AsStringCapable;
import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.common.HasPublicStaticIsValidMethod;
import org.fuin.objects4j.common.HasPublicStaticValueOfMethod;
import org.fuin.objects4j.core.AbstractUuidValueObject;
import org.fuin.objects4j.ui.Label;
import org.fuin.objects4j.ui.Prompt;
import org.fuin.objects4j.ui.ShortLabel;
import org.fuin.objects4j.ui.Tooltip;

import javax.annotation.concurrent.Immutable;
import java.io.Serial;
import java.util.UUID;

/**
 * Universal unique event identifier.
 */
@Immutable
@Label("Event Identifier")
@ShortLabel("EventID")
@Tooltip("Identifies an event universally unique")
@Prompt("bb05f34d-4eac-4f6a-b3c2-5c89269720f3")
@HasPublicStaticValueOfMethod
@HasPublicStaticIsValidMethod
public final class EventId extends AbstractUuidValueObject implements TechnicalId, AsStringCapable {

    @Serial
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
     * @param uuid UUID.
     */
    public EventId(@NotNull final UUID uuid) {
        super();
        Contract.requireArgNotNull("uuid", uuid);
        this.uuid = uuid;
    }

    @Override
    public UUID asBaseType() {
        return uuid;
    }

    @Override
    public String asString() {
        return uuid.toString();
    }

    @Override
    public String toString() {
        return uuid.toString();
    }

    /**
     * Converts a string into an entity identifier. A <code>null</code> parameter will return <code>null</code>.
     *
     * @param value Representation of the entity identifier as string.
     * @return Value object.
     */
    public static EventId valueOf(@Nullable final String value) {
        if (value == null) {
            return null;
        }
        return new EventId(UUID.fromString(value));
    }

}
