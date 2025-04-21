package org.fuin.ddd4j.jacksontest;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import org.fuin.ddd4j.core.EntityIdFactory;
import org.fuin.ddd4j.jackson.EntityIdJacksonDeserializer;
import org.fuin.ddd4j.jackson.EntityIdJacksonSerializer;
import org.fuin.objects4j.jackson.ValueObjectStringJacksonDeserializer;
import org.fuin.objects4j.jackson.ValueObjectStringJacksonSerializer;

import java.util.Objects;

/**
 * Module that registers the adapters for the package.
 */
public class TestJacksonAdapterModule extends Module {

    private final EntityIdFactory entityIdFactory;

    /**
     * Constructor with factory.
     *
     * @param entityIdFactory Entity ID factory.
     */
    public TestJacksonAdapterModule(final EntityIdFactory entityIdFactory) {
        super();
        this.entityIdFactory = Objects.requireNonNull(entityIdFactory, "entityIdFactory==null");
    }

    @Override
    public String getModuleName() {
        return "TestModule";
    }

    @Override
    public void setupModule(SetupContext context) {

        final SimpleSerializers serializers = new SimpleSerializers();
        serializers.addSerializer(new EntityIdJacksonSerializer<>(AId.class));
        serializers.addSerializer(new EntityIdJacksonSerializer<>(BId.class));
        serializers.addSerializer(new EntityIdJacksonSerializer<>(CId.class));
        serializers.addSerializer(new EntityIdJacksonSerializer<>(PersonId.class));
        serializers.addSerializer(new ValueObjectStringJacksonSerializer<>(PersonName.class));
        serializers.addSerializer(new EntityIdJacksonSerializer<>(VendorId.class));
        serializers.addSerializer(new ValueObjectStringJacksonSerializer<>(VendorName.class));
        serializers.addSerializer(new ValueObjectStringJacksonSerializer<>(VendorKey.class));
        context.addSerializers(serializers);

        final SimpleDeserializers deserializers = new SimpleDeserializers();
        deserializers.addDeserializer(AId.class, new EntityIdJacksonDeserializer<>(AId.class, entityIdFactory));
        deserializers.addDeserializer(BId.class, new EntityIdJacksonDeserializer<>(BId.class, entityIdFactory));
        deserializers.addDeserializer(CId.class, new EntityIdJacksonDeserializer<>(CId.class, entityIdFactory));
        deserializers.addDeserializer(PersonId.class, new EntityIdJacksonDeserializer<>(PersonId.class, entityIdFactory));
        deserializers.addDeserializer(PersonName.class, new ValueObjectStringJacksonDeserializer<>(PersonName.class, PersonName::valueOf));
        deserializers.addDeserializer(VendorId.class, new EntityIdJacksonDeserializer<>(VendorId.class, entityIdFactory));
        deserializers.addDeserializer(VendorName.class, new ValueObjectStringJacksonDeserializer<>(VendorName.class, VendorName::valueOf));
        deserializers.addDeserializer(VendorKey.class, new ValueObjectStringJacksonDeserializer<>(VendorKey.class, VendorKey::valueOf));
        context.addDeserializers(deserializers);
    }

    @Override
    public Version version() {
        // Don't forget to change from release to SNAPSHOT and back!
        return new Version(0, 7, 0, "SNAPSHOT");
    }

}