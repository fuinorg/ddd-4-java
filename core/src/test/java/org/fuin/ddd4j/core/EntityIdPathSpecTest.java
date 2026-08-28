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

import org.fuin.ddd4j.coretest.AId;
import org.fuin.ddd4j.coretest.BId;
import org.fuin.ddd4j.coretest.CId;
import org.fuin.objects4j.common.ConstraintViolationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link EntityIdPathSpec}.
 * <p>
 * The shape lives here rather than inside the constraint validator so that generated code can hold one as
 * a constant and check a path in a constructor, without a Bean Validation lifecycle to run it.
 */
public class EntityIdPathSpecTest {

    private static final AId A = new AId(1L);

    private static final BId B = new BId(2L);

    private static final BId B2 = new BId(3L);

    private static final CId C = new CId(4L);

    @Test
    public void testStepsTakeExactlyOneByDefault() {
        final EntityIdPathSpec testee = EntityIdPathSpec.builder().step(AId.class).step(BId.class).build();

        assertThat(testee.matches(new EntityIdPath(A, B))).isTrue();
        assertThat(testee.matches(new EntityIdPath(A))).isFalse();
        assertThat(testee.matches(new EntityIdPath(A, B, B2))).isFalse();
        assertThat(testee.matches(new EntityIdPath(B, A))).isFalse();
    }

    @Test
    public void testAnUnboundedStepTakesOneOrMore() {
        final EntityIdPathSpec testee = EntityIdPathSpec.builder()
                .step(AId.class).step(BId.class, 1, Integer.MAX_VALUE).build();

        assertThat(testee.matches(new EntityIdPath(A, B))).isTrue();
        assertThat(testee.matches(new EntityIdPath(A, B, B2))).isTrue();
        assertThat(testee.matches(new EntityIdPath(A))).isFalse();
    }

    @Test
    public void testASkippableStepMayBeAbsent() {
        // 'COMPANY/DEPARTEMENT[0..N]/GROUP' - a group directly under a company.
        final EntityIdPathSpec testee = EntityIdPathSpec.builder()
                .step(AId.class).step(BId.class, 0, Integer.MAX_VALUE).step(CId.class).build();

        assertThat(testee.matches(new EntityIdPath(A, C))).isTrue();
        assertThat(testee.matches(new EntityIdPath(A, B, C))).isTrue();
        assertThat(testee.matches(new EntityIdPath(A, B, B2, C))).isTrue();
        assertThat(testee.matches(new EntityIdPath(A, B))).isFalse();
    }

    @Test
    public void testABoundedStepHasACeiling() {
        final EntityIdPathSpec testee = EntityIdPathSpec.builder()
                .step(AId.class).step(BId.class, 1, 2).build();

        assertThat(testee.matches(new EntityIdPath(A, B))).isTrue();
        assertThat(testee.matches(new EntityIdPath(A, B, B2))).isTrue();
        assertThat(testee.matches(new EntityIdPath(A, B, B2, B))).isFalse();
    }

    @Test
    public void testNothingToDisagreeWith() {
        // A path that is not there has no shape to be wrong about; whether it may be absent is a separate
        // question, asked by whatever declares the value.
        assertThat(EntityIdPathSpec.builder().step(AId.class).build().matches(null)).isTrue();
    }

    @Test
    public void testItSaysWhatItWanted() {
        final EntityIdPathSpec testee = EntityIdPathSpec.builder()
                .step(AId.class).step(BId.class, 1, Integer.MAX_VALUE).step(CId.class, 0, 2).build();

        assertThat(testee).hasToString("AId, BId[1..N], CId[0..2]");
    }

    @Test
    public void testRequireArgValidNamesTheValueAndTheShape() {
        final EntityIdPathSpec testee = EntityIdPathSpec.builder().step(AId.class).step(BId.class).build();

        testee.requireArgValid("path", new EntityIdPath(A, B));
        assertThatThrownBy(() -> testee.requireArgValid("path", new EntityIdPath(A)))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("'path'")
                .hasMessageContaining("AId, BId");
    }

    @Test
    public void testAShapeThatCouldMatchNothingIsRefusedWhenItIsBuilt() {
        assertThatThrownBy(() -> EntityIdPathSpec.builder().build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("no steps");
        assertThatThrownBy(() -> EntityIdPathSpec.builder().step(AId.class, 2, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[2..1]");
        assertThatThrownBy(() -> EntityIdPathSpec.builder().step(AId.class, 0, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[0..0]");
    }

}
