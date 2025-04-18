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
package org.fuin.ddd4j.esc;

import jakarta.validation.constraints.NotNull;
import org.fuin.ddd4j.core.AggregateRootId;
import org.fuin.ddd4j.core.EntityType;
import org.fuin.esc.api.StreamId;
import org.fuin.objects4j.core.KeyValue;

import javax.annotation.concurrent.Immutable;
import java.io.Serial;
import java.util.List;
import java.util.Objects;

/**
 * Unique name of an aggregate stream. Equals and has code are based on the {@link #asString()} method.
 */
@Immutable
public final class AggregateStreamId implements StreamId {

    @Serial
    private static final long serialVersionUID = 1000L;

    @NotNull
    private final EntityType type;

    @NotNull
    private final String paramName;

    @NotNull
    private final AggregateRootId paramValue;

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
        this.type = Objects.requireNonNull(type, "type==null");
        this.paramName = Objects.requireNonNull(paramName, "paramName==null");
        this.paramValue = Objects.requireNonNull(paramValue, "paramValue==null");
    }

    @Override
    public String getName() {
        return type.asString();
    }

    @Override
    public boolean isProjection() {
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getSingleParamValue() {
        return (T) paramValue.asString();
    }

    @Override
    public List<KeyValue> getParameters() {
        if (params == null) {
            params = List.of(new KeyValue(paramName, paramValue.asString()));
        }
        return params;
    }

    @Override
    public String asString() {
        return type + "-" + paramValue.asString();
    }

    @Override
    public int hashCode() {
        return asString().hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
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
    public String toString() {
        return asString();
    }

}
