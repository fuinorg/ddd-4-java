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

import org.fuin.ddd4j.test.AId;
import org.fuin.ddd4j.test.BId;
import org.fuin.ddd4j.test.CId;
import org.fuin.objects4j.common.ConstraintViolationException;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

//CHECKSTYLE:OFF
public class EntityIdPathConverterTest {

    @Test
    public void testIsValid() {

        // PREPARE
        final EntityIdPathConverter testee = new EntityIdPathConverter(new MyIdFactory());

        // TEST & VERIFY
        assertThat(testee.isValid(null)).isTrue();
        assertThat(testee.isValid("A 1")).isTrue();
        assertThat(testee.isValid("A 1/B 2")).isTrue();
        assertThat(testee.isValid("A 1/B 2/C 3")).isTrue();

        assertThat(testee.isValid("X 1")).isFalse();
        assertThat(testee.isValid("X x")).isFalse();
        assertThat(testee.isValid("A 1/X 2")).isFalse();
        assertThat(testee.isValid("A 1/B 2/X 3")).isFalse();
        assertThat(testee.isValid("")).isFalse();

    }

    @Test
    public void testRequireArgValid() {

        // PREPARE
        final EntityIdPathConverter testee = new EntityIdPathConverter(new MyIdFactory());

        // TEST & VERIFY

        testee.requireArgValid("a", null);
        testee.requireArgValid("x", "A 1");

        try {
            testee.requireArgValid("a", "X 1");
            fail("Expected exception");
        } catch (final ConstraintViolationException ex) {
            assertThat(ex.getMessage()).isEqualTo("The argument 'a' is not valid: 'X 1'");
        }

        try {
            testee.requireArgValid("a", "");
            fail("Expected exception");
        } catch (final ConstraintViolationException ex) {
            assertThat(ex.getMessage()).isEqualTo("The argument 'a' is not valid: ''");
        }

    }

    @Test
    public void testToVOSingle() {

        // PREPARE
        final EntityIdPathConverter testee = new EntityIdPathConverter(new MyIdFactory());
        EntityIdPath path;
        Iterator<EntityId> it;
        EntityId first;
        EntityId last;

        // TEST & VERIFY

        // Single
        path = testee.toVO("A 1");
        first = path.first();
        last = path.last();
        assertValues(first, AId.class, "A", 1L);
        assertValues(last, AId.class, "A", 1L);
        assertThat(first).isSameAs(last);
        it = path.iterator();
        assertValues(it.next(), AId.class, "A", 1L);
        assertThat(it.hasNext()).isFalse();

        // Two
        path = testee.toVO("A 1/B 2");
        first = path.first();
        last = path.last();
        assertValues(first, AId.class, "A", 1L);
        assertValues(last, BId.class, "B", 2L);
        it = path.iterator();
        assertValues(it.next(), AId.class, "A", 1L);
        assertValues(it.next(), BId.class, "B", 2L);
        assertThat(it.hasNext()).isFalse();

        // Three
        path = testee.toVO("A 1/B 2/C 3");
        first = path.first();
        last = path.last();
        assertValues(first, AId.class, "A", 1L);
        assertValues(last, CId.class, "C", 3L);
        it = path.iterator();
        assertValues(it.next(), AId.class, "A", 1L);
        assertValues(it.next(), BId.class, "B", 2L);
        assertValues(it.next(), CId.class, "C", 3L);
        assertThat(it.hasNext()).isFalse();

    }

    private void assertValues(EntityId entityId, Class<?> typeClass, String type, long id) {
        assertThat(entityId).isInstanceOf(typeClass);
        assertThat(entityId.getType().asString()).isEqualTo(type);
        assertThat(entityId.asString()).isEqualTo("" + id);
    }

    private static final class MyIdFactory implements EntityIdFactory {

        @Override
        public EntityId createEntityId(final String type, final String id) {
            if (type.equals("A")) {
                return new AId(Long.valueOf(id));
            }
            if (type.equals("B")) {
                return new BId(Long.valueOf(id));
            }
            if (type.equals("C")) {
                return new CId(Long.valueOf(id));
            }
            throw new IllegalArgumentException("Unknown type: '" + type + "'");
        }

        @Override
        public boolean containsType(final String type) {
            if (type.equals("A")) {
                return true;
            }
            if (type.equals("B")) {
                return true;
            }
            if (type.equals("C")) {
                return true;
            }
            return false;
        }

        @Override
        public boolean isValid(String type, String id) {
            try {
                if (type.equals("A")) {
                    Long.parseLong(id);
                    return true;
                }
                if (type.equals("B")) {
                    Long.parseLong(id);
                    return true;
                }
                if (type.equals("C")) {
                    Long.parseLong(id);
                    return true;
                }
                return false;
            } catch (final NumberFormatException ex) {
                return false;
            }
        }
    }

}
// CHECKSTYLE:ON
