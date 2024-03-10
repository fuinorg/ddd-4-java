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
package org.fuin.ddd4j.core;

import jakarta.validation.constraints.NotNull;
import org.fuin.objects4j.common.ExceptionShortIdentifable;

import java.io.Serial;

import static org.fuin.ddd4j.core.Ddd4JUtils.SHORT_ID_PREFIX;

/**
 * Signals a conflict between an expected and an actual version for an aggregate.
 */
public final class AggregateVersionConflictException extends AbstractAggregateException implements ExceptionShortIdentifable {

    /**
     * Unique name of the element to use for XML and JSON marshalling/unmarshalling.
     */
    public static final String ELEMENT_NAME = "aggregate-version-conflict-exception";

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Unique short identifier of this exception.
     */
    public static final String SHORT_ID = SHORT_ID_PREFIX + "-AGGREGATE_VERSION_CONFLICT";

    private final int expected;

    private final int actual;

    /**
     * Constructor with all data.
     *
     * @param type     Type of the aggregate.
     * @param id       Unique identifier of the aggregate.
     * @param expected Expected version.
     * @param actual   Actual version.
     */
    public AggregateVersionConflictException(@NotNull final EntityType type,
                                             @NotNull final AggregateRootId id,
                                             final int expected,
                                             final int actual) {
        this(type.asString(), id.asString(), expected, actual);
    }

    /**
     * Constructor with string.
     *
     * @param type     Type of the aggregate.
     * @param id       Unique identifier of the aggregate.
     * @param expected Expected version.
     * @param actual   Actual version.
     */
    public AggregateVersionConflictException(@NotNull final String type,
                                             @NotNull final String id,
                                             final int expected,
                                             final int actual) {
        super("Expected version " + expected + " for " + type + " (" + id + "), but was " + actual, type, id);
        this.expected = expected;
        this.actual = actual;
    }

    @Override
    public String getShortId() {
        return SHORT_ID;
    }

    /**
     * Returns the expected version.
     *
     * @return Expected version.
     */
    public int getExpected() {
        return expected;
    }

    /**
     * Returns the actual version.
     *
     * @return Actual version.
     */
    public int getActual() {
        return actual;
    }

}
