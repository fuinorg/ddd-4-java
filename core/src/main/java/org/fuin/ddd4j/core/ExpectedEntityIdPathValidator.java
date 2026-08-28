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
    private List<Segment> expectedSegments;

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
        for (final Segment segment : annotation) {
            if (segment.min() < 0 || segment.max() < 1 || segment.max() < segment.min()) {
                throw new IllegalArgumentException("Segment range for '" + segment.type().getSimpleName()
                        + "' is invalid: [" + segment.min() + ".." + segment.max() + "]");
            }
        }
        expectedSegments = Arrays.asList(annotation);
    }

    @Override
    public final boolean isValid(final EntityIdPath value, final ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        final List<EntityId> actual = new ArrayList<>();
        value.iterator().forEachRemaining(actual::add);
        if (!matches(0, actual, 0)) {
            error(context, message(value));
            return false;
        }
        return true;
    }

    /**
     * Whether the steps from {@code segmentIdx} on match the identifiers from {@code idIdx} on.
     * <p>
     * A step takes between its own bounds, so the search backtracks rather than being greedy: two
     * unbounded steps of the same type in a row would otherwise never match, and neither would a
     * skippable one followed by something it could have swallowed. The lists are a handful of entries
     * long, which is why this is written for clarity rather than for speed.
     *
     * @param segmentIdx Step to match next.
     * @param actual     Identifiers of the path being checked.
     * @param idIdx      Identifier to match next.
     * @return {@literal true} if the remainder matches.
     */
    private boolean matches(final int segmentIdx, final List<EntityId> actual, final int idIdx) {
        if (segmentIdx == expectedSegments.size()) {
            return idIdx == actual.size();
        }
        final Segment segment = expectedSegments.get(segmentIdx);
        int available = 0;
        while (idIdx + available < actual.size() && available < segment.max()
                && segment.type().isAssignableFrom(actual.get(idIdx + available).getClass())) {
            available++;
        }
        for (int taken = segment.min(); taken <= available; taken++) {
            if (matches(segmentIdx + 1, actual, idIdx + taken)) {
                return true;
            }
        }
        return false;
    }

    private String message(final EntityIdPath value) {
        return Objects.requireNonNull(Utils4J.replaceVars(MESSAGES.getString(KEY),
                Map.of("expectedEntityIdTypes", segmentNames(expectedSegments),
                        "actualEntityIdTypes", entityIdTypesNames(value),
                        "actualEntityIdPath", value.toString())));
    }

    private static String entityIdTypesNames(EntityIdPath path) {
        final List<Class<? extends EntityId>> idTypes = new ArrayList<>();
        path.iterator().forEachRemaining(t -> idTypes.add(t.getClass()));
        return entityIdTypesNames(idTypes);
    }

    private static String segmentNames(final List<Segment> segments) {
        return segments.stream()
                .map(ExpectedEntityIdPathValidator::segmentName)
                .collect(Collectors.joining(", "));
    }

    private static String segmentName(final Segment segment) {
        final String name = segment.type().getSimpleName();
        if (segment.min() == 1 && segment.max() == 1) {
            return name;
        }
        return name + "[" + segment.min() + ".."
                + (segment.max() == Integer.MAX_VALUE ? "N" : segment.max()) + "]";
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
