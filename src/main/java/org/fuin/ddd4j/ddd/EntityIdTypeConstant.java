package org.fuin.ddd4j.ddd;

import java.lang.annotation.*;

/**
 * A class that has a public static constant of type {@link EntityType}.
 * The expected default name of the constant is <b>TYPE</b>.
 */
@Documented
@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface EntityIdTypeConstant {

    /**
     * Returns the name of a public static constant of type {@link EntityType} in the annotated class.
     *
     * @return Name of the public static constant.
     */
    String value() default "TYPE";

}
