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

import org.fuin.ddd4j.test.AId;
import org.fuin.ddd4j.test.BId;
import org.fuin.ddd4j.test.CId;
import org.junit.Test;

//CHECKSTYLE:OFF Test code
public class ExpectedEntityIdPathValidatorTest {

    @Test
    public void testIsValidNull() {

        // PREPARE
        final ExpectedEntityIdPath anno = createAnnotation(AId.TYPE.asString());
        final ExpectedEntityIdPathValidator testee = new ExpectedEntityIdPathValidator();
        testee.initialize(anno);

        // TEST & VERIFY
        assertThat(testee.isValid(null, null)).isTrue();
    }

    @Test
    public void testIsValidOneLevel() {

        // PREPARE
        final AId aid = new AId(1L);
        final BId bid = new BId(2L);
        final ExpectedEntityIdPath anno = createAnnotation(AId.TYPE.asString());
        final ExpectedEntityIdPathValidator testee = new ExpectedEntityIdPathValidator();
        testee.initialize(anno);

        // TEST & VERIFY
        assertThat(testee.isValid(new EntityIdPath(aid), null)).isTrue();
        assertThat(testee.isValid(new EntityIdPath(bid), null)).isFalse();
        assertThat(testee.isValid(new EntityIdPath(aid, bid), null)).isFalse();

    }

    @Test
    public void testIsValidTwoLevel() {

        // PREPARE
        final AId aid = new AId(1L);
        final BId bid = new BId(2L);
        final CId cid = new CId(3L);
        final ExpectedEntityIdPath anno = createAnnotation(AId.TYPE.asString(), BId.TYPE.asString());
        final ExpectedEntityIdPathValidator testee = new ExpectedEntityIdPathValidator();
        testee.initialize(anno);

        // TEST & VERIFY
        assertThat(testee.isValid(new EntityIdPath(aid, bid), null)).isTrue();
        assertThat(testee.isValid(new EntityIdPath(aid), null)).isFalse();
        assertThat(testee.isValid(new EntityIdPath(aid, cid), null)).isFalse();
        assertThat(testee.isValid(new EntityIdPath(aid, bid, cid), null)).isFalse();

    }

    @Test
    public void testIsValidThreeLevel() {

        // PREPARE
        final AId aid = new AId(1L);
        final BId bid = new BId(2L);
        final CId cid = new CId(3L);
        final CId cid2 = new CId(123L);
        final ExpectedEntityIdPath anno = createAnnotation(AId.TYPE.asString(), BId.TYPE.asString(), CId.TYPE.asString());
        final ExpectedEntityIdPathValidator testee = new ExpectedEntityIdPathValidator();
        testee.initialize(anno);

        // TEST & VERIFY
        assertThat(testee.isValid(new EntityIdPath(aid, bid, cid), null)).isTrue();
        assertThat(testee.isValid(new EntityIdPath(aid, bid), null)).isFalse();
        assertThat(testee.isValid(new EntityIdPath(aid), null)).isFalse();
        assertThat(testee.isValid(new EntityIdPath(aid, cid, bid), null)).isFalse();
        assertThat(testee.isValid(new EntityIdPath(aid, bid, cid, cid2), null)).isFalse();

    }

    private static ExpectedEntityIdPath createAnnotation(final String... expectedValues) {
        return new ExpectedEntityIdPath() {
            @Override
            public String[] value() {
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
}
// CHECKSTYLE:ON
