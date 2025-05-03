package org.fuin.ddd4j.codegen.example;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.text.NumberFormat;
import java.text.ParsePosition;

import jakarta.json.bind.adapter.JsonbAdapter;
import jakarta.annotation.Generated;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;

import org.fuin.ddd4j.core.IntegerEntityId;
import org.fuin.ddd4j.core.EntityType;
import org.fuin.ddd4j.core.HasEntityTypeConstant;
import org.fuin.ddd4j.core.StringBasedEntityType;
import org.fuin.objects4j.common.ConstraintViolationException;
import org.fuin.objects4j.common.HasPublicStaticValueOfMethod;

import javax.annotation.concurrent.Immutable;

/**
 * Unique identifier of the entity.
 * 
 * CAUTION: Instances of this type may contain invalid values by deserializing it.
 * This means if you create it from JSON, XML or database (JPA) it may not have a correct min or max value.
 */
@Generated("Generated class - Manual changes will be overwritten")
@Immutable
@HasPublicStaticValueOfMethod
@HasEntityTypeConstant

public final class TheEntityId extends IntegerEntityId {

    private static final long serialVersionUID = 1000L;

    /** Unique name of the aggregate this identifier refers to. */
    public static final EntityType TYPE = new StringBasedEntityType("THE_ENTITY");
    
    private static final int MIN = 1;

    private static final int MAX = 2147483647;
    
    /**
     * Constructor with mandatory data.
     * 
     * @param value
     *            Value.
     */
    public TheEntityId(final Integer value) {
        this(value, true);
    }

    private TheEntityId(final Integer value, final boolean strict) {
        super(TYPE, value);
        if (strict & !isValid(value)) {
            throw new ConstraintViolationException("The argument 'value' is not valid: '" + value + "'");            
        }
    }

    /**
     * Parses a given string and returns a new instance of this type.
     * 
     * @param value
     *            String with valid Integer to convert. A <code>null</code> value returns <code>null</code>.
     * 
     * @return Converted value.
     */
    public static TheEntityId valueOf(final String value) {
        if (value == null) {
            return null;
        }
        requireArgValid("value", value);
        return new TheEntityId(Integer.valueOf(value));
    }
    
    /**
     * Verifies that a given integer can be converted into the type.
     * 
     * @param value
     *            Value to validate.
     * 
     * @return Returns <code>true</code> if it's a valid type else <code>false</code>.
     */
    public static boolean isValid(final Integer value) {
        if (value == null) {
            return true;
        }
        if (value < MIN) {
            return false;
        }
        return (value <= MAX);
    }

    /**
     * Verifies that a given string can be converted into the type.
     * 
     * @param value
     *            Value to validate.
     * 
     * @return Returns <code>true</code> if it's a valid type else <code>false</code>.
     */
    public static boolean isValid(final String value) {
        if (value == null) {
            return true;
        }
        final ParsePosition pp = new ParsePosition(0);
        final NumberFormat nf = NumberFormat.getInstance();
        nf.setParseIntegerOnly(true);
        final Number num = nf.parse(value, pp);
        if (pp.getErrorIndex() != -1 || pp.getIndex() < value.length()) {
            return false;
        }
        if (!(num instanceof Integer)) {
            return false;
        }
        return isValid((Integer) num);
    }

    /**
     * Verifies if the argument is valid and throws an exception if this is not the case.
     * 
     * @param name
     *            Name of the value for a possible error message.
     * @param value
     *            Value to check.
     * 
     * @throws ConstraintViolationException
     *             The value was not valid.
     */
    public static void requireArgValid(@NotNull final String name, @NotNull final String value) throws ConstraintViolationException {
        if (!isValid(value)) {
            throw new ConstraintViolationException("The argument '" + name + "' is not valid: '" + value + "'");
        }
    }

    /**
     * Ensures that the string can be converted into the type.
     */
    @Target({ ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = { Validator.class })
    @Documented
    public static @interface TheEntityIdStr {

        String message()

        default "{org.fuin.ddd4j.codegen.example.TheEntityId.message}";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};

    }

    /**
     * Validates if a string is compliant with the type.
     */
    public static final class Validator implements ConstraintValidator<TheEntityIdStr, String> {

        @Override
        public final void initialize(final TheEntityIdStr annotation) {
            // Not used
        }

        @Override
        public final boolean isValid(final String value, final ConstraintValidatorContext context) {
            return TheEntityId.isValid(value);
        }

    }

    /**
     * Converts the value object from/to string.
     */
    public static final class Converter implements JsonbAdapter<TheEntityId, Integer> {

        private TheEntityId toVO(final Integer value) {
            if (value == null) {
                return null;
            }
            return new TheEntityId(value, false);
        }

        private Integer fromVO(final TheEntityId value) {
            if (value == null) {
                return null;
            }
            return value.asBaseType();
        }
        // JSONB Adapter

        @Override
        public final Integer adaptToJson(final TheEntityId obj) throws Exception {
            return fromVO(obj);
        }

        @Override
        public final TheEntityId adaptFromJson(final Integer str) throws Exception {
            return toVO(str);
        }

    }

}
