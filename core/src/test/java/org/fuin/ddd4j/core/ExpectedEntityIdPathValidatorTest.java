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

import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.fuin.ddd4j.coretest.AId;
import org.fuin.ddd4j.coretest.BId;
import org.fuin.ddd4j.coretest.CId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExpectedEntityIdPathValidatorTest {

    private Validator validator;

    private ConstraintValidatorContext context;

    private ConstraintValidatorContext.ConstraintViolationBuilder builder;

    @BeforeEach
    public void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
        context = mock(ConstraintValidatorContext.class);
        builder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        when(context.buildConstraintViolationWithTemplate(any())).thenReturn(builder);
    }

    @AfterEach
    public void tearDown() {
        validator = null;
        context = null;
    }

    @Test
    public void testIsValidNull() {

        // PREPARE
        final ExpectedEntityIdPath anno = createAnnotation(AId.class);
        final ExpectedEntityIdPathValidator testee = new ExpectedEntityIdPathValidator();
        testee.initialize(anno);

        // TEST & VERIFY
        assertThat(testee.isValid(null, context)).isTrue();

    }

    @Test
    public void testIsValidOneLevel() {

        // PREPARE
        final AId aid = new AId(1L);
        final BId bid = new BId(2L);
        final ExpectedEntityIdPath anno = createAnnotation(AId.class);
        final ExpectedEntityIdPathValidator testee = new ExpectedEntityIdPathValidator();
        testee.initialize(anno);

        // TEST & VERIFY
        assertThat(testee.isValid(new EntityIdPath(aid), context)).isTrue();
        assertThat(testee.isValid(new EntityIdPath(bid), context)).isFalse();
        assertThat(testee.isValid(new EntityIdPath(aid, bid), context)).isFalse();

        // TEST & VERIFY
        assertThat(validator.validate(new OneLevelPath(new EntityIdPath(aid)))).isEmpty();
        assertThat(validator.validate(new OneLevelPath(new EntityIdPath(bid))))
                .anyMatch(v -> v.getMessage().contains("Expected the following types (in order) 'AId', but was: 'BId' ('B 2')"));

    }

    @Test
    public void testIsValidTwoLevel() {

        // PREPARE
        final AId aid = new AId(1L);
        final BId bid = new BId(2L);
        final CId cid = new CId(3L);
        final ExpectedEntityIdPath anno = createAnnotation(AId.class, BId.class);
        final ExpectedEntityIdPathValidator testee = new ExpectedEntityIdPathValidator();
        testee.initialize(anno);

        // TEST & VERIFY
        assertThat(testee.isValid(new EntityIdPath(aid, bid), context)).isTrue();
        assertThat(testee.isValid(new EntityIdPath(aid), context)).isFalse();
        assertThat(testee.isValid(new EntityIdPath(aid, cid), context)).isFalse();
        assertThat(testee.isValid(new EntityIdPath(aid, bid, cid), context)).isFalse();

        // TEST & VERIFY
        assertThat(validator.validate(new TwoLevelPath(new EntityIdPath(aid, bid)))).isEmpty();
        assertThat(validator.validate(new TwoLevelPath(new EntityIdPath(aid))))
                .anyMatch(v -> v.getMessage()
                        .contains("Expected the following types (in order) 'AId, BId', but was: 'AId' ('A 1')"));
        assertThat(validator.validate(new TwoLevelPath(new EntityIdPath(aid, bid, cid))))
                .anyMatch(v -> v.getMessage()
                        .contains("Expected the following types (in order) 'AId, BId', but was: 'AId, BId, CId' ('A 1/B 2/C 3')"));

    }

    @Test
    public void testIsValidThreeLevel() {

        // PREPARE
        final AId aid = new AId(1L);
        final BId bid = new BId(2L);
        final CId cid = new CId(3L);
        final CId cid2 = new CId(123L);
        final ExpectedEntityIdPath anno = createAnnotation(AId.class, BId.class, CId.class);
        final ExpectedEntityIdPathValidator testee = new ExpectedEntityIdPathValidator();
        testee.initialize(anno);

        // TEST & VERIFY
        assertThat(testee.isValid(new EntityIdPath(aid, bid, cid), context)).isTrue();
        assertThat(testee.isValid(new EntityIdPath(aid, bid), context)).isFalse();
        assertThat(testee.isValid(new EntityIdPath(aid), context)).isFalse();
        assertThat(testee.isValid(new EntityIdPath(aid, cid, bid), context)).isFalse();
        assertThat(testee.isValid(new EntityIdPath(aid, bid, cid, cid2), context)).isFalse();

        // TEST & VERIFY
        assertThat(validator.validate(new ThreeLevelPath(new EntityIdPath(aid, bid, cid)))).isEmpty();
        assertThat(validator.validate(new ThreeLevelPath(new EntityIdPath(aid))))
                .anyMatch(v -> v.getMessage()
                        .contains("Expected the following types (in order) 'AId, BId, CId', but was: 'AId' ('A 1')"));
        assertThat(validator.validate(new ThreeLevelPath(new EntityIdPath(aid, bid))))
                .anyMatch(v -> v.getMessage()
                        .contains("Expected the following types (in order) 'AId, BId, CId', but was: 'AId, BId' ('A 1/B 2')"));

    }

    @Test
    public void testIsValidUnboundedSegmentTakesOneOrMore() {
        // An entity that may contain another of its own kind - a role inside a role. One or more, so the
        // marked step still has to appear and the path always addresses the thing the shape names.
        final ExpectedEntityIdPathValidator testee = new ExpectedEntityIdPathValidator();
        testee.initialize(createAnnotation(segment(AId.class), segment(BId.class, 1, Integer.MAX_VALUE)));

        final AId aid = new AId(1);
        final BId bid = new BId(2);
        final BId bid2 = new BId(3);
        final CId cid = new CId(4);

        assertThat(testee.isValid(new EntityIdPath(aid, bid), context)).isTrue();
        assertThat(testee.isValid(new EntityIdPath(aid, bid, bid2), context)).isTrue();
        assertThat(testee.isValid(new EntityIdPath(aid, bid, bid2, bid), context)).isTrue();

        // The marked step is one-or-more, so the root on its own does not match.
        assertThat(testee.isValid(new EntityIdPath(aid), context)).isFalse();
        assertThat(testee.isValid(new EntityIdPath(aid, cid), context)).isFalse();
        assertThat(testee.isValid(new EntityIdPath(aid, bid, cid), context)).isFalse();
    }

    @Test
    public void testIsValidUnboundedSegmentInTheMiddle() {
        // 'COMPANY/DEPARTEMENT*/GROUP' - the shape that made repetition worth having.
        final ExpectedEntityIdPathValidator testee = new ExpectedEntityIdPathValidator();
        testee.initialize(createAnnotation(segment(AId.class), segment(BId.class, 1, Integer.MAX_VALUE),
                segment(CId.class)));

        final AId aid = new AId(1);
        final BId bid = new BId(2);
        final BId bid2 = new BId(3);
        final CId cid = new CId(4);

        assertThat(testee.isValid(new EntityIdPath(aid, bid, cid), context)).isTrue();
        assertThat(testee.isValid(new EntityIdPath(aid, bid, bid2, cid), context)).isTrue();

        // Without the repeating step there is nothing between the root and the leaf.
        assertThat(testee.isValid(new EntityIdPath(aid, cid), context)).isFalse();
        assertThat(testee.isValid(new EntityIdPath(aid, bid, bid2), context)).isFalse();
    }

    @Test
    public void testIsValidSkippableSegment() {
        // What the boolean could not say: '[0..N]' lets the step be absent altogether, so a group sits
        // directly under a company. Choosing between that and one-or-more is per model, not per language.
        final ExpectedEntityIdPathValidator testee = new ExpectedEntityIdPathValidator();
        testee.initialize(createAnnotation(segment(AId.class), segment(BId.class, 0, Integer.MAX_VALUE),
                segment(CId.class)));

        final AId aid = new AId(1);
        final BId bid = new BId(2);
        final BId bid2 = new BId(3);
        final CId cid = new CId(4);

        assertThat(testee.isValid(new EntityIdPath(aid, cid), context)).isTrue();
        assertThat(testee.isValid(new EntityIdPath(aid, bid, cid), context)).isTrue();
        assertThat(testee.isValid(new EntityIdPath(aid, bid, bid2, cid), context)).isTrue();

        assertThat(testee.isValid(new EntityIdPath(aid), context)).isFalse();
        assertThat(testee.isValid(new EntityIdPath(aid, bid), context)).isFalse();
    }

    @Test
    public void testIsValidBoundedSegment() {
        // '[1..2]' - a nesting depth the model puts a ceiling on.
        final ExpectedEntityIdPathValidator testee = new ExpectedEntityIdPathValidator();
        testee.initialize(createAnnotation(segment(AId.class), segment(BId.class, 1, 2)));

        final AId aid = new AId(1);
        final BId bid = new BId(2);
        final BId bid2 = new BId(3);
        final BId bid3 = new BId(4);

        assertThat(testee.isValid(new EntityIdPath(aid, bid), context)).isTrue();
        assertThat(testee.isValid(new EntityIdPath(aid, bid, bid2), context)).isTrue();

        assertThat(testee.isValid(new EntityIdPath(aid), context)).isFalse();
        assertThat(testee.isValid(new EntityIdPath(aid, bid, bid2, bid3), context)).isFalse();
    }

    @Test
    public void testARangeThatCannotMatchAnythingIsRefusedUpFront() {
        // Rather than silently rejecting every path at validation time.
        final ExpectedEntityIdPathValidator testee = new ExpectedEntityIdPathValidator();

        assertThatThrownBy(() -> testee.initialize(createAnnotation(segment(AId.class, 2, 1))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[2..1]");
        assertThatThrownBy(() -> testee.initialize(createAnnotation(segment(AId.class, 0, 0))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[0..0]");
    }

    @SafeVarargs
    private static ExpectedEntityIdPath createAnnotation(final Class<? extends EntityId>... expectedValues) {
        final Segment[] segments = new Segment[expectedValues.length];
        for (int i = 0; i < expectedValues.length; i++) {
            segments[i] = segment(expectedValues[i]);
        }
        return createAnnotation(segments);
    }

    private static ExpectedEntityIdPath createAnnotation(final Segment... expectedValues) {
        return new ExpectedEntityIdPath() {
            @Override
            public Segment[] value() {
                return expectedValues;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return ExpectedEntityIdPath.class;
            }

            @Override
            public String message() {
                return null;
            }

            @Override
            public Class<?>[] groups() {
                return null;
            }

            @Override
            public Class<? extends Payload>[] payload() {
                // TODO Auto-generated method stub
                return null;
            }
        };
    }

    private static Segment segment(final Class<? extends EntityId> type) {
        return segment(type, 1, 1);
    }

    private static Segment segment(final Class<? extends EntityId> type, final int min, final int max) {
        return new Segment() {
            @Override
            public Class<? extends EntityId> type() {
                return type;
            }

            @Override
            public int min() {
                return min;
            }

            @Override
            public int max() {
                return max;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return Segment.class;
            }
        };
    }

    public class OneLevelPath {

        @ExpectedEntityIdPath({@Segment(type = AId.class)})
        private EntityIdPath path;

        public OneLevelPath(EntityIdPath path) {
            this.path = path;
        }

    }

    public class TwoLevelPath {

        @ExpectedEntityIdPath({@Segment(type = AId.class), @Segment(type = BId.class)})
        private EntityIdPath path;

        public TwoLevelPath(EntityIdPath path) {
            this.path = path;
        }

    }

    public class ThreeLevelPath {

        @ExpectedEntityIdPath({@Segment(type = AId.class), @Segment(type = BId.class),
            @Segment(type = CId.class)})
        private EntityIdPath path;

        public ThreeLevelPath(EntityIdPath path) {
            this.path = path;
        }

    }

}
