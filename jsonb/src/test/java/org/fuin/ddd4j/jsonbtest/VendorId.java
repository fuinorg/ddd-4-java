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
package org.fuin.ddd4j.jsonbtest;

import jakarta.validation.constraints.NotNull;
import org.fuin.ddd4j.core.AggregateRootId;
import org.fuin.ddd4j.core.EntityType;
import org.fuin.ddd4j.core.HasEntityTypeConstant;
import org.fuin.ddd4j.core.StringBasedEntityType;
import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.common.HasPublicStaticValueOfMethod;
import org.fuin.objects4j.common.ValueObjectWithBaseType;
import org.fuin.objects4j.core.AbstractUuidValueObject;

import javax.annotation.concurrent.Immutable;
import java.io.Serial;
import java.util.UUID;

/**
 * Unique identifier of a customer.
 */
@Immutable
@HasPublicStaticValueOfMethod
@HasEntityTypeConstant
public final class VendorId extends AbstractUuidValueObject implements AggregateRootId, ValueObjectWithBaseType<UUID> {

    @Serial
    private static final long serialVersionUID = 1000L;

    /** Type of entity this identifier represents. */
    public static final EntityType TYPE = new StringBasedEntityType("Vendor");

    private final UUID uuid;

    /**
     * Default constructor.
     */
    public VendorId() {
        super();
        uuid = UUID.randomUUID();
    }

    /**
     * Constructor with UUID.
     *
     * @param uuid
     *            UUID.
     */
    public VendorId(@NotNull final UUID uuid) {
        super();
        Contract.requireArgNotNull("uuid", uuid);
        this.uuid = uuid;
    }

    @Override
    public final UUID asBaseType() {
        return uuid;
    }

    @Override
    public final EntityType getType() {
        return TYPE;
    }

    @Override
    public final String asTypedString() {
        return getType() + " " + asString();
    }

    @Override
    public final String asString() {
        return uuid.toString();
    }

    @Override
    public final String toString() {
        return uuid.toString();
    }

    /**
     * Parses an identifier from a String.
     *
     * @param value
     *            Value to convert. A <code>null</code> value returns <code>null</code>.
     *
     * @return Converted value.
     */
    public static VendorId valueOf(final String value) {
        if (value == null) {
            return null;
        }
        return new VendorId(UUID.fromString(value));
    }

    /**
     * Parses an identifier from a UUID.
     *
     * @param value
     *            Value to convert. A <code>null</code> value returns <code>null</code>.
     *
     * @return Converted value.
     */
    public static VendorId valueOf(final UUID value) {
        if (value == null) {
            return null;
        }
        return new VendorId(value);
    }

}
