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

import org.fuin.objects4j.common.ConstraintViolationException;
import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.common.ThreadSafe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The shape an {@link EntityIdPath} is expected to have: an ordered list of steps, each an identifier type
 * and how many of them it accepts.
 * <p>
 * This is the same thing {@link ExpectedEntityIdPath} declares, in a form that can be built and asked
 * outside a Bean Validation lifecycle. Generated code holds one as a constant and checks a path against it
 * in a constructor; the {@link ExpectedEntityIdPathValidator} builds one from its annotation. Both then
 * match the same way, which is the point of it being here rather than written twice.
 */
@ThreadSafe
public final class EntityIdPathSpec {

    private final List<Step> steps;

    private EntityIdPathSpec(final List<Step> steps) {
        if (steps.isEmpty()) {
            throw new IllegalArgumentException("A path shape with no steps matches nothing");
        }
        this.steps = Collections.unmodifiableList(steps);
    }

    /**
     * Returns a builder for a shape.
     *
     * @return New builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Returns the shape an annotation declares.
     *
     * @param segments Steps as declared.
     * @return New instance.
     */
    public static EntityIdPathSpec of(final Segment[] segments) {
        Contract.requireArgNotNull("segments", segments);
        final Builder builder = builder();
        for (final Segment segment : segments) {
            builder.step(segment.type(), segment.min(), segment.max());
        }
        return builder.build();
    }

    /**
     * Whether a path has this shape.
     *
     * @param path Path to check, or {@literal null} which is nothing to disagree with.
     * @return {@literal true} if it matches.
     */
    public boolean matches(final EntityIdPath path) {
        if (path == null) {
            return true;
        }
        final List<EntityId> actual = new ArrayList<>();
        path.iterator().forEachRemaining(actual::add);
        return matches(0, actual, 0);
    }

    /**
     * Checks a path has this shape and throws if it does not.
     *
     * @param name Name of the value for a possible error message.
     * @param path Path to check.
     * @throws ConstraintViolationException The path did not have this shape.
     */
    public void requireArgValid(final String name, final EntityIdPath path) throws ConstraintViolationException {
        Contract.requireArgNotNull("name", name);
        if (!matches(path)) {
            throw new ConstraintViolationException("The argument '" + name + "' is not valid: expected '"
                    + this + "' but was '" + path + "'");
        }
    }

    /**
     * Whether the steps from {@code stepIdx} on match the identifiers from {@code idIdx} on.
     * <p>
     * A step takes between its own bounds, so the search backtracks rather than being greedy: two
     * unbounded steps of the same type in a row would otherwise never match, and neither would a skippable
     * one followed by something it could have swallowed. The lists are a handful of entries long, which is
     * why this is written for clarity rather than for speed.
     */
    private boolean matches(final int stepIdx, final List<EntityId> actual, final int idIdx) {
        if (stepIdx == steps.size()) {
            return idIdx == actual.size();
        }
        final Step step = steps.get(stepIdx);
        int available = 0;
        while (idIdx + available < actual.size() && available < step.max()
                && step.type().isAssignableFrom(actual.get(idIdx + available).getClass())) {
            available++;
        }
        for (int taken = step.min(); taken <= available; taken++) {
            if (matches(stepIdx + 1, actual, idIdx + taken)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return steps.stream().map(Step::toString).collect(Collectors.joining(", "));
    }

    /** One step of a shape. */
    private record Step(Class<? extends EntityId> type, int min, int max) {

        private Step {
            Contract.requireArgNotNull("type", type);
            if (min < 0 || max < 1 || max < min) {
                throw new IllegalArgumentException("Step range for '" + type.getSimpleName()
                        + "' is invalid: [" + min + ".." + max + "]");
            }
        }

        @Override
        public String toString() {
            if (min == 1 && max == 1) {
                return type.getSimpleName();
            }
            return type.getSimpleName() + "[" + min + ".." + (max == Integer.MAX_VALUE ? "N" : max) + "]";
        }

    }

    /** Builds a shape one step at a time, which is how generated code writes one. */
    public static final class Builder {

        private final List<Step> steps = new ArrayList<>();

        private Builder() {
            super();
        }

        /**
         * Adds a step that takes exactly one identifier.
         *
         * @param type Identifier type.
         * @return This builder.
         */
        public Builder step(final Class<? extends EntityId> type) {
            return step(type, 1, 1);
        }

        /**
         * Adds a step that takes between {@code min} and {@code max} identifiers.
         *
         * @param type Identifier type.
         * @param min  Fewest accepted, never negative.
         * @param max  Most accepted, {@link Integer#MAX_VALUE} for unbounded.
         * @return This builder.
         */
        public Builder step(final Class<? extends EntityId> type, final int min, final int max) {
            steps.add(new Step(type, min, max));
            return this;
        }

        /**
         * Returns the shape.
         *
         * @return New instance.
         */
        public EntityIdPathSpec build() {
            return new EntityIdPathSpec(steps);
        }

    }

}
