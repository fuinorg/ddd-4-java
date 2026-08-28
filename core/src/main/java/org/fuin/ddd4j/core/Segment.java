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

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * One step of an expected entity identifier path: the identifier type it is made of, and how many of them
 * the step accepts.
 * <p>
 * The default is exactly one, which is every ordinary step. A wider range exists for an entity that may
 * contain another of its own kind - a role inside a role, a department inside a department - and it is
 * written out rather than marked, because "may repeat" does not say whether the step may also be absent.
 * {@code min = 0} makes it skippable, {@code max = Integer#MAX_VALUE} unbounded.
 *
 * @see ExpectedEntityIdPath
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Segment {

    /**
     * Identifier type this step is made of.
     *
     * @return Expected type.
     */
    Class<? extends EntityId> type();

    /**
     * Fewest identifiers of this type the step accepts. Zero makes the step skippable.
     *
     * @return Lower bound, never negative.
     */
    int min() default 1;

    /**
     * Most identifiers of this type the step accepts. {@link Integer#MAX_VALUE} for unbounded.
     *
     * @return Upper bound, at least one and never below {@link #min()}.
     */
    int max() default 1;

}
