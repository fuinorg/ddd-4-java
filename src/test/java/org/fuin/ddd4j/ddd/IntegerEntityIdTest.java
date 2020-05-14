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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

/**
 * Test for {@link AggregateRootId}.
 */
public class IntegerEntityIdTest {

    @Test
    public void testEqualsHashCode() {
        EqualsVerifier.forClass(IntegerEntityId.class)
                .withPrefabValues(EntityType.class, new StringBasedEntityType("A"), new StringBasedEntityType("B"))
                .suppress(Warning.NULL_FIELDS).verify();
    }

    @Test
    public void testCompareTo() {

        final IntegerEntityId a1 = new IntegerEntityId(new StringBasedEntityType("A"), 1) {
            private static final long serialVersionUID = 1L;
        };
        final IntegerEntityId a2 = new IntegerEntityId(new StringBasedEntityType("A"), 2) {
            private static final long serialVersionUID = 1L;
        };
        final IntegerEntityId a3 = new IntegerEntityId(new StringBasedEntityType("A"), 3) {
            private static final long serialVersionUID = 1L;
        };
        final IntegerEntityId b1 = new IntegerEntityId(new StringBasedEntityType("B"), 1) {
            private static final long serialVersionUID = 1L;
        };
        final IntegerEntityId b2 = new IntegerEntityId(new StringBasedEntityType("B"), 2) {
            private static final long serialVersionUID = 1L;
        };
        final IntegerEntityId b3 = new IntegerEntityId(new StringBasedEntityType("B"), 3) {
            private static final long serialVersionUID = 1L;
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
    public void testSimpleMethods() {

        final StringBasedEntityType type = new StringBasedEntityType("A");
        final IntegerEntityId a1 = new IntegerEntityId(type, 1) {
            private static final long serialVersionUID = 1L;
        };

        assertThat(a1.toString()).isEqualTo("1");
        assertThat(a1.asString()).isEqualTo("1");
        assertThat(a1.asTypedString()).isEqualTo(type + " 1");
        assertThat(a1.getType()).isEqualTo(type);

    }

}
