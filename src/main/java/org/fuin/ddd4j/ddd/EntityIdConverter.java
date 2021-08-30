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

import jakarta.json.bind.adapter.JsonbAdapter;
import javax.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import org.fuin.objects4j.common.ConstraintViolationException;
import org.fuin.objects4j.common.ThreadSafe;

/**
 * JAXB and JSON converter for an entity identifier.
 */
@ThreadSafe
public final class EntityIdConverter extends XmlAdapter<String, EntityId> implements JsonbAdapter<EntityId, String> {

    private final EntityIdFactory factory;

    /**
     * Constructor with factory.
     * 
     * @param factory
     *            Factory to use.
     */
    public EntityIdConverter(final EntityIdFactory factory) {
        super();
        if (factory == null) {
            throw new IllegalStateException("Factory cannot be null");
        }
        this.factory = factory;
    }

    /**
     * Verifies that the given value can be converted into a value object using the factory. A <code>null</code> parameter will return
     * <code>true</code>.
     * 
     * @param value
     *            Value to check.
     * 
     * @return <code>true</code> if the value can be converted, else <code>false</code>.
     */
    public final boolean isValid(final String value) {
        if (value == null) {
            return true;
        }
        final int p = value.indexOf(' ');
        if (p == -1) {
            return false;
        }
        final String type = value.substring(0, p);
        final String id = value.substring(p + 1);
        if (!factory.containsType(type)) {
            return false;
        }
        return factory.isValid(type, id);
    }

    /**
     * Verifies if the argument is valid and throws an exception if this is not the case.
     * 
     * @param name
     *            Name of the value for a possible error message.
     * @param value
     *            Value to check.
     * 
     * @throws ConstraintViolationException
     *             The value was not valid.
     */
    public final void requireArgValid(@NotNull final String name, @NotNull final String value) throws ConstraintViolationException {

        if (!isValid(value)) {
            throw new ConstraintViolationException("The argument '" + name + "' is not valid: '" + value + "'");
        }

    }

    /**
     * Converts a string into an entity identifier. A <code>null</code> parameter will return <code>null</code>.
     * 
     * @param value
     *            Representation of the entity identifier as string.
     * 
     * @return Value object.
     */
    public final EntityId toVO(final String value) {
        if (value == null) {
            return null;
        }
        final int p = value.indexOf(' ');
        if (p == -1) {
            // Error
            return null;
        }
        final String type = value.substring(0, p);
        final String id = value.substring(p + 1);
        return factory.createEntityId(type, id);
    }

    /**
     * Converts the value object into a String. A <code>null</code> parameter will return <code>null</code>.
     * 
     * @param value
     *            Value object.
     * 
     * @return Base type.
     */
    public final String fromVO(final EntityId value) {
        if (value == null) {
            return null;
        }
        return value.asTypedString();
    }

    // XML Adapter

    @Override
    public final String marshal(final EntityId value) throws Exception {
        return fromVO(value);
    }

    @Override
    public final EntityId unmarshal(final String value) throws Exception {
        return toVO(value);
    }
    // JSONB Adapter

    @Override
    public final String adaptToJson(final EntityId obj) throws Exception {
        return fromVO(obj);
    }

    @Override
    public final EntityId adaptFromJson(final String str) throws Exception {
        return toVO(str);
    }

}
