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
import org.fuin.objects4j.common.ConstraintViolationException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

//CHECKSTYLE:OFF
public class EntityIdConverterTest {

    @Test
    public void testIsValid() {

        // PREPARE
        final EntityIdConverter testee = new EntityIdConverter(new MyIdFactory());

        // TEST & VERIFY
        assertThat(testee.isValid(null)).isTrue();
        assertThat(testee.isValid("A 1")).isTrue();

        assertThat(testee.isValid("A ")).isFalse();
        assertThat(testee.isValid("A 1 ")).isFalse();
        assertThat(testee.isValid(" A 1")).isFalse();
        assertThat(testee.isValid("A  1")).isFalse();
        assertThat(testee.isValid("X 1")).isFalse();
        assertThat(testee.isValid("X x")).isFalse();
        assertThat(testee.isValid("")).isFalse();

    }

    @Test
    public void testRequireArgValid() {

        // PREPARE
        final EntityIdConverter testee = new EntityIdConverter(new MyIdFactory());

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
    public void testToVO() {

        // PREPARE
        final EntityIdConverter testee = new EntityIdConverter(new MyIdFactory());
        final EntityId entityId = testee.toVO("A 1");

        // TEST & VERIFY
        assertThat(entityId).isInstanceOf(AId.class);
        assertThat(entityId.getType().asString()).isEqualTo("A");
        assertThat(entityId.asString()).isEqualTo("" + 1L);

    }

    @Test
    public void testFromVO() {

        // PREPARE
        final EntityIdConverter testee = new EntityIdConverter(new MyIdFactory());
        final EntityId entityId = testee.toVO("A 1");

        // TEST & VERIFY
        assertThat(testee.fromVO(entityId)).isEqualTo("A 1");

    }

    private static final class MyIdFactory implements EntityIdFactory {

        @Override
        public EntityId createEntityId(final String type, final String id) {
            if (type.equals("A")) {
                return new AId(Long.valueOf(id));
            }
            throw new IllegalArgumentException("Unknown type: '" + type + "'");
        }

        @Override
        public boolean containsType(final String type) {
            if (type.equals("A")) {
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
                return false;
            } catch (final NumberFormatException ex) {
                return false;
            }
        }
    }

}
// CHECKSTYLE:ON
