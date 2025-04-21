package org.fuin.ddd4j.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.fuin.ddd4j.core.AggregateVersion;
import org.fuin.ddd4j.core.EntityIdFactory;
import org.fuin.ddd4j.core.EntityIdPath;
import org.fuin.ddd4j.core.EventId;
import org.fuin.ddd4j.core.EventType;
import org.fuin.objects4j.jackson.ValueObjectStringJacksonDeserializer;
import org.fuin.objects4j.jackson.ValueObjectStringJacksonSerializer;
import org.fuin.utils4j.TestOmitted;

import java.util.List;
import java.util.Objects;

/**
 * Module that registers the adapters for the package.
 */
@TestOmitted("Already tested with other tests")
public class Ddd4JacksonModule extends Module {

    private final EntityIdFactory entityIdFactory;

    /**
     * Constructor with entity identifier factory.
     *
     * @param entityIdFactory Factory used to create entity identifiers.
     */
    public Ddd4JacksonModule(EntityIdFactory entityIdFactory) {
        this.entityIdFactory = Objects.requireNonNull(entityIdFactory, "entityIdFactory==null");
    }

    @Override
    public String getModuleName() {
        return "Ddd4JModule";
    }

    @Override
    public Iterable<? extends Module> getDependencies() {
        return List.of(new JavaTimeModule());
    }

    @Override
    public void setupModule(SetupContext context) {

        final SimpleSerializers serializers = new SimpleSerializers();
        serializers.addSerializer(new AggregateVersionJacksonSerializer());
        serializers.addSerializer(new ValueObjectStringJacksonSerializer<>(EntityIdPath.class));
        serializers.addSerializer(new ValueObjectStringJacksonSerializer<>(EventId.class));
        serializers.addSerializer(new ValueObjectStringJacksonSerializer<>(EventType.class));
        context.addSerializers(serializers);

        final SimpleDeserializers deserializers = new SimpleDeserializers();
        deserializers.addDeserializer(AggregateVersion.class, new AggregateVersionJacksonDeserializer());
        deserializers.addDeserializer(EntityIdPath.class, new EntityIdPathJacksonDeserializer(entityIdFactory));
        deserializers.addDeserializer(EventId.class, new ValueObjectStringJacksonDeserializer<>(EventId.class, EventId::valueOf));
        deserializers.addDeserializer(EventType.class, new ValueObjectStringJacksonDeserializer<>(EventType.class, EventType::new));
        context.addDeserializers(deserializers);
    }

    @Override
    public Version version() {
        // Don't forget to change from release to SNAPSHOT and back!
        return new Version(0, 7, 0, "SNAPSHOT");
    }

}