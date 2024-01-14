package org.fuin.ddd4j.ddd;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * A class that has a public static constant of type {@link EntityType}.
 * The expected default name of the constant is <b>TYPE</b>.
 */
@Documented
@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { HasEntityTypeConstantValidator.class })
public @interface HasEntityTypeConstant {

    /**
     * Returns the name of a public static constant of type {@link EntityType} in the annotated class.
     *
     * @return Name of the public static constant.
     */
    String value() default "TYPE";

    String message() default "Does not define a public static constant with the given name";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
