package org.fuin.ddd4j.codegen.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Command.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommandVO {

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
     * Aggregate identifier class.
     *
     * @return Class of the aggregate ID.
     */
    String aggregateIdClass();

    /**
     * Entity identifier class. This is the last part in an entity path.
     * In case the command targets only an aggregate root (not an entity inside it),
     * the value can be empty or is set to same value as {@link  #aggregateIdClass()}.
     *
     * @return Class of the entity ID or an empty string.
     */
    String entityIdClass() default "";

    /**
     * Types of the ID classes used to construct an EntityIdPath.
     * Example: <code>{ "my.pkg.VendorId", "my.pkg.PersonId" }</code>
     * In case the type is not in the same package as the command, it must be fully qualified.
     * @return Names of the parameters types in {@link #entityIdPathParams()}.
     */
    String[] entityIdPathClasses();

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
