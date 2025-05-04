package org.fuin.ddd4j.core;

import org.fuin.objects4j.common.TypeConstantValidator;
import org.fuin.utils4j.TestOmitted;

/**
 * Determines if the annotated class has a public static constant with the given name and {@link EntityType} type.
 */
@TestOmitted("All function in base class")
public class HasEntityTypeConstantValidator extends TypeConstantValidator<HasEntityTypeConstant> {
}
