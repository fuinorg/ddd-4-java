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

import javax.annotation.processing.Messager;
import javax.lang.model.element.TypeElement;
import java.io.Writer;
import java.lang.annotation.Annotation;

/**
 * Generates code using the values of an annotation and a velocity template.
 *
 * @param <ANNOTATION>
 *            Annotation type.
 */
public interface ValueObjectTemplate<ANNOTATION extends Annotation> {

    /**
     * Returns the annotation class the template can handle.
     *
     * @return Annotation type.
     */
    Class<ANNOTATION> getAnnotationClass();

    /**
     * Returns the name of the target class to create.
     *
     * @param anno
     *            Annotation instance that has the name.
     *
     * @return Simple name of the class to generate.
     */
    String targetClassName(ANNOTATION anno);

    /**
     * Stores the result of merging the template and the annotation data to the given writer instance.
     *
     * @param messager
     *            Used for logging.
     * @param writer
     *            Writer used to store the generated code.
     * @param packageName
     *            Package for the generated class.
     * @param anno
     *            Annotation instance.
     * @param element
     *            Annotated element.
     */
    void generate(Messager messager,
                  Writer writer,
                  String packageName,
                  ANNOTATION anno,
                  TypeElement element);

}
