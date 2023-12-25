package org.fuin.ddd4j.ddd;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Validates that the path has a defined order and type.
 */
public final class ExpectedEntityIdPathValidator implements ConstraintValidator<ExpectedEntityIdPath, EntityIdPath> {

    private List<Class<? extends EntityId>> annotations;

    @Override
    public void initialize(final ExpectedEntityIdPath annotation) {
        initialize(annotation.value());
    }

    /**
     * Initializer used by other validators in this package.
     * 
     * @param annotation
     *            Expected annotation names.
     */
    protected void initialize(final Class<? extends EntityId>[] annotation) {
        annotations = Arrays.asList(annotation);
    }

    @Override
    public final boolean isValid(final EntityIdPath value, final ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        if (value.size() != annotations.size()) {
            return false;
        }
        final Iterator<Class<? extends EntityId>> expected = annotations.iterator();
        final Iterator<EntityId> actual = value.iterator();
        while (actual.hasNext()) {
            final EntityId actualId = actual.next();
            final Class<? extends EntityId> expectedIdType = expected.next();
            if (!expectedIdType.isAssignableFrom(actualId.getClass())) {
                return false;
            }
        }
        return true;
    }

}
