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
package org.fuin.ddd4j.esrepo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.fuin.ddd4j.ddd.AggregateRootId;
import org.fuin.ddd4j.ddd.EntityType;
import org.fuin.esc.api.StreamId;
import org.fuin.objects4j.common.Immutable;
import org.fuin.objects4j.vo.KeyValue;

/**
 * Unique name of an aggregate stream. Equals and has code are based on the {@link #asString()} method.
 */
@Immutable
public final class AggregateStreamId implements StreamId {

    private static final long serialVersionUID = 1000L;

    private EntityType type;

    private String paramName;

    private AggregateRootId paramValue;

    private transient List<KeyValue> params;

    /**
     * Constructor with type and id.
     * 
     * @param type
     *            Aggregate type.
     * @param paramName
     *            Parameter name.
     * @param paramValue
     *            Aggregate id.
     */
    public AggregateStreamId(final EntityType type, final String paramName, final AggregateRootId paramValue) {
        super();
        this.type = type;
        this.paramName = paramName;
        this.paramValue = paramValue;
    }

    @Override
    public final String getName() {
        return type.asString();
    }

    @Override
    public final boolean isProjection() {
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <T> T getSingleParamValue() {
        return (T) paramValue.asString();
    }

    @Override
    public final List<KeyValue> getParameters() {
        if (params == null) {
            final List<KeyValue> list = new ArrayList<KeyValue>();
            list.add(new KeyValue(paramName, paramValue.asString()));
            params = Collections.unmodifiableList(list);
        }
        return params;
    }

    @Override
    public final String asString() {
        return type + "-" + paramValue.asString();
    }

    @Override
    public final int hashCode() {
        return asString().hashCode();
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AggregateStreamId other = (AggregateStreamId) obj;
        return asString().equals(other.asString());
    }

    @Override
    public final String toString() {
        return asString();
    }

}
