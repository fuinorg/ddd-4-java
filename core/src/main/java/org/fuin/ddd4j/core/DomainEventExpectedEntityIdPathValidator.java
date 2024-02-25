package org.fuin.ddd4j.core;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validates that the path has a defined order and type.
 */
public final class DomainEventExpectedEntityIdPathValidator
        implements ConstraintValidator<DomainEventExpectedEntityIdPath, DomainEvent<?>> {

    private ExpectedEntityIdPathValidator delegate;

    @Override
    public void initialize(final DomainEventExpectedEntityIdPath annotation) {
        delegate = new ExpectedEntityIdPathValidator();
        delegate.initialize(annotation.value());
    }

    @Override
    public final boolean isValid(final DomainEvent<?> value, final ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return delegate.isValid(value.getEntityIdPath(), context);
    }

}
