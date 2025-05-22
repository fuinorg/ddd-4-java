/**
 * Copyright (C) 2020 Michael Schnell. All rights reserved. http://www.fuin.org/
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.fuin.ddd4j.codegen.processor;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.fuin.ddd4j.codegen.api.IntegerEntityIdVO;

import javax.annotation.processing.Messager;
import javax.lang.model.element.TypeElement;
import java.io.Writer;

import static org.fuin.ddd4j.codegen.processor.Ddd4jCodeGenUtils.createVelocityEngine;

/**
 * Generates code using the values of the {@link IntegerEntityIdVO} annotation and a velocity template.
 */
public final class IntegerEntityIdVOTemplate implements ValueObjectTemplate<IntegerEntityIdVO> {

    @Override
    public final Class<IntegerEntityIdVO> getAnnotationClass() {
        return IntegerEntityIdVO.class;
    }

    @Override
    public final String targetClassName(final IntegerEntityIdVO anno) {
        return anno.name();
    }

    @Override
    public String targetPackage(IntegerEntityIdVO anno) {
        return anno.pkg();
    }

    @Override
    public void generate(final Messager messager, final Writer writer, final String packageName,
                         final IntegerEntityIdVO anno, final TypeElement element) {

        final VelocityEngine ve = createVelocityEngine();
        final VelocityContext context = new VelocityContext();
        if (anno.pkg().length() == 0) {
            context.put("package", packageName);
        } else {
            context.put("package", anno.pkg());
        }
        context.put("class", anno.name());
        context.put("entityType", anno.entityType());
        context.put("jpa", anno.jpa());
        context.put("jsonb", anno.jsonb());
        context.put("jaxb", anno.jaxb());
        context.put("openapi", anno.openapi());
        context.put("description", anno.description());
        context.put("serialVersionUID", anno.serialVersionUID());
        context.put("minValue", anno.minValue());
        context.put("maxValue", anno.maxValue());
        context.put("integerMinValue", Integer.MIN_VALUE);
        context.put("integerMaxValue", Integer.MAX_VALUE);
        final Template template = ve.getTemplate("IntegerEntityIdVO.java");
        template.merge(context, writer);

    }

}
