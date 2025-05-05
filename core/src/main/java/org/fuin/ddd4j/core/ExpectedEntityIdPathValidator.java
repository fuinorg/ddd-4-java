package org.fuin.ddd4j.core;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.fuin.utils4j.Utils4J;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Validates that the path has a defined order and type.
 */
public final class ExpectedEntityIdPathValidator implements ConstraintValidator<ExpectedEntityIdPath, EntityIdPath> {

    private static final String KEY = ExpectedEntityIdPath.class.getName() + ".message";

    private static final ResourceBundle MESSAGES = ResourceBundle.getBundle("ValidationMessages", Locale.getDefault());

    private List<Class<? extends EntityId>> expectedEntityIdTypes;

    @Override
    public void initialize(final ExpectedEntityIdPath annotation) {
        initialize(annotation.value());
    }

    /**
     * Initializer used by other validators in this package.
     *
     * @param annotation Expected annotation names.
     */
    protected void initialize(final Class<? extends EntityId>[] annotation) {
        if (annotation == null || annotation.length == 0) {
            throw new IllegalArgumentException("List of entity id types in annotation is invalid: "
                    + Arrays.toString(annotation));
        }
        expectedEntityIdTypes = Arrays.asList(annotation);
    }

    @Override
    public final boolean isValid(final EntityIdPath value, final ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        if (value.size() != expectedEntityIdTypes.size()) {
            error(context, Utils4J.replaceVars(MESSAGES.getString(KEY),
                    Map.of("expectedEntityIdTypes", entityIdTypesNames(expectedEntityIdTypes),
                            "actualEntityIdTypes", entityIdTypesNames(value),
                            "actualEntityIdPath", value.toString())));
            return false;
        }
        final Iterator<Class<? extends EntityId>> expected = expectedEntityIdTypes.iterator();
        final Iterator<EntityId> actual = value.iterator();
        while (actual.hasNext()) {
            final EntityId actualId = actual.next();
            final Class<? extends EntityId> expectedIdType = expected.next();
            if (!expectedIdType.isAssignableFrom(actualId.getClass())) {
                error(context, Utils4J.replaceVars(MESSAGES.getString(KEY),
                        Map.of("expectedEntityIdTypes", entityIdTypesNames(expectedEntityIdTypes),
                               "actualEntityIdTypes", entityIdTypesNames(value),
                               "actualEntityIdPath", value.toString())));
                return false;
            }
        }
        return true;
    }

    private static String entityIdTypesNames(EntityIdPath path) {
        final List<Class<? extends EntityId>> idTypes = new ArrayList<>();
        path.iterator().forEachRemaining(t -> idTypes.add(t.getClass()));
        return entityIdTypesNames(idTypes);
    }

    private static String entityIdTypesNames( List<Class<? extends EntityId>> idTypes) {
        return idTypes.stream()
                .map(Class::getSimpleName)
                .collect(Collectors.joining(", "));
    }

    private void error(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }


}
