package org.fuin.ddd4j.ddd;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validates that the path has a defined order and type.
 */
public final class ExpectedEntityIdPathValidator implements ConstraintValidator<ExpectedEntityIdPath, EntityIdPath> {

    private List<String> annotations;

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
    protected void initialize(final String[] annotation) {
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
        final Iterator<String> expected = annotations.iterator();
        final Iterator<EntityId> actual = value.iterator();
        while (actual.hasNext()) {
            final String actualName = actual.next().getType().asString();
            final String expectedName = expected.next();
            if (!actualName.equals(expectedName)) {
                return false;
            }
        }
        return true;
    }

}
