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

/**
 * Signals a conflict between an expected and an actual version for an aggregate.
 */
@XmlRootElement(name = "aggregate-version-conflict-exception")
public final class AggregateVersionConflictException extends AbstractJaxbMarshallableException
        implements ExceptionShortIdentifable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "sid")
    private String sid;

    @XmlElement(name = "aggregate-type")
    private String aggregateType;

    @XmlElement(name = "aggregate-id")
    private String aggregateId;

    @XmlElement(name = "expected")
    private int expected;

    @XmlElement(name = "actual")
    private int actual;

    /**
     * JAX-B constructor.
     */
    protected AggregateVersionConflictException() {
        super();
    }

    /**
     * Constructor with all data.
     * 
     * @param aggregateType
     *            Type of the aggregate.
     * @param aggregateId
     *            Unique identifier of the aggregate.
     * @param expected
     *            Expected version.
     * @param actual
     *            Actual version.
     */
    public AggregateVersionConflictException(@NotNull final EntityType aggregateType,
            @NotNull final AggregateRootId aggregateId, final int expected, final int actual) {
        super("Expected version " + expected + " for " + aggregateType.asString() + " ("
                + aggregateId.asString() + "), but was " + actual);

        this.sid = SHORT_ID_PREFIX + "-AGGREGATE_VERSION_CONFLICT";
        this.aggregateType = aggregateType.asString();
        this.aggregateId = aggregateId.asString();
        this.expected = expected;
        this.actual = actual;
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
     * Returns the expected version.
     * 
     * @return Expected version.
     */
    public final int getExpected() {
        return expected;
    }

    /**
     * Returns the actual version.
     * 
     * @return Actual version.
     */
    public final int getActual() {
        return actual;
    }

}
