package org.fuin.ddd4j.jsonb;

import com.tngtech.archunit.junit.ArchIgnore;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.json.bind.adapter.JsonbAdapter;
import jakarta.json.bind.config.BinaryDataStrategy;
import org.eclipse.yasson.FieldAccessStrategy;
import org.fuin.ddd4j.core.EntityIdFactory;
import org.fuin.ddd4j.jsonbtest.JsonbTestEntityIdFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Utils for the package.
 */
@ArchIgnore
final class TestUtils {

    private static final EntityIdFactory ENTITY_ID_FACTORY = new JsonbTestEntityIdFactory();

    private static final List<JsonbAdapter<?, ?>> JSONB_ADAPTERS =
            List.of(
                    new EntityIdJsonbAdapter(ENTITY_ID_FACTORY),
                    new EntityIdPathJsonbAdapter(ENTITY_ID_FACTORY),
                    new EventIdJsonbAdapter()
            );

    private TestUtils() {
    }

    /**
     * Creates an instance with the configured values.
     *
     * @return New instance.
     */
    public static Jsonb jsonb() {
        return JsonbBuilder.create(
                new JsonbConfig()
                        .withEncoding(StandardCharsets.UTF_8.name())
                        .withPropertyVisibilityStrategy(new FieldAccessStrategy())
                        .withAdapters(JSONB_ADAPTERS.toArray(new JsonbAdapter[0]))
                        .withBinaryDataStrategy(BinaryDataStrategy.BASE_64)
        );
    }

}
