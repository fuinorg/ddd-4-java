/**
 * Copyright (C) 2015 Michael Schnell. All rights reserved.
 * http://www.fuin.org/
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see http://www.gnu.org/licenses/.
 */
package org.fuin.ddd4j.codegen.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * UUID based aggregate root identifier value object.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AggregateRootUuidVO {

    /**
     * Name of the package for the type to generate. If this value is not set, the package of the annotated type will be used.
     *
     * @return Package name.
     */
    String pkg() default "";

    /**
     * Unique name of the type to generate.
     *
     * @return Simple name of the target class.
     */
    String name();

    /**
     * Description of the type.
     *
     * @return Human-readable business driven description of what this type represents.
     */
    String description();

    /**
     * Unique identifier of the aggregate root type.
     *
     * @return Name that is unique in the context and can be used to create a "EntityType".
     */
    String entityType();

    /**
     * Generate a JPA converter.
     *
     * @return TRUE generates a converter.
     */
    boolean jpa() default false;

    /**
     * Generate a JAXB adapter.
     *
     * @return TRUE generates an adapter.
     */
    boolean jaxb() default false;

    /**
     * Generate a JSONB adapter.
     *
     * @return TRUE generates an adapter.
     */
    boolean jsonb() default false;

    /**
     * Generate Jackson information.
     *
     * @return TRUE generates stuff for Jackson.
     */
    boolean jackson() default false;

    /**
     * Generate a Eclipse Microprofile OpenAPI annotations.
     *
     * @return TRUE generates annotations.
     */
    boolean openapi() default false;

    /**
     * Example value of the type.
     *
     * @return Example value.
     */
    String example() default "00000000-0000-0000-0000-000000000000";

    /**
     * Serial version UID to use.
     *
     * @return Version used for Java serialization.
     */
    long serialVersionUID() default 1L;

}
