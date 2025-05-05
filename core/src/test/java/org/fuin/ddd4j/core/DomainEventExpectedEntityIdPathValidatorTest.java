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
import jakarta.validation.ValidatorFactory;
import org.fuin.ddd4j.coretest.ACreatedEvent;
import org.fuin.ddd4j.coretest.AId;
import org.fuin.ddd4j.coretest.BId;
import org.fuin.ddd4j.coretest.CAddedEvent;
import org.fuin.ddd4j.coretest.CEvent;
import org.fuin.ddd4j.coretest.CId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DomainEventExpectedEntityIdPathValidatorTest {

    private ConstraintValidatorContext context;

    private ConstraintValidatorContext.ConstraintViolationBuilder builder;

    @BeforeEach
    public void setUp() {
        context = mock(ConstraintValidatorContext.class);
        builder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        when(context.buildConstraintViolationWithTemplate(any())).thenReturn(builder);
    }

    @AfterEach
    public void tearDown() {
        context = null;
    }

    @Test
    public void testIsValidNull() {

        // PREPARE
        final DomainEventExpectedEntityIdPath anno = createAnnotation(AId.class);
        final DomainEventExpectedEntityIdPathValidator testee = new DomainEventExpectedEntityIdPathValidator();
        testee.initialize(anno);

        // TEST & VERIFY
        assertThat(testee.isValid(null, context)).isTrue();
    }

    @Test
    public void testIsValidOneLevel() {

        // PREPARE
        final AId aid = new AId(1L);
        final BId bid = new BId(2L);
        final CId cid = new CId(3L);
        final DomainEventExpectedEntityIdPath anno = createAnnotation(AId.class);
        final DomainEventExpectedEntityIdPathValidator testee = new DomainEventExpectedEntityIdPathValidator();
        testee.initialize(anno);

        // TEST & VERIFY
        assertThat(testee.isValid(new ACreatedEvent(aid), context)).isTrue();
        assertThat(testee.isValid(new CAddedEvent(aid, bid, cid), context)).isFalse();
        assertThat(testee.isValid(new CEvent(aid, bid, cid), context)).isFalse();

    }

    @SafeVarargs
    private static DomainEventExpectedEntityIdPath createAnnotation(final Class<? extends EntityId>... expectedValues) {
        return new DomainEventExpectedEntityIdPath() {
            @Override
            public Class<? extends EntityId>[] value() {
                return expectedValues;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return DomainEventExpectedEntityIdPath.class;
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
}
