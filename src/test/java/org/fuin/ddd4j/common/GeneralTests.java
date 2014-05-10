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
package org.fuin.ddd4j.common;

import java.io.File;

import org.fuin.units4j.AssertCoverage;
import org.fuin.units4j.AssertDependencies;
import org.junit.Test;

// CHECKSTYLE:OFF
public class GeneralTests {

    @Test
    public final void testCoverage() {
	AssertCoverage.assertEveryClassHasATest(new File("src/main/java"));
    }

    @Test
    public final void testAssertDependencies() {
	final File classesDir = new File("target/classes");
	AssertDependencies.assertRules(this.getClass(),
		"/ddd4j-dependencies.xml", classesDir);
    }

}
// CHECKSTYLE:ON
