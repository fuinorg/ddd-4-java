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

import org.fuin.objects4j.common.ConstraintViolationException;
import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

/**
 * Test for {@link EntityIntegerId}.
 */
public class EntityIntegerIdTest {

    @Test
    public void testEqualsHashCode() {
        EqualsVerifier.forClass(EntityIntegerId.class)
                .withPrefabValues(EntityType.class, new StringBasedEntityType("A"), new StringBasedEntityType("B"))
                .withPrefabValues(Integer.class, 1, 2).suppress(Warning.NULL_FIELDS).verify();
    }

    @Test
    public void testIsValid() {

        assertThat(EntityIntegerId.isValid(null)).isTrue();
        assertThat(EntityIntegerId.isValid("")).isFalse();
        assertThat(EntityIntegerId.isValid("-1")).isFalse();
        assertThat(EntityIntegerId.isValid("0")).isFalse();
        assertThat(EntityIntegerId.isValid("1")).isTrue();
        assertThat(EntityIntegerId.isValid("" + Integer.MAX_VALUE)).isTrue();
        assertThat(EntityIntegerId.isValid("a")).isFalse();

    }

    @Test
    public void testRequireArgValid() {

        EntityIntegerId.requireArgValid("null", null);
        EntityIntegerId.requireArgValid("other", "1");
        EntityIntegerId.requireArgValid("max", "" + Integer.MAX_VALUE);

        try {
            EntityIntegerId.requireArgValid("a", "");
            fail();
        } catch (final ConstraintViolationException ex) {
            assertThat(ex.getMessage()).isEqualTo("The argument 'a' is not valid: ''");
        }

        try {
            EntityIntegerId.requireArgValid("b", "-1");
            fail();
        } catch (final ConstraintViolationException ex) {
            assertThat(ex.getMessage()).isEqualTo("The argument 'b' is not valid: '-1'");
        }

        try {
            EntityIntegerId.requireArgValid("c", "0");
            fail();
        } catch (final ConstraintViolationException ex) {
            assertThat(ex.getMessage()).isEqualTo("The argument 'c' is not valid: '0'");
        }

    }

}
