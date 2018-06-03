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
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import javax.annotation.concurrent.ThreadSafe;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.fuin.objects4j.vo.AbstractValueObjectConverter;

/**
 * JAXB and JPA converter for an entity identifier path.
 */
@ThreadSafe
@Converter(autoApply = true)
public final class EntityIdPathConverter extends AbstractValueObjectConverter<String, EntityIdPath>
        implements AttributeConverter<EntityIdPath, String> {

    private final EntityIdFactory factory;

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
        this.factory = factory;
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
        final List<Entry> entryList = entries(value);
        if (entryList.size() == 0) {
            return false;
        }
        for (final Entry entry : entryList) {
            if (!factory.containsType(entry.type)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public final EntityIdPath toVO(final String value) {
        if (value == null) {
            return null;
        }
        final List<Entry> entryList = entries(value);
        if (entryList.size() == 0) {
            throw new IllegalArgumentException("Invalid entity path: '" + value + "'");
        }
        final List<EntityId> ids = new ArrayList<EntityId>();
        for (final Entry entry : entryList) {
            ids.add(factory.createEntityId(entry.type, entry.id));
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

    private List<Entry> entries(final String value) {
        final List<Entry> list = new ArrayList<Entry>();
        final StringTokenizer tok = new StringTokenizer(value, EntityIdPath.PATH_SEPARATOR);
        while (tok.hasMoreTokens()) {
            final String str = tok.nextToken();
            final int p = str.indexOf(' ');
            if (p == -1) {
                // Error
                return Collections.emptyList();
            }
            final String type = str.substring(0, p);
            final String id = str.substring(p + 1);
            list.add(new Entry(type, id));
        }
        return list;
    }

    /**
     * Maps type and identifer as pair.
     */
    private static final class Entry {

        private String type;
        private String id;

        public Entry(final String type, final String id) {
            this.type = type;
            this.id = id;
        }

    }

}
