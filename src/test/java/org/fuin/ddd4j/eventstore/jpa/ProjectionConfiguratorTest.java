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

import org.fuin.units4j.AbstractPersistenceTest;
import org.junit.Ignore;
import org.junit.Test;

//CHECKSTYLE:OFF
public class ProjectionConfiguratorTest extends AbstractPersistenceTest {

    @Ignore("Cannot be executed with HSQL as the SQL in the config is Oracle specific")
    @Test
    public void testPersist() {

	// PREPARE
	final ProjectionConfigurator testee = new ProjectionConfigurator(
		getEm());

	// TEST
	beginTransaction();
	testee.execute();
	commitTransaction();

	// VERIFY
	beginTransaction();
	assertThat(true).isTrue();
	commitTransaction();

    }

}
// CHECKSTYLE:ON
