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
