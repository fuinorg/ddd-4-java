package org.fuin.ddd4j.codegen;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Event.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EventVO {

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
     * Entity identifier class.
     *
     * @return Class of the ID.
     */
    String fqnEntityIdClass();

    /**
     * Expression that should be used as parameters for the EntityIdPath constructor.
     * Based only on fields in the event like <code>vendorRef.getId(), personId</code>.
     * Will be used like this: <code>new EntityIdPath(vendorRef.getId(), personId)</code>.
     *
     * @return A code snippet to be used for the EntityIdPath constructor.
     */
    String entityIdPathParams();

    /**
     * Description of the type.
     *
     * @return Human readable business driven description of what this type represents.
     */
    String description();

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
     * Generate a Eclipse Microprofile OpenAPI annotations.
     *
     * @return TRUE generates annotations.
     */
    boolean openapi() default false;
    /**
     * Serial version UID to use.
     *
     * @return Version used for Java serialization.
     */
    long serialVersionUID() default 1L;

    /**
     * Message used to define the "toString()" content.
     * May contain variables like <code>${field.name}</code>.
     */
    String message();

}
