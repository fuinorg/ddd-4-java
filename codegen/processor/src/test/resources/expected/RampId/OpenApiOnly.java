package org.fuin.ddd4j.codegen.test;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.text.NumberFormat;
import java.text.ParsePosition;

import jakarta.annotation.Generated;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;

import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.fuin.ddd4j.core.IntegerEntityId;
import org.fuin.ddd4j.core.EntityType;
import org.fuin.ddd4j.core.HasEntityTypeConstant;
import org.fuin.ddd4j.core.StringBasedEntityType;
import org.fuin.objects4j.common.ConstraintViolationException;
import org.fuin.objects4j.common.HasPublicStaticIsValidMethod;
import org.fuin.objects4j.common.HasPublicStaticValueOfMethod;

import javax.annotation.concurrent.Immutable;

/**
 * Unique identifier of a ramp.
 * 
 * CAUTION: Instances of this type may contain invalid values by deserializing it.
 * This means if you create it from JSON, XML or database (JPA) it may not have a correct min or max value.
 */
@Generated("Generated class - Manual changes will be overwritten")
@Immutable
@HasPublicStaticValueOfMethod
@HasEntityTypeConstant
@HasPublicStaticIsValidMethod
@HasPublicStaticIsValidMethod(param = Integer.class)
@Schema(name = "RampId", type = SchemaType.INTEGER, description = "Unique identifier of a ramp", format="int32", minimum = "1", maximum = "100")
public final class RampId extends IntegerEntityId {

    private static final long serialVersionUID = 1000L;

    /** Unique name of the aggregate this identifier refers to. */
    public static final EntityType TYPE = new StringBasedEntityType("RAMP");
    
    private static final int MIN = 1;

    private static final int MAX = 100;
    
    /**
     * Constructor with mandatory data.
     * 
     * @param value
     *            Value.
     */
    public RampId(final Integer value) {
        this(value, true);
    }

    private RampId(final Integer value, final boolean strict) {
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
    public static RampId valueOf(final String value) {
        if (value == null) {
            return null;
        }
        requireArgValid("value", value);
        return new RampId(Integer.valueOf(value));
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
        if (num instanceof Integer v) {
            return isValid(v);
        }
        if (num instanceof Long v && v <= Integer.MAX_VALUE) {
            return isValid(v.intValue());
        }
        return false;
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
    public static @interface RampIdStr {

        String message()

        default "{org.fuin.ddd4j.codegen.test.RampId.message}";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};

    }

    /**
     * Validates if a string is compliant with the type.
     */
    public static final class Validator implements ConstraintValidator<RampIdStr, String> {

        @Override
        public final void initialize(final RampIdStr annotation) {
            // Not used
        }

        @Override
        public final boolean isValid(final String value, final ConstraintValidatorContext context) {
            return RampId.isValid(value);
        }

    }


}
