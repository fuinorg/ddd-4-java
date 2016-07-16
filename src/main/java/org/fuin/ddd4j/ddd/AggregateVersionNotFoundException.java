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

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.fuin.objects4j.common.AbstractJaxbMarshallableException;
import org.fuin.objects4j.common.ExceptionShortIdentifable;
import org.fuin.objects4j.common.NeverNull;

/**
 * Signals that the requested version for an aggregate does not exist.
 */
@XmlRootElement(name = "aggregate-version-not-found-exception")
public final class AggregateVersionNotFoundException extends AbstractJaxbMarshallableException implements
        ExceptionShortIdentifable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "sid")
    private String sid;

    @XmlElement(name = "aggregate-type")
    private String aggregateType;

    @XmlElement(name = "aggregate-id")
    private String aggregateId;

    @XmlElement(name = "version")
    private int version;

    /**
     * JAX-B constructor.
     */
    protected AggregateVersionNotFoundException() {
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
     *            Requested version.
     */
    public AggregateVersionNotFoundException(@NotNull final EntityType aggregateType,
            @NotNull final AggregateRootId aggregateId, final int version) {
        super("Requested version " + version + " for " + aggregateType.asString() + " ("
                + aggregateId.asString() + ") does not exist");

        this.sid = SHORT_ID_PREFIX + "-AGGREGATE_VERSION_NOT_FOUND";
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
    @NeverNull
    public final String getAggregateType() {
        return aggregateType;
    }

    /**
     * Returns the unique identifier of the aggregate.
     * 
     * @return Stream with version conflict.
     */
    @NeverNull
    public final String getAggregateId() {
        return aggregateId;
    }

    /**
     * Returns the requested version.
     * 
     * @return Version.
     */
    public final int getVersion() {
        return version;
    }

}
