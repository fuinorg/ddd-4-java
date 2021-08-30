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

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import javax.validation.constraints.NotNull;

import org.fuin.objects4j.common.ConstraintViolationException;
import org.fuin.objects4j.common.ThreadSafe;
import org.fuin.objects4j.vo.AbstractValueObjectConverter;

/**
 * JAXB and JPA converter for an entity identifier path.
 */
@ThreadSafe
@Converter(autoApply = true)
public final class EntityIdPathConverter extends AbstractValueObjectConverter<String, EntityIdPath>
        implements AttributeConverter<EntityIdPath, String> {

    private final EntityIdConverter converter;

    /**
     * Constructor with factory.
     * 
     * @param factory
     *            Factory to use.
     */
    public EntityIdPathConverter(final EntityIdFactory factory) {
        super();
        if (factory == null) {
            throw new IllegalStateException("Factory cannot be null");
        }
        this.converter = new EntityIdConverter(factory);
    }

    @Override
    public final Class<String> getBaseTypeClass() {
        return String.class;
    }

    @Override
    public final Class<EntityIdPath> getValueObjectClass() {
        return EntityIdPath.class;
    }

    @Override
    public final boolean isValid(final String value) {
        if (value == null) {
            return true;
        }
        final List<String> entryList = entries(value);
        if (entryList.isEmpty()) {
            return false;
        }
        for (final String entry : entryList) {
            if (!converter.isValid(entry)) {
                return false;
            }
        }
        return true;
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

    @Override
    public final EntityIdPath toVO(final String value) {
        if (value == null) {
            return null;
        }
        final List<String> entryList = entries(value);
        if (entryList.isEmpty()) {
            throw new IllegalArgumentException("Invalid entity path: '" + value + "'");
        }
        final List<EntityId> ids = new ArrayList<>();
        for (final String entry : entryList) {
            ids.add(converter.toVO(entry));
        }
        return new EntityIdPath(ids);
    }

    @Override
    public final String fromVO(final EntityIdPath value) {
        if (value == null) {
            return null;
        }
        return value.asString();
    }

    private List<String> entries(final String value) {
        final List<String> list = new ArrayList<>();
        final StringTokenizer tok = new StringTokenizer(value, EntityIdPath.PATH_SEPARATOR);
        while (tok.hasMoreTokens()) {
            final String str = tok.nextToken();
            list.add(str);
        }
        return list;
    }

}
