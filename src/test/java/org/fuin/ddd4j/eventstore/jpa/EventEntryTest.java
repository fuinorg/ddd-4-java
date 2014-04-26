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
package org.fuin.ddd4j.eventstore.jpa;

import static org.fest.assertions.Assertions.assertThat;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.UUID;

import org.fuin.ddd4j.eventstore.intf.Data;
import org.fuin.units4j.AbstractPersistenceTest;
import org.junit.Test;

// CHECKSTYLE:OFF
public final class EventEntryTest extends AbstractPersistenceTest {

	@Test
	public void testPersist() {

		// PREPARE
		final UUID uuid = UUID.randomUUID();
		final String type = "HelloWorld";
		final int version = 1;
		final String xml = "<hello name=\"world\" />";
		final EventEntry testee = create(uuid, type, version, xml);

		// beginTransaction();
		// Nothing to prepare here...
		// commitTransaction();

		// TEST
		beginTransaction();
		getEm().persist(testee);
		commitTransaction();

		// VERIFY
		beginTransaction();
		final EventEntry found = getEm()
				.find(EventEntry.class, uuid.toString());
		assertThat(found).isNotNull();
		assertThat(found.getId()).isEqualTo(uuid.toString());
		assertThat(found.getTimestamp()).isNotNull();
		assertThat(found.getData()).isNotNull();
		assertThat(found.getData().getType()).isEqualTo(type);
		assertThat(found.getData().getVersion()).isEqualTo(version);
		assertThat(found.getData().getRaw()).isNotNull();
		assertThat(found.getMeta()).isNull();
		final String data = new String(found.getData().getRaw(), found
				.getData().getEncoding());
		assertThat(data).isEqualTo(xml);
		commitTransaction();

	}

	private EventEntry create(final UUID uuid, final String type,
			final int version, final String xml) {
		final Charset encoding = Charset.forName("utf-8");
		final EventEntry eventEntry = new EventEntry(uuid.toString(),
				new Date(), new Data(type, version, "application/xml",
						encoding, xml.getBytes(encoding)));
		return eventEntry;
	}

}
// CHECKSTYLE:ON
