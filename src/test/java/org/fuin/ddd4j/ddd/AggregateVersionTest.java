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

//CHECKSTYLE:OFF
public class AggregateVersionTest {

    @Test
    public void testCreateValid() {
        
        assertThat(new AggregateVersion(0).asBaseType()).isEqualTo(0);
        assertThat(new AggregateVersion(1).asBaseType()).isEqualTo(1);
        assertThat(new AggregateVersion(Integer.MAX_VALUE).asBaseType()).isEqualTo(Integer.MAX_VALUE);
        
    }

    @Test
    public void testCreateNull() {

        try {
            new AggregateVersion(null);
            fail("Expected exception");
        } catch (final ConstraintViolationException ex) {
            assertThat(ex.getMessage()).isEqualTo("The argument 'version' cannot be null");
        }
        
        
    }

    @Test
    public void testCreateNegative() {

        try {
            new AggregateVersion(-1);
            fail("Expected exception");
        } catch (final ConstraintViolationException ex) {
            assertThat(ex.getMessage()).isEqualTo("Min value of argument 'version' is 0, but was: -1");
        }
        
    }

    @Test
    public void testIsValid() {
        
        assertThat(AggregateVersion.isValid(null)).isTrue();
        assertThat(AggregateVersion.isValid(-1)).isFalse();
        assertThat(AggregateVersion.isValid(0)).isTrue();
        assertThat(AggregateVersion.isValid(1)).isTrue();
        assertThat(AggregateVersion.isValid(Integer.MAX_VALUE)).isTrue();
        
    }

    @Test
    public void testValueOf() {
        
        assertThat(AggregateVersion.valueOf(null)).isNull();
        assertThat(AggregateVersion.valueOf(0)).isEqualTo(new AggregateVersion(0));
        assertThat(AggregateVersion.valueOf(1)).isEqualTo(new AggregateVersion(1));
        assertThat(AggregateVersion.valueOf(Integer.MAX_VALUE)).isEqualTo(new AggregateVersion(Integer.MAX_VALUE));
        
    }
    
}
//CHECKSTYLE:ON
