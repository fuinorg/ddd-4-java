package org.fuin.ddd4j.core;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.fuin.objects4j.common.ThreadSafe;
import org.fuin.utils4j.Utils4J;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Validates that the path has a defined order and type.
 */
@ThreadSafe
public final class ExpectedEntityIdPathValidator implements ConstraintValidator<ExpectedEntityIdPath, EntityIdPath> {

    private static final String KEY = ExpectedEntityIdPath.class.getName() + ".message";

    private static final ResourceBundle MESSAGES = ResourceBundle.getBundle("ValidationMessages", Locale.getDefault());

    @SuppressWarnings("NullAway.Init")
    private EntityIdPathSpec expected;

    @Override
    public void initialize(final ExpectedEntityIdPath annotation) {
        initialize(annotation.value());
    }

    /**
     * Initializer used by other validators in this package.
     *
     * @param annotation Expected annotation names.
     */
    protected void initialize(final Segment[] annotation) {
        if (annotation == null || annotation.length == 0) {
            throw new IllegalArgumentException("List of entity id types in annotation is invalid: "
                    + Arrays.toString(annotation));
        }
        expected = EntityIdPathSpec.of(annotation);
    }

    @Override
    public final boolean isValid(final EntityIdPath value, final ConstraintValidatorContext context) {
        if (expected.matches(value)) {
            return true;
        }
        error(context, message(value));
        return false;
    }

    private String message(final EntityIdPath value) {
        return Objects.requireNonNull(Utils4J.replaceVars(MESSAGES.getString(KEY),
                Map.of("expectedEntityIdTypes", expected.toString(),
                        "actualEntityIdTypes", entityIdTypesNames(value),
                        "actualEntityIdPath", value.toString())));
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
