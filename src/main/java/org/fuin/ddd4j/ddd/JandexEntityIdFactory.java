package org.fuin.ddd4j.ddd;

import org.fuin.objects4j.common.HasPublicStaticIsValidMethod;
import org.fuin.objects4j.common.HasPublicStaticIsValidMethodValidator;
import org.fuin.objects4j.common.HasPublicStaticValueOfMethod;
import org.fuin.objects4j.common.HasPublicStaticValueOfMethodValidator;
import org.fuin.utils4j.JandexIndexFileReader;
import org.fuin.utils4j.JandexIndexFileReader.Builder;
import org.fuin.utils4j.JandexUtils;
import org.jboss.jandex.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Function;

/**
 * Registry that is built up by scanning for classes that are annotated with:
 * <ul>
 * <li>{@link HasPublicStaticIsValidMethod}</li>
 * <li>{@link HasPublicStaticValueOfMethod} (with String parameter)</li>
 * <li>{@link HasEntityTypeConstant}</li>
 * </ul>
 * and implementing {@link EntityId}.
 */
public final class JandexEntityIdFactory implements EntityIdFactory {

    private static final Logger LOG = LoggerFactory.getLogger(JandexEntityIdFactory.class);

    private final List<File> classesDirs;

    private final Map<String, Function<String, EntityId>> valueOfMap;

    private final Map<String, Function<String, Boolean>> isValidMap;

    private final List<Class<?>> idClasses;

    /**
     * Default constructor.
     */
    public JandexEntityIdFactory() {
        this(new File("target/classes"));
    }

    /**
     * Constructor with classes directories. Most likely only used in tests.
     *
     * @param classesDirs Directories with class files.
     */
    public JandexEntityIdFactory(final File...classesDirs) {
        super();
        this.classesDirs = Arrays.asList(classesDirs);
        valueOfMap = new HashMap<>();
        isValidMap = new HashMap<>();
        idClasses = scanForEntityIdClasses();
        for (final Class<?> entityIdClass : idClasses) {
            valueOfMap.put(typeConstant(entityIdClass).asString(), valueOfMethod(entityIdClass));
            isValidMap.put(typeConstant(entityIdClass).asString(), isValidMethod(entityIdClass));
        }
    }

    @Override
    public EntityId createEntityId(final String type, final String id) {
        final Function<String, EntityId> factory = valueOfMap.get(type);
        if (factory == null) {
            throw new IllegalArgumentException("Unknown type: " + type + " (Known types are: " + getIdClasses() + ")");
        }
        return factory.apply(id);
    }

    @Override
    public boolean containsType(final String type) {
        return valueOfMap.containsKey(type);
    }

    @Override
    public boolean isValid(String type, String id) {
        final Function<String, Boolean> func = isValidMap.get(type);
        if (func == null) {
            return false;
        }
        return func.apply(id);
    }

    /**
     * Returns a list of known {@link EntityId} classes.
     *
     * @return Entity ID classes.
     */
    public List<Class<?>> getIdClasses() {
        return Collections.unmodifiableList(idClasses);
    }

    private List<Class<?>> scanForEntityIdClasses() {
        final List<IndexView> indexes = new ArrayList<>();
        indexes.add(new Builder().addDefaultResource().build().loadR());
        indexes.add(indexClassesDirs());
        return findEntityIdClasses(CompositeIndex.create(indexes));
    }

    private IndexView indexClassesDirs() {
        final Indexer indexer = new Indexer();
        final List<File> knownClassFiles = new ArrayList<>();
        for (final File classesDir : classesDirs) {
            JandexUtils.indexDir(indexer, knownClassFiles, classesDir);
        }
        return indexer.complete();
    }

    private static List<Class<?>> findEntityIdClasses(final IndexView index) {
        List<Class<?>> classes = new ArrayList<>();
        final Set<ClassInfo> classInfos = new HashSet<>();
        classInfos.addAll(index.getAllKnownImplementors(DotName.createSimple(EntityId.class)));
        classInfos.addAll(index.getAllKnownImplementors(DotName.createSimple(AggregateRootId.class)));
        for (final ClassInfo classInfo : classInfos) {
            if (!Modifier.isAbstract(classInfo.flags()) && !Modifier.isInterface(classInfo.flags())) {
                final Class<?> clasz = JandexUtils.loadClass(classInfo.name());
                boolean include = true;
                if (clasz.getAnnotation(HasPublicStaticIsValidMethod.class) == null) {
                    LOG.warn("Missing annotation @{} on {} class: {}", HasPublicStaticIsValidMethod.class.getSimpleName(), EntityId.class.getSimpleName(), clasz.getName());
                    include = false;
                }
                if (clasz.getAnnotation(HasPublicStaticValueOfMethod.class) == null) {
                    LOG.warn("Missing annotation @{} on {} class: {}", HasPublicStaticValueOfMethod.class.getSimpleName(), EntityId.class.getSimpleName(), clasz.getName());
                    include = false;
                }
                if (clasz.getAnnotation(HasEntityTypeConstant.class) == null) {
                    LOG.warn("Missing annotation @{} on {} class: {}", HasEntityTypeConstant.class.getSimpleName(), EntityId.class.getSimpleName(), clasz.getName());
                    include = false;
                }
                if (include) {
                    classes.add(clasz);
                    LOG.info("Added {} class to {}: {}", EntityId.class.getSimpleName(), JandexEntityIdFactory.class.getSimpleName(), clasz.getName());
                } else {
                    LOG.debug("Ignored {} class: {}", EntityId.class.getSimpleName(), clasz.getName());
                }
            }
        }
        return classes;
    }

    public EntityType typeConstant(Class<?> entityIdClass) {
        final HasEntityTypeConstant annotation = entityIdClass.getAnnotation(HasEntityTypeConstant.class);
        return HasEntityTypeConstantValidator.extractValue(entityIdClass, annotation.value());
    }

    private Function<String, Boolean> isValidMethod(Class<?> entityIdClass) {
        final HasPublicStaticIsValidMethod annotation = entityIdClass.getAnnotation(HasPublicStaticIsValidMethod.class);
        return HasPublicStaticIsValidMethodValidator.findFunction(entityIdClass, annotation.method(), annotation.param());
    }

    private Function<String, EntityId> valueOfMethod(Class<?> entityIdClass) {
        final HasPublicStaticValueOfMethod annotation = entityIdClass.getAnnotation(HasPublicStaticValueOfMethod.class);
        return HasPublicStaticValueOfMethodValidator.findFunction(entityIdClass, annotation.method(), annotation.param());
    }

}
