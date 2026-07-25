package org.fuin.ddd4j.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import org.fuin.ddd4j.core.EntityId;
import org.fuin.objects4j.common.AsStringCapable;
import org.fuin.objects4j.common.HasPublicStaticValueOfMethod;
import org.fuin.objects4j.common.ThreadSafe;
import org.fuin.objects4j.common.ValueObjectWithBaseType;
import org.fuin.objects4j.jackson.ValueObjectJacksonDeserializer;
import org.fuin.objects4j.jackson.ValueObjectJacksonSerializer;
import org.fuin.objects4j.jackson.ValueObjectStringJacksonDeserializer;
import org.fuin.objects4j.jackson.ValueObjectStringJacksonSerializer;
import org.fuin.utils4j.jandex.JandexIndexFileReader;
import org.fuin.utils4j.jandex.JandexUtils;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.CompositeIndex;
import org.jboss.jandex.DotName;
import org.jboss.jandex.IndexView;
import org.jboss.jandex.Indexer;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * Module that registers a Jackson (de)serializer for every {@link ValueObjectWithBaseType} found by
 * scanning the Jandex index.
 * <p>
 * A value object is a single value, not a bean: without such a registration Jackson writes a
 * {@code CategoryName} as <code>{"value":"Office supplies","baseType":"java.lang.String"}</code> and
 * then refuses to read it back. Registering them one by one works, but the list is mechanical, has to
 * be extended whenever a new value object is added, and a forgotten entry is not a compile error - it
 * is a runtime failure in whatever writes JSON first.
 * <p>
 * Entity identifiers are deliberately <b>not</b> handled here, although they are value objects with a
 * base type too ({@code AggregateRootUuid} wraps a {@code UUID}): {@link Ddd4JacksonModule} already
 * registers a pair for every class its {@code EntityIdFactory} knows, and that pair writes the type
 * along with the value ({@code "Category f918869c-..."}). A registration made here would silently
 * replace it with a bare UUID, so identifiers are skipped - as are this library's own value objects
 * listed in {@link Ddd4JacksonModule#FIXED_TYPES}, for the same reason.
 * <p>
 * Both modules are needed: this one on its own covers no identifier, and
 * {@link Ddd4JacksonModule} on its own covers no value object.
 * <p>
 * <b>Name the application's packages</b> ({@link #JandexJacksonModule(String...)}). The libraries on the
 * class path contain value objects of their own, and several of them are already registered with a
 * dedicated (de)serializer by their own module - {@code CurrencyAmount} by {@code Objects4JJacksonModule}
 * and {@code TypeName} by {@code EscJacksonModule}, for example. Registering them a second time here
 * would leave it to the order in which the modules happen to reach the mapper which of the two wins, and
 * the two do not always agree on the JSON. Scanning everything is fine in a test, where there is a single
 * module and the ambiguity cannot arise.
 */
@ThreadSafe
public final class JandexJacksonModule extends Module {

    private static final Logger LOG = LoggerFactory.getLogger(JandexJacksonModule.class);

    private final List<Class<?>> valueObjectClasses;

    /**
     * Constructor that scans everything on the class path.
     */
    public JandexJacksonModule() {
        this(List.of(), new File("target/classes"));
    }

    /**
     * Constructor that scans only the given packages - the normal choice for an application.
     *
     * @param packagePrefixes Packages the value objects of the application live in, for example
     *                        {@code "com.example.myapp"}. Nothing outside them is registered.
     */
    public JandexJacksonModule(final String... packagePrefixes) {
        this(Arrays.asList(packagePrefixes), new File("target/classes"));
    }

    /**
     * Constructor with packages and classes directories. The directories are most likely only used in
     * tests - a packaged application is found through the index files on the class path.
     *
     * @param packagePrefixes Packages to scan. An empty list means everything on the class path.
     * @param classesDirs Directories with class files.
     */
    public JandexJacksonModule(final List<String> packagePrefixes, final File... classesDirs) {
        super();
        this.valueObjectClasses = scanForValueObjectClasses(packagePrefixes, Arrays.asList(classesDirs));
    }

    /**
     * Returns the value object classes a (de)serializer is registered for.
     *
     * @return Value object classes, sorted by name.
     */
    public List<Class<?>> getValueObjectClasses() {
        return Collections.unmodifiableList(valueObjectClasses);
    }

    @Override
    public String getModuleName() {
        return "Ddd4JJandexModule";
    }

    @Override
    public void setupModule(final SetupContext context) {

        final SimpleSerializers serializers = new SimpleSerializers();
        final SimpleDeserializers deserializers = new SimpleDeserializers();
        for (final Class<?> clasz : valueObjectClasses) {
            register(serializers, deserializers, clasz);
        }
        context.addSerializers(serializers);
        context.addDeserializers(deserializers);

    }

    @Override
    public Version version() {
        // Don't forget to change from release to SNAPSHOT and back!
        return new Version(0, 8, 0, "SNAPSHOT",
                "org.fuin.ddd4j", "ddd-4-java-jackson");
    }

    private static void register(final SimpleSerializers serializers,
                                 final SimpleDeserializers deserializers,
                                 final Class<?> clasz) {

        final Class<?> baseType = baseTypeOf(clasz);
        if (baseType == null) {
            LOG.warn("Ignored value object (type argument of {} is not a concrete class): {}",
                    ValueObjectWithBaseType.class.getSimpleName(), clasz.getName());
            return;
        }

        final Function<Object, Object> factory = factoryFor(clasz, baseType);
        if (factory == null) {
            LOG.warn("Ignored value object (neither a public static factory method nor a public constructor "
                            + "taking a single {} argument): {}",
                    baseType.getSimpleName(), clasz.getName());
            return;
        }

        // The string pair writes asString(); it needs the value object to be capable of that. Everything
        // else - including a value object wrapping a string that is not AsStringCapable - goes through
        // the generic pair, which hands the base value to the mapper. The mapper's configuration for the
        // base type therefore still applies, so a BigDecimal keeps its plain notation.
        if (baseType == String.class && AsStringCapable.class.isAssignableFrom(clasz)) {
            addStringBased(serializers, deserializers, clasz, factory);
        } else {
            addBaseTyped(serializers, deserializers, clasz, baseType, factory);
        }

        LOG.info("Added value object to {}: {} ({})", JandexJacksonModule.class.getSimpleName(),
                clasz.getName(), baseType.getSimpleName());

    }

    @SuppressWarnings("unchecked")
    private static <T extends AsStringCapable> void addStringBased(final SimpleSerializers serializers,
                                                                   final SimpleDeserializers deserializers,
                                                                   final Class<?> clasz,
                                                                   final Function<Object, Object> factory) {
        final Class<T> type = (Class<T>) clasz;
        serializers.addSerializer(new ValueObjectStringJacksonSerializer<>(type));
        deserializers.addDeserializer(type,
                new ValueObjectStringJacksonDeserializer<>(type, str -> (T) factory.apply(str)));
    }

    @SuppressWarnings("unchecked")
    private static <B, T extends ValueObjectWithBaseType<B>> void addBaseTyped(final SimpleSerializers serializers,
                                                                              final SimpleDeserializers deserializers,
                                                                              final Class<?> clasz,
                                                                              final Class<?> baseType,
                                                                              final Function<Object, Object> factory) {
        final Class<T> type = (Class<T>) clasz;
        final Class<B> base = (Class<B>) baseType;
        serializers.addSerializer(new ValueObjectJacksonSerializer<B, T>(type));
        deserializers.addDeserializer(type,
                new ValueObjectJacksonDeserializer<>(type, base, value -> (T) factory.apply(value)));
    }

    private static List<Class<?>> scanForValueObjectClasses(final List<String> packagePrefixes,
                                                            final List<File> classesDirs) {
        final List<IndexView> indexes = new ArrayList<>();
        indexes.add(new JandexIndexFileReader.Builder().addDefaultResource().build().loadR());
        indexes.add(indexClassesDirs(classesDirs));
        return findValueObjectClasses(CompositeIndex.create(indexes), packagePrefixes);
    }

    private static IndexView indexClassesDirs(final List<File> classesDirs) {
        final Indexer indexer = new Indexer();
        final List<File> knownClassFiles = new ArrayList<>();
        for (final File classesDir : classesDirs) {
            JandexUtils.indexDir(indexer, knownClassFiles, classesDir);
        }
        return indexer.complete();
    }

    private static List<Class<?>> findValueObjectClasses(final IndexView index, final List<String> packagePrefixes) {
        final Set<Class<?>> classes = new LinkedHashSet<>();
        for (final ClassInfo classInfo : index.getAllKnownImplementors(DotName.createSimple(ValueObjectWithBaseType.class))) {
            if (Modifier.isAbstract(classInfo.flags())
                    || Modifier.isInterface(classInfo.flags())
                    || classInfo.name().toString().contains("$")
                    || !inScannedPackage(classInfo.name().toString(), packagePrefixes)) {
                continue;
            }
            final Class<?> clasz = JandexUtils.loadClass(classInfo.name());
            if (EntityId.class.isAssignableFrom(clasz) || Ddd4JacksonModule.FIXED_TYPES.contains(clasz)) {
                LOG.debug("Ignored type handled by {}: {}",
                        Ddd4JacksonModule.class.getSimpleName(), clasz.getName());
                continue;
            }
            classes.add(clasz);
        }
        // The index has no defined order, so sort to make the registrations and the log reproducible
        return classes.stream().sorted(Comparator.comparing(Class::getName)).toList();
    }

    /**
     * Answers whether a class is inside the packages that were asked for.
     *
     * @param className Fully qualified class name.
     * @param packagePrefixes Packages to scan. An empty list means everything on the class path.
     *
     * @return TRUE if a (de)serializer should be registered for the class.
     */
    private static boolean inScannedPackage(final String className, final List<String> packagePrefixes) {
        if (packagePrefixes.isEmpty()) {
            return true;
        }
        for (final String prefix : packagePrefixes) {
            if (className.startsWith(prefix + ".")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines the type a value object wraps by resolving the type argument of
     * {@link ValueObjectWithBaseType} - which may be declared anywhere up the hierarchy, as
     * {@code getBaseType()} cannot be asked without an instance.
     *
     * @param type Type to inspect.
     *
     * @return Base type or {@literal null} if it is not a concrete class (a type variable, for example).
     */
    @Nullable
    private static Class<?> baseTypeOf(final Type type) {
        if (type instanceof ParameterizedType parameterizedType) {
            if (ValueObjectWithBaseType.class.equals(parameterizedType.getRawType())) {
                final Type argument = parameterizedType.getActualTypeArguments()[0];
                return argument instanceof Class<?> clasz ? clasz : null;
            }
            return baseTypeOf(parameterizedType.getRawType());
        }
        if (type instanceof Class<?> clasz) {
            for (final Type implemented : clasz.getGenericInterfaces()) {
                final Class<?> found = baseTypeOf(implemented);
                if (found != null) {
                    return found;
                }
            }
            final Type superclass = clasz.getGenericSuperclass();
            if (superclass != null) {
                return baseTypeOf(superclass);
            }
        }
        return null;
    }

    /**
     * Determines how an instance is created from its base value. The generated value objects are not
     * consistent about this - some have a public static {@code valueOf}, others only a public
     * constructor - so both are tried.
     *
     * @param clasz Value object class.
     * @param baseType Type the value object wraps.
     *
     * @return Factory or {@literal null} if the class offers neither.
     */
    @Nullable
    private static Function<Object, Object> factoryFor(final Class<?> clasz, final Class<?> baseType) {

        final Method method = findStaticFactoryMethod(clasz, baseType);
        if (method != null) {
            return arg -> {
                try {
                    return method.invoke(null, arg);
                } catch (final ReflectiveOperationException ex) {
                    throw new IllegalStateException("Failed to call: " + method, ex);
                }
            };
        }

        final Constructor<?> constructor = findConstructor(clasz, baseType);
        if (constructor != null) {
            return arg -> {
                try {
                    return constructor.newInstance(arg);
                } catch (final ReflectiveOperationException ex) {
                    throw new IllegalStateException("Failed to call: " + constructor, ex);
                }
            };
        }

        return null;

    }

    @Nullable
    private static Method findStaticFactoryMethod(final Class<?> clasz, final Class<?> baseType) {
        try {
            final Method method = clasz.getMethod(factoryMethodName(clasz, baseType), baseType);
            if (Modifier.isStatic(method.getModifiers()) && clasz.isAssignableFrom(method.getReturnType())) {
                return method;
            }
            return null;
        } catch (final NoSuchMethodException ex) {
            return null;
        }
    }

    /**
     * Returns the name of the factory method to look for. {@link HasPublicStaticValueOfMethod} names it
     * along with the parameter type it takes, and is repeatable, so a value object may declare one for
     * its base type and another for a string. Classes without the annotation use the default name.
     *
     * @param clasz Value object class.
     * @param baseType Type the value object wraps.
     *
     * @return Method name.
     */
    private static String factoryMethodName(final Class<?> clasz, final Class<?> baseType) {
        for (final HasPublicStaticValueOfMethod annotation : clasz.getAnnotationsByType(HasPublicStaticValueOfMethod.class)) {
            if (annotation.param().equals(baseType)) {
                return annotation.method();
            }
        }
        return "valueOf";
    }

    @Nullable
    private static Constructor<?> findConstructor(final Class<?> clasz, final Class<?> baseType) {
        try {
            return clasz.getConstructor(baseType);
        } catch (final NoSuchMethodException ex) {
            return null;
        }
    }

}
