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
package org.fuin.ddd4j.jsonb;

import org.fuin.ddd4j.core.EventId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class EventIdJsonbAdapterTest {

    @Test
    public void testUnmarshal() throws Exception {

        // PREPARE
        final UUID id = UUID.randomUUID();
        final EventIdJsonbAdapter testee = new EventIdJsonbAdapter();

        // TEST
        final EventId eventId = testee.adaptFromJson(id.toString());

        // VERIFY
        assertThat(eventId.asBaseType()).isEqualTo(id);
        assertThat(eventId.asString()).isEqualTo(id.toString());

        // TEST & VERIFY
        assertThat(testee.adaptFromJson(null)).isNull();

    }

    @Test
    public void testMarshal() throws Exception {

        // PREPARE
        final UUID id = UUID.randomUUID();
        final EventIdJsonbAdapter testee = new EventIdJsonbAdapter();
        final EventId eventId = new EventId(id);

        // TEST & VERIFY
        assertThat(testee.adaptToJson(eventId)).isEqualTo(id.toString());
        assertThat(testee.adaptToJson(null)).isNull();

    }

}
