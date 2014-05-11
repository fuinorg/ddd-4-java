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
import static org.fuin.units4j.Units4JUtils.deserialize;
import static org.fuin.units4j.Units4JUtils.marshal;
import static org.fuin.units4j.Units4JUtils.serialize;
import static org.fuin.units4j.Units4JUtils.unmarshal;

import org.junit.Test;

//CHECKSTYLE:OFF
public class ProjectionConfigTest {
    
    private static final String TRIGGER_SQL = "trigger";
    private static final String NAME = "ProjectionX";
    private static final String INSERT_SQL = "insert";

    @Test
    public final void testSerializeDeserialize() {

	// PREPARE
	final ProjectionConfig original = createTestee();

	// TEST
	final ProjectionConfig copy = deserialize(serialize(original));

	// VERIFY
	assertThat(original).isEqualTo(copy);
	assertThat(original.getName()).isEqualTo(NAME);
	assertThat(original.getTriggerSql()).isEqualTo(TRIGGER_SQL);
	assertThat(original.getInsertSql()).isEqualTo(INSERT_SQL);

    }

    @Test
    public final void testMarshalUnmarshal() {

	// PREPARE
	final ProjectionConfig original = createTestee();

	// TEST
	final String xml = marshal(original, ProjectionConfig.class);
	System.out.println(xml);
	final ProjectionConfig copy = unmarshal(xml, ProjectionConfig.class);

	// VERIFY
	assertThat(original).isEqualTo(copy);
	assertThat(original.getName()).isEqualTo(NAME);
	assertThat(original.getTriggerSql()).isEqualTo(TRIGGER_SQL);
	assertThat(original.getInsertSql()).isEqualTo(INSERT_SQL);

    }

    private ProjectionConfig createTestee() {
	return new ProjectionConfig(NAME, TRIGGER_SQL, INSERT_SQL);
    }    

}
//CHECKSTYLE:ON
