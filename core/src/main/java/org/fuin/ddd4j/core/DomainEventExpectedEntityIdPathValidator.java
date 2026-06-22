package org.fuin.ddd4j.core;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.fuin.objects4j.common.ThreadSafe;

/**
 * Validates that the path has a defined order and type.
 */
@ThreadSafe
public final class DomainEventExpectedEntityIdPathValidator
        implements ConstraintValidator<DomainEventExpectedEntityIdPath, DomainEvent<?>> {

    @SuppressWarnings("NullAway.Init")
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
