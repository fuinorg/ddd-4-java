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

import org.apache.velocity.VelocityContext;
import org.fuin.ddd4j.codegen.api.EventVO;

/**
 * Generates code using the values of the {@link EventVO} annotation and a velocity template.
 */
public final class EventVOTemplate extends AbstractEventVOTemplate<EventVO> {

    @Override
    public final Class<EventVO> getAnnotationClass() {
        return EventVO.class;
    }

    @Override
    public final String targetClassName(final EventVO anno) {
        return anno.name();
    }

    @Override
    public String targetPackage(EventVO anno) {
        return anno.pkg();
    }

    @Override
    protected String getTemplateName() {
        return "EventVO.java";
    }

    @Override
    protected VelocityContext createVelocityContext(String packageName, EventVO anno) {
        final VelocityContext context = new VelocityContext();
        if (anno.pkg().isEmpty()) {
            context.put("package", packageName);
        } else {
            context.put("package", anno.pkg());
        }
        context.put("class", anno.name());
        context.put("idClass", new Clasz(anno.entityIdClass()));
        context.put("entityIdPathParams", anno.entityIdPathParams());
        context.put("jsonb", anno.jsonb());
        context.put("jackson", anno.jackson());
        context.put("jaxb", anno.jaxb());
        context.put("openapi", anno.openapi());
        context.put("description", anno.description());
        context.put("serialVersionUID", anno.serialVersionUID());
        context.put("message", anno.message());
        return context;
    }

}
