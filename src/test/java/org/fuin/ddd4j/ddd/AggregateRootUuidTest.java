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

import java.util.UUID;

import org.fuin.objects4j.common.ConstraintViolationException;
import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

/**
 * Test for {@link AggregateRootId}.
 */
public class AggregateRootUuidTest {

    @Test
    public void testEqualsHashCode() {
        EqualsVerifier.forClass(AggregateRootUuid.class)
                .withPrefabValues(EntityType.class, new StringBasedEntityType("A"), new StringBasedEntityType("B"))
                .withPrefabValues(UUID.class, UUID.randomUUID(), UUID.randomUUID()).suppress(Warning.NULL_FIELDS).verify();
    }

    @Test
    public void testIsValid() {

        assertThat(AggregateRootUuid.isValid(null)).isTrue();
        assertThat(AggregateRootUuid.isValid("")).isFalse();
        assertThat(AggregateRootUuid.isValid("0")).isFalse();
        assertThat(AggregateRootUuid.isValid("00000000-0000-0000-0000-000000000000")).isTrue();
        assertThat(AggregateRootUuid.isValid(UUID.randomUUID().toString())).isTrue();

    }

    @Test
    public void testRequireArgValid() {

        AggregateRootUuid.requireArgValid("null", null);
        AggregateRootUuid.requireArgValid("zero", "00000000-0000-0000-0000-000000000000");
        AggregateRootUuid.requireArgValid("other", UUID.randomUUID().toString());

        try {
            AggregateRootUuid.requireArgValid("a", "");
            fail();
        } catch (final ConstraintViolationException ex) {
            assertThat(ex.getMessage()).isEqualTo("The argument 'a' is not valid: ''");
        }

        try {
            AggregateRootUuid.requireArgValid("a", "0");
            fail();
        } catch (final ConstraintViolationException ex) {
            assertThat(ex.getMessage()).isEqualTo("The argument 'a' is not valid: '0'");
        }

    }

}
