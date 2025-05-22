/**
 * Copyright (C) 2020 Michael Schnell. All rights reserved. http://www.fuin.org/
 * <p>
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.fuin.ddd4j.codegen.processor;

import com.google.auto.service.AutoService;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.fuin.ddd4j.codegen.processor.Ddd4jCodeGenUtils.runWithContextClassLoader;

/**
 * Class that does the actual processing of the annotation and generates code from the ones found.
 */
@SupportedAnnotationTypes({
        ValueObjectProcessor.STRING_VO,
        ValueObjectProcessor.AGGREGATE_ROOT_UUID_VO,
        ValueObjectProcessor.INTEGER_ENTITY_ID_VO,
        ValueObjectProcessor.EVENT_VO,
        ValueObjectProcessor.COMMAND_VO
})
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@AutoService(Processor.class)
public final class ValueObjectProcessor extends AbstractProcessor {

    static final String STRING_VO = "org.fuin.ddd4j.codegen.api.StringVO";

    static final String AGGREGATE_ROOT_UUID_VO = "org.fuin.ddd4j.codegen.api.AggregateRootUuidVO";

    static final String INTEGER_ENTITY_ID_VO = "org.fuin.ddd4j.codegen.api.IntegerEntityIdVO";

    static final String EVENT_VO = "org.fuin.ddd4j.codegen.api.EventVO";

    static final String COMMAND_VO = "org.fuin.ddd4j.codegen.api.CommandVO";

    @SuppressWarnings("rawtypes")
    private Map<String, ValueObjectTemplate> templateMap;

    private List<TargetType> targetTypes;

    /**
     * Default constructor.
     */
    public ValueObjectProcessor() {
        super();
        templateMap = new HashMap<>();
        templateMap.put(STRING_VO, new StringVOTemplate());
        templateMap.put(AGGREGATE_ROOT_UUID_VO, new AggregateRootUuidVOTemplate());
        templateMap.put(INTEGER_ENTITY_ID_VO, new IntegerEntityIdVOTemplate());
        templateMap.put(EVENT_VO, new EventVOTemplate());
        templateMap.put(COMMAND_VO, new CommandVOTemplate());
    }

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {

        this.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,
                "Process (processingOver=" + roundEnv.processingOver()
                + ", errorRaised=" + roundEnv.errorRaised() + "): " + typeElementNames(annotations));

        if (targetTypes == null) {
            targetTypes = new ArrayList<>(createTargetTypes(annotations, roundEnv));
        }
        targetTypes.removeIf(targetType -> generateCode(roundEnv, targetType));
        return true;

    }

    private static List<String> typeElementNames(Collection<? extends TypeElement> typeElements) {
        if (typeElements == null) {
            return Collections.emptyList();
        }
        return typeElements.stream().map(anno -> anno.getSimpleName().toString()).toList();
    }

    private List<TargetType> createTargetTypes(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return annotations.stream()
            .filter(this::hasTemplate)
            .map(anno -> new AnnotationTemplate(anno, template(anno)))
            .map(annoTpl -> createTargetTypes(roundEnv, annoTpl))
            .flatMap(Collection::stream)
            .toList();
    }

    private List<TargetType> createTargetTypes(final RoundEnvironment roundEnv, final AnnotationTemplate annoTpl) {
        final TypeElement annotation = annoTpl.getAnnotation();
        final ValueObjectTemplate<Annotation> template = annoTpl.getTemplate();
        final Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
        return annotatedElements
                .stream()
                .filter(annotatedElement -> topLevelType(annotatedElement, annotation))
                .filter(annotatedElement -> intf(annotatedElement, annotation))
                .map(annotatedElement -> new TargetType(template, (TypeElement) annotatedElement))
                .collect(Collectors.toList());
    }

    private boolean hasTemplate(final TypeElement annotation) {
        return template(annotation) != null;
    }

    @SuppressWarnings("unchecked")
    private ValueObjectTemplate<Annotation> template(final TypeElement annotation) {
        final String annotationFQN = annotation.getQualifiedName().toString();
        return templateMap.get(annotationFQN);
    }

    private boolean topLevelType(final Element annotatedElement, final TypeElement annotation) {
        final boolean b = (annotatedElement instanceof TypeElement) || (annotatedElement.getKind() != ElementKind.INTERFACE);
        if (!b) {
            final String message = "Found @" + annotation.getSimpleName() + " on element " + annotatedElement.getSimpleName()
                    + " which is not an interface";
            processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, message, annotatedElement);
        }
        return b;
    }

    private boolean intf(final Element annotatedElement, final TypeElement annotation) {
        final boolean b = (annotatedElement.getEnclosingElement() instanceof PackageElement);
        if (!b) {
            final String message = "Found @" + annotation.getSimpleName() + " on element " + annotatedElement.getSimpleName()
                    + " which is not a top level type";
            processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, message, annotatedElement);
        }
        return b;
    }

    private boolean generateCode(final RoundEnvironment roundEnv, final TargetType targetType) {
        try {
            final ValueObjectTemplate<Annotation> template = targetType.getTemplate();
            final TypeElement element = targetType.getAnnotatedElement();
            final String packageName = targetType.getPackageName();
            final Annotation annotation = targetType.getAnnotationInstance();

            return runWithContextClassLoader(() -> {
                final String packageAndClass = packageName + "." + template.targetClassName(annotation);
                this.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,"Generating code for: " + targetType);
                try {
                    final JavaFileObject fileObject = processingEnv.getFiler().createSourceFile(packageAndClass, element);
                    try (final Writer fileWriter = fileObject.openWriter()) {
                        template.generate(processingEnv.getMessager(), fileWriter, packageName, annotation, element);
                    }
                    return true;
                } catch (final Exception ex) {
                    ex.printStackTrace();
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Failed to write source for: " + packageAndClass, element);
                    return false;
                }
            }, getClass().getClassLoader());
        } catch (final RuntimeException ex) {
            return false;
        }
    }
    
    private static class AnnotationTemplate {

        private final TypeElement annotation;

        private final ValueObjectTemplate<Annotation> template;

        public AnnotationTemplate(TypeElement annotation,
                                  ValueObjectTemplate<Annotation> template) {
            super();
            this.annotation = annotation;
            this.template = template;
        }

        public TypeElement getAnnotation() {
            return annotation;
        }

        public ValueObjectTemplate<Annotation> getTemplate() {
            return template;
        }

    }

    private static class TargetType {

        private final ValueObjectTemplate<Annotation> template;

        private final TypeElement annotatedElement;

        public TargetType(ValueObjectTemplate<Annotation> template, TypeElement annotatedElement) {
            super();
            this.template = template;
            this.annotatedElement = annotatedElement;
        }

        public ValueObjectTemplate<Annotation> getTemplate() {
            return template;
        }

        public TypeElement getAnnotatedElement() {
            return annotatedElement;
        }

        public String getPackageName() {
            return template.targetPackage(getAnnotationInstance());
        }

        public Annotation getAnnotationInstance() {
            return annotatedElement.getAnnotation(template.getAnnotationClass());
        }

        @Override
        public String toString() {
            return annotatedElement.getSimpleName()
                    + " (" + template.getAnnotationClass().getName() + ") => "
                    + template.targetClassName(getAnnotationInstance());
        }

    }

}
