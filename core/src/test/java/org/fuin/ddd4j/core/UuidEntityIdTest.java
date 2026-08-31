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

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.fuin.objects4j.common.ConstraintViolationException;
import org.junit.jupiter.api.Test;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Test for {@link UuidEntityId}.
 */
public class UuidEntityIdTest {

    @Test
    public void testEqualsHashCode() {
        EqualsVerifier.forClass(UuidEntityId.class)
                .withPrefabValues(EntityType.class, new StringBasedEntityType("A"), new StringBasedEntityType("B"))
                .withPrefabValues(UUID.class, UUID.randomUUID(), UUID.randomUUID()).suppress(Warning.NULL_FIELDS).verify();
    }

    @Test
    public void testCompareTo() {

        final UUID uuid1 = UUID.fromString("00000000-0000-0000-0000-000000000001");
        final UUID uuid2 = UUID.fromString("00000000-0000-0000-0000-000000000002");

        final UuidEntityId a1 = id("A", uuid1);
        final UuidEntityId a2 = id("A", uuid2);
        final UuidEntityId b1 = id("B", uuid1);
        final UuidEntityId b2 = id("B", uuid2);

        final List<UuidEntityId> ids = new ArrayList<>();
        ids.add(b2);
        ids.add(a2);
        ids.add(b1);
        ids.add(a1);
        Collections.sort(ids);

        assertThat(ids).containsExactly(a1, a2, b1, b2);

    }

    @Test
    public void testSimpleMethods() {

        final StringBasedEntityType type = new StringBasedEntityType("A");
        final UUID uuid = UUID.randomUUID();
        final UuidEntityId a1 = id("A", uuid);

        assertThat(a1.toString()).isEqualTo(uuid.toString());
        assertThat(a1.asString()).isEqualTo(uuid.toString());
        assertThat(a1.asTypedString()).isEqualTo(type + " " + uuid);
        assertThat(a1.getType()).isEqualTo(type);
        assertThat(a1.getBaseType()).isEqualTo(UUID.class);
        assertThat(a1.asBaseType()).isEqualTo(uuid);

    }

    @Test
    public void testRandomUuidConstructor() {

        final StringBasedEntityType type = new StringBasedEntityType("A");
        final UuidEntityId a1 = new UuidEntityId(type) {
            @Serial
            private static final long serialVersionUID = 1L;
        };
        final UuidEntityId a2 = new UuidEntityId(type) {
            @Serial
            private static final long serialVersionUID = 1L;
        };

        assertThat(a1.asBaseType()).isNotNull();
        assertThat(a1).isNotEqualTo(a2);

    }

    @Test
    public void testIsValid() {

        assertThat(UuidEntityId.isValid(null)).isTrue();
        assertThat(UuidEntityId.isValid("")).isFalse();
        assertThat(UuidEntityId.isValid("0")).isFalse();
        assertThat(UuidEntityId.isValid("00000000-0000-0000-0000-000000000000")).isTrue();
        assertThat(UuidEntityId.isValid(UUID.randomUUID().toString())).isTrue();

    }

    @Test
    public void testRequireArgValid() {

        UuidEntityId.requireArgValid("null", null);
        UuidEntityId.requireArgValid("zero", "00000000-0000-0000-0000-000000000000");
        UuidEntityId.requireArgValid("other", UUID.randomUUID().toString());

        try {
            UuidEntityId.requireArgValid("a", "");
            fail();
        } catch (final ConstraintViolationException ex) {
            assertThat(ex.getMessage()).isEqualTo("The argument 'a' is not valid: ''");
        }

    }

    private static UuidEntityId id(final String type, final UUID uuid) {
        return new UuidEntityId(new StringBasedEntityType(type), uuid) {
            @Serial
            private static final long serialVersionUID = 1L;
        };
    }

}
