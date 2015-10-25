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
package org.fuin.ddd4j.common;

import static org.fest.assertions.Assertions.assertThat;

import java.time.ZonedDateTime;

import org.junit.Test;

/**
 * Tests the {@link ZonedZonedDateTimeAdapter}.
 */
// CHECKSTYLE:OFF Test code
public final class ZonedDateTimeAdapterTest {

    @Test
    public final void testMarshalUnmarshal() {

        // PREPARE
        final ZonedDateTimeAdapter testee = new ZonedDateTimeAdapter();
        final ZonedDateTime now = ZonedDateTime.now();

        // TEST
        final String nowStr = testee.marshal(now);
        final ZonedDateTime copy = testee.unmarshal(nowStr);

        // VERIFY
        assertThat(copy).isEqualTo(now);

    }

}
// CHECKSTYLE:ON
