package org.fuin.ddd4j.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.fuin.ddd4j.core.EntityId;

import java.io.IOException;

/**
 * Converts an entity identifier into a string (Jackson).
 */
public final class EntityIdJacksonSerializer<T extends EntityId> extends StdSerializer<T> {

    /**
     * Constructor with ID type.
     *
     * @param clasz Entity ID type.
     */
    public EntityIdJacksonSerializer(Class<T> clasz) {
        super(clasz);
    }

    public void serialize(T value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value == null) {
            gen.writeNull();
        } else {
            gen.writeString(value.asTypedString());
        }
    }

}
