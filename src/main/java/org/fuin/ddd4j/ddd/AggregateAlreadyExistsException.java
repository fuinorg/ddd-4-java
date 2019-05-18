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

import static org.fuin.ddd4j.ddd.Ddd4JUtils.SHORT_ID_PREFIX;

import javax.json.bind.annotation.JsonbProperty;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.fuin.objects4j.common.AbstractJaxbMarshallableException;
import org.fuin.objects4j.common.ExceptionShortIdentifable;

/**
 * An aggregate already exists when trying to create it.
 */
@XmlRootElement(name = "aggregate-already-exists-exception")
public final class AggregateAlreadyExistsException extends AbstractJaxbMarshallableException implements ExceptionShortIdentifable {

    private static final long serialVersionUID = 1L;

    @JsonbProperty("sid")
    @XmlElement(name = "sid")
    private String sid;

    @JsonbProperty("aggregate-type")
    @XmlElement(name = "aggregate-type")
    private String aggregateType;

    @JsonbProperty("aggregate-id")
    @XmlElement(name = "aggregate-id")
    private String aggregateId;

    @JsonbProperty("version")
    @XmlElement(name = "version")
    private int version;

    /**
     * JAX-B constructor.
     */
    protected AggregateAlreadyExistsException() {
        super();
    }

    /**
     * Constructor with all data.
     * 
     * @param aggregateType
     *            Type of the aggregate.
     * @param aggregateId
     *            Unique identifier of the aggregate.
     * @param version
     *            Actual version.
     */
    public AggregateAlreadyExistsException(@NotNull final EntityType aggregateType, @NotNull final AggregateRootId aggregateId,
            final int version) {
        super("Aggregate " + aggregateType.asString() + " (" + aggregateId.asString() + ") already exists (version=" + version + ")");

        this.sid = SHORT_ID_PREFIX + "-AGGREGATE_ALREADY_EXISTS";
        this.aggregateType = aggregateType.asString();
        this.aggregateId = aggregateId.asString();
        this.version = version;
    }

    @Override
    public final String getShortId() {
        return sid;
    }

    /**
     * Returns the type of the aggregate.
     * 
     * @return Type.
     */
    @NotNull
    public final String getAggregateType() {
        return aggregateType;
    }

    /**
     * Returns the unique identifier of the aggregate.
     * 
     * @return Stream with version conflict.
     */
    @NotNull
    public final String getAggregateId() {
        return aggregateId;
    }

    /**
     * Returns the actual version.
     * 
     * @return Actual version.
     */
    public final int getVersion() {
        return version;
    }

}
