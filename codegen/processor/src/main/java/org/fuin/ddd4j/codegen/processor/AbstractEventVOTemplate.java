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

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import javax.annotation.processing.Messager;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import static org.fuin.ddd4j.codegen.processor.Ddd4jCodeGenUtils.createVelocityEngine;

/**
 * Base class for event classes.
 *
 * @param <ANNOTATION> Annotation type.
 */
public abstract class AbstractEventVOTemplate<ANNOTATION extends Annotation> implements ValueObjectTemplate<ANNOTATION> {

    private static final Pattern PATTERN = Pattern.compile("(?=[A-Z][a-z])");

    protected abstract VelocityContext createVelocityContext(final String packageName, ANNOTATION annotation);

    protected abstract String getTemplateName();

    @Override
    public final void generate(final Messager messager,
                               final Writer writer,
                               final String packageName,
                               final ANNOTATION anno,
                               final TypeElement element) {

        final VelocityEngine ve = createVelocityEngine();
        final VelocityContext context = createVelocityContext(packageName, anno);

        final List<Field> fields = element.getEnclosedElements().stream()
                .filter(f -> f.getKind() == ElementKind.FIELD)
                .map(e -> (VariableElement) e)
                .map(var -> new Field(var.getSimpleName().toString(),
                        pkgName(messager, var.asType()),
                        typeName(messager, var.asType()),
                        var.getAnnotationMirrors()))
                .toList();
        context.put("fields", fields);

        final List<String> imports = new ArrayList<>();

        // Add imports for fields
        imports.addAll(fields.stream()
                .filter(field -> !field.pkg.isEmpty())
                .map(field -> field.pkg + "." + field.type)
                .toList());

        // Add imports for annotations
        imports.addAll(fields.stream()
                .map(Field::getAnnotationImports)
                .flatMap(Collection::stream)
                .toList());

        context.put("additionalImports", imports);

        final Template template = ve.getTemplate(getTemplateName());
        template.merge(context, writer);

    }

    private static String typeName(Messager messager, TypeMirror type) {
        final String fqn = fqn(messager, type);
        final int p = fqn.lastIndexOf('.');
        if (p > 0) {
            return fqn.substring(p + 1);
        }
        return fqn;
    }

    private static String pkgName(Messager messager, TypeMirror type) {
        final String fqn = fqn(messager, type);
        final int p = fqn.lastIndexOf('.');
        if (p > 0) {
            return fqn.substring(0, p);
        }
        return "";
    }

    private static String fqn(Messager messager, TypeMirror type) {
        if (type.getKind().isPrimitive()) {
            PrimitiveType primitiveType = (PrimitiveType) type;
            return primitiveType.toString();
        }
        if (type.getKind() == TypeKind.DECLARED) {
            final DeclaredType declaredType = (DeclaredType) type;
            return declaredType.asElement().toString();
        }
        messager.printMessage(Diagnostic.Kind.WARNING, "OTHER Type: " + type.getKind() + " [" + type + "]");
        return type.toString();
    }


    public static class Clasz {

        private String name;

        public Clasz(String name) {
            this.name = name;
        }

        public String getSimpleName() {
            final int p = name.lastIndexOf('.');
            return name.substring(p + 1);
        }

        public String getPackage() {
            final int p = name.lastIndexOf('.');
            if (p > 0) {
                return name.substring(0, p);
            }
            return "";
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public record Field(String name,
                        String pkg,
                        String type,
                        List<? extends AnnotationMirror> annotations) {

        public String getDescription() {
            return annotations.stream()
                    .filter(anno -> anno.getAnnotationType().toString().endsWith("Tooltip"))
                    .map(this::extractValue)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse("TODO Add '@Tooltip' annotation");
        }

        public String getLabel() {
            return annotations.stream()
                    .filter(anno -> anno.getAnnotationType().toString().endsWith("Label")
                            && !anno.getAnnotationType().toString().endsWith("ShortLabel"))
                    .map(this::extractValue)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse("TODO Add '@Label' annotation");
        }

        private String extractValue(AnnotationMirror anno) {
            final Map<? extends ExecutableElement, ? extends AnnotationValue> values = anno.getElementValues();
            for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : values.entrySet()) {
                final String key = entry.getKey().getSimpleName().toString();
                if (key.equals("value")) {
                    return (String) entry.getValue().getValue();
                }
            }
            return null;
        }

        public List<String> getAnnotations() {
            return annotations.stream()
                    .map(anno -> removePackage(anno.toString()))
                    .toList();
        }

        public List<String> getAnnotationImports() {
            return annotations.stream()
                    .map(anno -> anno.getAnnotationType().asElement().toString())
                    .toList();
        }

        public String getProperty() {
            return PATTERN.matcher(name).replaceAll("-").toLowerCase();
        }

        public String getNameGetter() {
            return "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
        }

        private String removePackage(String annotation) {
            final String str = annotation.trim();
            if (!str.startsWith("@")) {
                return str;
            }
            int p = str.indexOf("(");
            if (p == -1) {
                // No args
                return "@" + simpleName(str.substring(1));
            }
            final String params = str.substring(p);
            final String withoutParams = str.substring(1, p);
            return "@" + simpleName(withoutParams) + params;
        }

        private String simpleName(final String fqn) {
            final int p = fqn.lastIndexOf(".");
            if (p == -1) {
                return fqn;
            }
            return fqn.substring(p + 1);
        }


    }

}
