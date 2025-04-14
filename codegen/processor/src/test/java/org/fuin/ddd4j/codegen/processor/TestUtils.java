package org.fuin.ddd4j.codegen.processor;

import io.toolisticon.cute.CuteApi;

/**
 * Helper functions for the package.
 */
final class TestUtils {

    /**
     * Runs an annotation test case.
     *
     * @param compileTestBuilder   Compile test builder to use.
     * @param targetName           Unique simple name of the target java class.
     * @param sourceWithAnnotation Source code with annotation used to generate the target code.
     */
    static void testAnnotation(CuteApi.BlackBoxTestSourceFilesInterface compileTestBuilder,
                               String targetName,
                               String testCase,
                               String sourceWithAnnotation) {
        final String expectedFileName = "/expected/" + targetName + "/" + testCase + ".java";
        compileTestBuilder
                .andSourceFile("input." + targetName + "Example", sourceWithAnnotation)
                .whenCompiled()
                .thenExpectThat()
                .compilationSucceeds()
                .andThat()
                .generatedSourceFile("input." + targetName)
                .exists()
                .andThat()
                .generatedSourceFile("input." + targetName)
                .matches(new ExpectedFileMatcher(expectedFileName))
                .executeTest();
    }

}
