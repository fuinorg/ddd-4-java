package org.fuin.ddd4j.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.fuin.ddd4j.core.EntityIdFactory;
import org.fuin.ddd4j.jacksontest.JacksonTestEntityIdFactory;
import org.fuin.ddd4j.jacksontest.TestJacksonAdapterModule;
import org.fuin.utils4j.TestOmitted;

/**
 * Utils for the package.
 */
@TestOmitted("Only a test class")
final class TestUtils {

    private static final EntityIdFactory ENTITY_ID_FACTORY = new JacksonTestEntityIdFactory();

    private TestUtils() {
    }

    /**
     * Creates an instance with the configured values.
     *
     * @return New instance.
     */
    public static ObjectMapper objectMapper() {
        return new ObjectMapper()
                .registerModule(new Ddd4JacksonModule(ENTITY_ID_FACTORY))
                .registerModule(new TestJacksonAdapterModule(ENTITY_ID_FACTORY));
    }

}
