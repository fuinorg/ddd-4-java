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

import static org.fest.assertions.Assertions.assertThat;
import static org.fuin.utils4j.Utils4J.deserialize;
import static org.fuin.utils4j.JaxbUtils.marshal;
import static org.fuin.utils4j.Utils4J.serialize;
import static org.fuin.utils4j.JaxbUtils.unmarshal;

import java.time.ZonedDateTime;

import org.junit.Test;

// CHECKSTYLE:OFF
public final class BasicEventMetaDataTest {

    @Test
    public final void testSerializeDeserialize() {

        // PREPARE
        final BasicEventMetaData original = createTestee();

        // TEST
        final BasicEventMetaData copy = deserialize(serialize(original));

        // VERIFY
        assertThat(original).isEqualTo(copy);

    }

    @Test
    public final void testMarshalUnmarshal() {

        // PREPARE
        final BasicEventMetaData original = createTestee();

        // TEST
        final String xml = marshal(original, BasicEventMetaData.class);
        final BasicEventMetaData copy = unmarshal(xml, BasicEventMetaData.class);

        // VERIFY
        assertThat(original).isEqualTo(copy);

    }

    private BasicEventMetaData createTestee() {
        return new BasicEventMetaData("127.0.0.1", 1, "REMOTE_USER", "127.0.0.2", 2, "USER",
                ZonedDateTime.now(), ZonedDateTime.now());
    }

}
// CHECKSTYLE:ON
