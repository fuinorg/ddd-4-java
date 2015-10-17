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
package org.fuin.ddd4j.test;

import java.util.UUID;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.fuin.ddd4j.ddd.AbstractUUIDVO;
import org.fuin.ddd4j.ddd.AggregateRootId;
import org.fuin.ddd4j.ddd.EntityType;
import org.fuin.ddd4j.ddd.StringBasedEntityType;
import org.fuin.objects4j.common.Immutable;
import org.fuin.objects4j.vo.UUIDStrValidator;
import org.fuin.objects4j.vo.ValueObjectWithBaseType;

/**
 * Unique identifier of a customer.
 */
@Immutable
@XmlJavaTypeAdapter(VendorIdConverter.class)
public final class VendorId extends AbstractUUIDVO implements AggregateRootId,
        ValueObjectWithBaseType<String> {

    private static final long serialVersionUID = 1000L;

    /** Type of entity this identifier represents. */
    public static final EntityType ENTITY_TYPE = new StringBasedEntityType(
            "Vendor");

    /**
     * Default constructor.
     */
    public VendorId() {
        super();
    }

    /**
     * Constructor with UUID.
     * 
     * @param uuid
     *            UUID.
     */
    public VendorId(@NotNull final UUID uuid) {
        super(uuid);
    }

    @Override
    public final Class<String> getBaseType() {
        return String.class;
    }

    @Override
    public final String asBaseType() {
        return asString();
    }

    @Override
    public final EntityType getType() {
        return ENTITY_TYPE;
    }

    @Override
    public final String asTypedString() {
        return getType() + " " + asString();
    }

    /**
     * Returns the information if a given string is a valid vendor identifier.
     * 
     * @param value
     *            Value to check. A <code>null</code> value returns
     *            <code>true</code>.
     * 
     * @return TRUE if it's a valid ID, else FALSE.
     */
    public static boolean isValid(final String value) {
        return UUIDStrValidator.isValid(value);
    }

    /**
     * Parses a vendor identifier.
     * 
     * @param value
     *            Value to convert. A <code>null</code> value returns
     *            <code>null</code>.
     * 
     * @return Converted value.
     */
    public static VendorId valueOf(final String value) {
        if (value == null) {
            return null;
        }
        return new VendorId(UUID.fromString(value));
    }

}
