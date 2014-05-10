/**
 * Copyright (C) 2013 Future Invent Informationsmanagement GmbH. All rights
 * reserved. <http://www.fuin.org/>
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
 * along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fuin.ddd4j.ddd;

import static org.fest.assertions.Assertions.assertThat;
import static org.fuin.units4j.Units4JUtils.deserialize;
import static org.fuin.units4j.Units4JUtils.marshal;
import static org.fuin.units4j.Units4JUtils.serialize;
import static org.fuin.units4j.Units4JUtils.unmarshal;

import org.joda.time.DateTime;
import org.junit.Test;

// CHECKSTYLE:OFF
public final class EventMetaDataTest {

    @Test
    public final void testSerializeDeserialize() {

	// PREPARE
	final EventMetaData original = createTestee();

	// TEST
	final EventMetaData copy = deserialize(serialize(original));

	// VERIFY
	assertThat(original).isEqualTo(copy);

    }

    @Test
    public final void testMarshalUnmarshal() {

	// PREPARE
	final EventMetaData original = createTestee();

	// TEST
	final String xml = marshal(original, EventMetaData.class);
	final EventMetaData copy = unmarshal(xml, EventMetaData.class);

	// VERIFY
	assertThat(original).isEqualTo(copy);

    }

    private EventMetaData createTestee() {
	return new EventMetaData("127.0.0.1", 1, "REMOTE_USER", "127.0.0.2", 2,
		"USER", new DateTime(), new DateTime());
    }

}
// CHECKSTYLE:ON
