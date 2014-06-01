/**
 * Copyright (C) 2013 Future Invent Informationsmanagement GmbH. All rights
 * reserved. <http://www.fuin.org/>
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
 * along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fuin.ddd4j.ddd;

import javax.validation.constraints.NotNull;

import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.common.NeverNull;
import org.fuin.objects4j.common.UniquelyNumberedException;

/**
 * Signals a conflict between an expected and an actual version for an
 * aggregate.
 */
public final class AggregateVersionConflictException extends
        UniquelyNumberedException {

    private static final long serialVersionUID = 1L;

    private final EntityType aggregateType;

    private final AggregateRootId aggregateId;

    private final int expected;

    private final int actual;

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
    public AggregateVersionConflictException(
            @NotNull final EntityType aggregateType,
            @NotNull final AggregateRootId aggregateId, final int expected,
            final int actual) {
        super(103, "Expected version " + expected + " for aggregate '"
                + aggregateType + "' (" + aggregateId + "), but was " + actual);

        Contract.requireArgNotNull("aggregateType", aggregateType);
        Contract.requireArgNotNull("aggregateId", aggregateId);

        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.expected = expected;
        this.actual = actual;
    }

    /**
     * Returns the type of the aggregate.
     * 
     * @return Type.
     */
    @NeverNull
    public final EntityType getAggregateType() {
        return aggregateType;
    }

    /**
     * Returns the unique identifier of the aggregate.
     * 
     * @return Stream with version conflict.
     */
    @NeverNull
    public final AggregateRootId getAggregateId() {
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
