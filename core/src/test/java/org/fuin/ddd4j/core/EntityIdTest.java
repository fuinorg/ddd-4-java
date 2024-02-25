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

public class EntityIdTest {

    @Test
    public void testIsValid() {

        // PREPARE
        final EntityIdFactory factory = new MyIdFactory();

        // TEST & VERIFY
        assertThat(EntityId.isValid(factory, null)).isTrue();
        assertThat(EntityId.isValid(factory, "A 1")).isTrue();

        assertThat(EntityId.isValid(factory, "X 1")).isFalse();
        assertThat(EntityId.isValid(factory, "X x")).isFalse();
        assertThat(EntityId.isValid(factory, "")).isFalse();

    }

    @Test
    public void testRequireArgValid() {

        // PREPARE
        final EntityIdFactory factory = new MyIdFactory();

        // TEST & VERIFY

        EntityId.requireArgValid(factory, "a", null);
        EntityId.requireArgValid(factory, "x", "A 1");

        assertThatThrownBy(() -> EntityId.requireArgValid(factory, "a", "X 1"))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessage("The argument 'a' is not valid: 'X 1'");

        assertThatThrownBy(() -> EntityId.requireArgValid(factory, "a", ""))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessage("The argument 'a' is not valid: ''");

    }

    @Test
    public void testValueOf() {

        // PREPARE
        final EntityIdFactory factory = new MyIdFactory();

        // TEST & VERIFY
        assertThat(EntityId.valueOf(factory, "A 1")).isInstanceOf(AId.class);
        assertThat(EntityId.valueOf(factory, "A 1").getType().asString()).isEqualTo("A");
        assertThat(EntityId.valueOf(factory, "A 1").asString()).isEqualTo("" + 1);


    }

    private static final class MyIdFactory implements EntityIdFactory {

        @Override
        public EntityId createEntityId(final String type, final String id) {
            if (type.equals("A")) {
                return new AId(Long.parseLong(id));
            }
            if (type.equals("B")) {
                return new BId(Long.parseLong(id));
            }
            if (type.equals("C")) {
                return new CId(Long.parseLong(id));
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
