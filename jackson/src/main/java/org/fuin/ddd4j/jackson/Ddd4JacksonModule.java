package org.fuin.ddd4j.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.fuin.ddd4j.core.AggregateVersion;
import org.fuin.ddd4j.core.EntityId;
import org.fuin.ddd4j.core.EntityIdFactory;
import org.fuin.ddd4j.core.EntityIdPath;
import org.fuin.ddd4j.core.EventId;
import org.fuin.ddd4j.core.EventType;
import org.fuin.objects4j.common.ThreadSafe;
import org.fuin.objects4j.jackson.ValueObjectStringJacksonDeserializer;
import org.fuin.objects4j.jackson.ValueObjectStringJacksonSerializer;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Module that registers the adapters for the package.
 * <p>
 * Beside the fixed types of this library, a (de)serializer is registered for every identifier class the
 * {@link EntityIdFactory} knows - see {@link EntityIdFactory#getIdClasses()}. An identifier is a single
 * value, not a bean: without such a registration Jackson writes it as an object with a spurious
 * {@code baseType} property, and refuses to read it back. A factory that cannot enumerate its classes
 * (the default) simply contributes nothing here.
 */
@ThreadSafe
public class Ddd4JacksonModule extends Module {

    /**
     * The library's own types that {@link #setupModule(SetupContext)} registers explicitly. They are all
     * value objects with a base type, so {@link JandexJacksonModule} would otherwise find them by scanning
     * and replace the dedicated (de)serializers above with generic ones - keep both in sync.
     */
    static final Set<Class<?>> FIXED_TYPES = Set.of(
            AggregateVersion.class, EntityIdPath.class, EventId.class, EventType.class);

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

        final SimpleDeserializers deserializers = new SimpleDeserializers();
        deserializers.addDeserializer(AggregateVersion.class, new AggregateVersionJacksonDeserializer());
        deserializers.addDeserializer(EntityIdPath.class, new EntityIdPathJacksonDeserializer(entityIdFactory));
        deserializers.addDeserializer(EventId.class, new ValueObjectStringJacksonDeserializer<>(EventId.class, EventId::valueOf));
        deserializers.addDeserializer(EventType.class, new ValueObjectStringJacksonDeserializer<>(EventType.class, str -> new EventType(Objects.requireNonNull(str))));

        for (final Class<? extends EntityId> idClass : entityIdFactory.getIdClasses()) {
            addEntityId(serializers, deserializers, idClass);
        }

        context.addSerializers(serializers);
        context.addDeserializers(deserializers);
    }

    /**
     * Registers the pair of (de)serializers for a single identifier class. The method exists only to
     * capture the wildcard of the scanned class in a type variable the (de)serializers can be built with.
     *
     * @param serializers Serializers to add to.
     * @param deserializers Deserializers to add to.
     * @param idClass Identifier class to register.
     *
     * @param <T> Type of the identifier.
     */
    private <T extends EntityId> void addEntityId(final SimpleSerializers serializers,
                                                  final SimpleDeserializers deserializers,
                                                  final Class<T> idClass) {
        serializers.addSerializer(new EntityIdJacksonSerializer<>(idClass));
        deserializers.addDeserializer(idClass, new EntityIdJacksonDeserializer<>(idClass, entityIdFactory));
    }

    @Override
    public Version version() {
        // Don't forget to change from release to SNAPSHOT and back!
        return new Version(0, 7, 0, "",
            "org.fuin.ddd4j", "ddd-4-java-jackson");
    }

}