package org.fuin.ddd4j.ddd;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.fuin.esc.api.HasSerializedDataTypeConstantValidator;
import org.fuin.esc.api.SerializedDataType;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Determines if the annotated class has a public static constant with the given name and {@link EntityType} type.
 */
public class HasEntityTypeConstantValidator implements ConstraintValidator<HasEntityTypeConstant, Object> {

    private String name;

    @Override
    public void initialize(HasEntityTypeConstant annotation) {
        this.name = annotation.value();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        try {
            final Field field = obj.getClass().getField(name);
            final int modifiers = field.getModifiers();
            if (!Modifier.isStatic(modifiers)) {
                error(context, "Field '" + name + "' is not static (#1)");
                return false;
            }
            if (field.getType() != EntityType.class) {
                error(context, "Expected constant '" + name + "' to be of type '" + EntityType.class.getName() + "', but was: " + field.getType().getName() + " (#3)");
                return false;
            }
            final Object value = field.get(obj);
            if (value == null) {
                error(context, "Constant '" + name + "' is expected to be a non-null value (#4)");
                return false;
            }
            if (!Modifier.isFinal(modifiers)) {
                error(context, "Constant '" + name + "' is not not final (#5)");
                return false;
            }
            return true;
        } catch (final NoSuchFieldException ex) {
            error(context, "The field '" + name + "' is undefined or it is not public (#2)");
            return false;
        } catch (final IllegalAccessException ex) {
            throw new IllegalStateException("Failed to execute method", ex);
        }
    }

    private void error(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }

    private static Result analyze(final Class<?> clasz, final String name) {
        try {
            final Field field = clasz.getField(name);
            final int modifiers = field.getModifiers();
            if (!Modifier.isStatic(modifiers)) {
                return new Result("Field '" + name + "' is not static (#1)", null);
            }
            if (field.getType() != EntityType.class) {
                return new Result("Expected constant '" + name + "' to be of type '" + EntityType.class.getName() + "', but was: " + field.getType().getName() + " (#3)", null);
            }
            final Object value = field.get(clasz);
            if (value == null) {
                return new Result("Constant '" + name + "' is expected to be a non-null value (#4)", null);
            }
            if (!Modifier.isFinal(modifiers)) {
                return new Result("Constant '" + name + "' is not not final (#5)", null);
            }
            return new Result(null, (EntityType) value);
        } catch (final NoSuchFieldException ex) {
            return new Result("The field '" + name + "' is undefined or it is not public (#2)", null);
        } catch (final IllegalAccessException ex) {
            throw new IllegalStateException("Failed to execute method", ex);
        }
    }

    /**
     * Returns a constant of type {@link EntityType} in a class. Throws an {@link IllegalArgumentException}
     * in case there is a problem with the field.
     *
     * @param clasz      Class to inspect.
     * @param fieldName Name of the public static field of type {@link EntityType}.
     * @return Value of the constant.
     */
    public static EntityType extractValue(final Class<?> clasz, final String fieldName) {
        final Result result = analyze(clasz, fieldName);
        if (result.message() == null) {
            return result.value();
        }
        throw new IllegalArgumentException(result.message());
    }

    private record Result(String message, EntityType value) {
    }


}
