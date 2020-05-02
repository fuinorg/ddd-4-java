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

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.annotation.Annotation;

import javax.validation.Payload;

import org.fuin.ddd4j.test.ACreatedEvent;
import org.fuin.ddd4j.test.AId;
import org.fuin.ddd4j.test.BId;
import org.fuin.ddd4j.test.CAddedEvent;
import org.fuin.ddd4j.test.CEvent;
import org.fuin.ddd4j.test.CId;
import org.junit.Test;

//CHECKSTYLE:OFF Test code
public class DomainEventExpectedEntityIdPathValidatorTest {

    @Test
    public void testIsValidNull() {

        // PREPARE
        final DomainEventExpectedEntityIdPath anno = createAnnotation(AId.class);
        final DomainEventExpectedEntityIdPathValidator testee = new DomainEventExpectedEntityIdPathValidator();
        testee.initialize(anno);

        // TEST & VERIFY
        assertThat(testee.isValid(null, null)).isTrue();
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
        assertThat(testee.isValid(new ACreatedEvent(aid), null)).isTrue();
        assertThat(testee.isValid(new CAddedEvent(aid, bid, cid), null)).isFalse();
        assertThat(testee.isValid(new CEvent(aid, bid, cid), null)).isFalse();

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
// CHECKSTYLE:ON
