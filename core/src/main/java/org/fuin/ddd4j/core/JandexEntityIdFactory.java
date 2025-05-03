package org.fuin.ddd4j.core;

import org.fuin.objects4j.common.HasPublicStaticIsValidMethod;
import org.fuin.objects4j.common.HasPublicStaticIsValidMethodValidator;
import org.fuin.objects4j.common.HasPublicStaticIsValidMethods;
import org.fuin.objects4j.common.HasPublicStaticValueOfMethod;
import org.fuin.objects4j.common.HasPublicStaticValueOfMethodValidator;
import org.fuin.objects4j.common.HasPublicStaticValueOfMethods;
import org.fuin.utils4j.jandex.JandexIndexFileReader.Builder;
import org.fuin.utils4j.jandex.JandexUtils;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.CompositeIndex;
import org.jboss.jandex.DotName;
import org.jboss.jandex.IndexView;
import org.jboss.jandex.Indexer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    public JandexEntityIdFactory(final File... classesDirs) {
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

    @SuppressWarnings("java:S3776") // Complexity is OK here as refactoring also would not provide much benefit
    private static List<Class<?>> findEntityIdClasses(final IndexView index) {
        List<Class<?>> classes = new ArrayList<>();
        final Set<ClassInfo> classInfos = new HashSet<>();
        classInfos.addAll(index.getAllKnownImplementors(DotName.createSimple(EntityId.class)));
        classInfos.addAll(index.getAllKnownImplementors(DotName.createSimple(AggregateRootId.class)));
        for (final ClassInfo classInfo : classInfos) {
            if (!Modifier.isAbstract(classInfo.flags()) && !Modifier.isInterface(classInfo.flags())) {
                final Class<?> clasz = JandexUtils.loadClass(classInfo.name());
                final boolean include = hasAnnotation(clasz, HasPublicStaticIsValidMethod.class, HasPublicStaticIsValidMethods.class)
                        && hasAnnotation(clasz, HasPublicStaticValueOfMethod.class, HasPublicStaticValueOfMethods.class)
                        && hasAnnotation(clasz, HasEntityTypeConstant.class);
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

    private static boolean hasAnnotation(Class<?> clasz,
                                         Class<? extends Annotation> single,
                                         Class<? extends Annotation> multi) {
        if (clasz.getAnnotation(single) != null) {
            return true;
        }
        if (clasz.getAnnotation(multi) != null) {
            return true;
        }
        LOG.warn("Missing annotation @{} or @{} on {} class: {}",
                single.getSimpleName(),
                multi.getSimpleName(),
                EntityId.class.getSimpleName(),
                clasz.getName());
        return false;
    }

    private static boolean hasAnnotation(Class<?> clasz, Class<? extends Annotation> single) {
        if (clasz.getAnnotation(single) == null) {
            LOG.warn("Missing annotation @{} on {} class: {}",
                    single.getSimpleName(),
                    EntityId.class.getSimpleName(),
                    clasz.getName());
            return false;
        }
        return true;
    }

    private Function<String, Boolean> isValidMethod(Class<?> entityIdClass) {
        HasPublicStaticIsValidMethod annotation = entityIdClass.getAnnotation(HasPublicStaticIsValidMethod.class);
        if (annotation == null) {
            annotation = find(entityIdClass.getAnnotation(HasPublicStaticIsValidMethods.class), String.class);
        }
        return HasPublicStaticIsValidMethodValidator.findFunction(entityIdClass, annotation.method(), annotation.param());
    }

    private Function<String, EntityId> valueOfMethod(Class<?> entityIdClass) {
        HasPublicStaticValueOfMethod annotation = entityIdClass.getAnnotation(HasPublicStaticValueOfMethod.class);
        if (annotation == null) {
            annotation = find(entityIdClass.getAnnotation(HasPublicStaticValueOfMethods.class), String.class);
        }
        return HasPublicStaticValueOfMethodValidator.findFunction(entityIdClass, annotation.method(), annotation.param());
    }

    private static HasPublicStaticIsValidMethod find(final HasPublicStaticIsValidMethods annotations, Class<?> parameterType) {
        if (annotations == null) {
            throw new IllegalStateException("Annotation " + HasPublicStaticIsValidMethod.class.getSimpleName()
                    + " or " + HasPublicStaticIsValidMethods.class.getSimpleName() + " is required,"
                    + " but none of them was found");
        }
        for (final HasPublicStaticIsValidMethod annotation : annotations.value()) {
            if (annotation.param().equals(parameterType)) {
                return annotation;
            }
        }
        throw new IllegalStateException("Found annotation " + HasPublicStaticIsValidMethods.class.getSimpleName()
                + ", but none of the child " + HasPublicStaticIsValidMethod.class.getSimpleName()
                + " annotations had parameter type " + parameterType.getSimpleName());
    }

    private static HasPublicStaticValueOfMethod find(final HasPublicStaticValueOfMethods annotations, Class<?> parameterType) {
        if (annotations == null) {
            throw new IllegalStateException("Annotation " + HasPublicStaticValueOfMethod.class.getSimpleName()
                    + " or " + HasPublicStaticValueOfMethods.class.getSimpleName() + " is required,"
                    + " but none of them was found");
        }
        for (final HasPublicStaticValueOfMethod annotation : annotations.value()) {
            if (annotation.param().equals(parameterType)) {
                return annotation;
            }
        }
        throw new IllegalStateException("Found annotation " + HasPublicStaticValueOfMethods.class.getSimpleName()
                + ", but none of the child " + HasPublicStaticValueOfMethod.class.getSimpleName()
                + " annotations had parameter type " + parameterType.getSimpleName());
    }

}
