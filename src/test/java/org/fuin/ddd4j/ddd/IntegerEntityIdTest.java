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
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.fuin.objects4j.common.ConstraintViolationException;
import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

/**
 * Test for {@link AggregateRootId}.
 */
public class IntegerEntityIdTest {

    @Test
    public void testEqualsHashCode() {
        EqualsVerifier
                .forClass(IntegerEntityId.class).withPrefabValues(EntityType.class,
                        new StringBasedEntityType("A"), new StringBasedEntityType("B"))
                .suppress(Warning.NULL_FIELDS).verify();
    }

    @Test
    public void testIsValid() {

        assertThat(IntegerEntityId.isValid(null)).isTrue();
        assertThat(IntegerEntityId.isValid(-1)).isFalse();
        assertThat(IntegerEntityId.isValid(0)).isFalse();
        assertThat(IntegerEntityId.isValid(IntegerEntityId.MIN)).isTrue();
        assertThat(IntegerEntityId.isValid(Integer.MAX_VALUE)).isTrue();

    }

    @Test
    public void testCompareTo() {

        final IntegerEntityId a1 = new IntegerEntityId(new StringBasedEntityType("A"), 1) {
        };
        final IntegerEntityId a2 = new IntegerEntityId(new StringBasedEntityType("A"), 2) {
        };
        final IntegerEntityId a3 = new IntegerEntityId(new StringBasedEntityType("A"), 3) {
        };
        final IntegerEntityId b1 = new IntegerEntityId(new StringBasedEntityType("B"), 1) {
        };
        final IntegerEntityId b2 = new IntegerEntityId(new StringBasedEntityType("B"), 2) {
        };
        final IntegerEntityId b3 = new IntegerEntityId(new StringBasedEntityType("B"), 3) {
        };

        final List<IntegerEntityId> ids = new ArrayList<>();
        ids.add(a3);
        ids.add(b1);
        ids.add(a1);
        ids.add(b2);
        ids.add(a2);
        ids.add(b3);
        Collections.sort(ids);

        assertThat(ids).containsExactly(a1, a2, a3, b1, b2, b3);

    }

    @Test
    public void testRequireArgValid() {

        IntegerEntityId.requireArgValid("null", null);
        IntegerEntityId.requireArgValid("min", IntegerEntityId.MIN);
        IntegerEntityId.requireArgValid("max", Integer.MAX_VALUE);

        try {
            IntegerEntityId.requireArgValid("a", -1);
            fail();
        } catch (final ConstraintViolationException ex) {
            assertThat(ex.getMessage()).isEqualTo("The argument 'a' is not valid: -1");
        }

        try {
            IntegerEntityId.requireArgValid("a", 0);
            fail();
        } catch (final ConstraintViolationException ex) {
            assertThat(ex.getMessage()).isEqualTo("The argument 'a' is not valid: 0");
        }

    }

    @Test
    public void testSimpleMethods() {

        final StringBasedEntityType type = new StringBasedEntityType("A");
        final IntegerEntityId a1 = new IntegerEntityId(type, 1) {
        };

        assertThat(a1.toString()).isEqualTo("1");
        assertThat(a1.asString()).isEqualTo("1");
        assertThat(a1.asTypedString()).isEqualTo(type + " 1");
        assertThat(a1.getType()).isEqualTo(type);

    }

}
