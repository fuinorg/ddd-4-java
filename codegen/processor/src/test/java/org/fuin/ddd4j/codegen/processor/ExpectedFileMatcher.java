package org.fuin.ddd4j.codegen.processor;

import io.toolisticon.cute.GeneratedFileObjectMatcher;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.tools.FileObject;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

class ExpectedFileMatcher implements GeneratedFileObjectMatcher {

    private final String expected;

    public ExpectedFileMatcher(final String expectedResource) {
        try {
            try (InputStream resourceAsStream = getClass().getResourceAsStream(expectedResource)) {
                if (resourceAsStream == null) {
                    throw new IllegalStateException(expectedResource + " not found");
                }
                expected = IOUtils.toString(resourceAsStream, StandardCharsets.UTF_8);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Failed to read resource: " + expectedResource, ex);
        }
    }

    @Override
    public boolean check(FileObject fileObject) throws IOException {
        final String actual = fileObject.getCharContent(false).toString();
        final boolean ok = actual.equals(expected);
        if (!ok) {
            FileUtils.write(new File("target/" + StringVOTemplateTest.class.getSimpleName() + "EXPECTED.java"), expected, StandardCharsets.UTF_8);
            FileUtils.write(new File("target/" + StringVOTemplateTest.class.getSimpleName() + "ACTUAL.java"), actual, StandardCharsets.UTF_8);
        }
        return ok;
    }

}
