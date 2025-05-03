package org.fuin.ddd4j.codegen.test;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.UUID;


import jakarta.annotation.Generated;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import org.fuin.ddd4j.core.AggregateRootUuid;
import org.fuin.ddd4j.core.EntityType;
import org.fuin.ddd4j.core.HasEntityTypeConstant;
import org.fuin.ddd4j.core.StringBasedEntityType;
import org.fuin.objects4j.common.ConstraintViolationException;
import org.fuin.objects4j.common.HasPublicStaticIsValidMethod;
import org.fuin.objects4j.common.HasPublicStaticValueOfMethod;

import javax.annotation.concurrent.Immutable;

/**
 * Unique identifier of a company.
 */
@Generated("Generated class - Manual changes will be overwritten")
@Immutable
@HasEntityTypeConstant
@HasPublicStaticIsValidMethod
@HasPublicStaticValueOfMethod

public final class CompanyId extends AggregateRootUuid {

    private static final long serialVersionUID = 1000L;

    /** Unique name of the aggregate this identifier refers to. */
    public static final EntityType TYPE = new StringBasedEntityType("COMPANY");
    
    /**
     * Default constructor that generates a random UUID.
     */
    public CompanyId() {
        super(TYPE);
    }

    /**
     * Constructor with mandatory data.
     * 
     * @param value
     *            Value.
     */
    public CompanyId(final UUID value) {
        super(TYPE, value);
    }

    /**
     * Parses a given string and returns a new instance of this type.
     * 
     * @param value
     *            String with valid UUID to convert. A <code>null</code> value returns <code>null</code>.
     * 
     * @return Converted value.
     */
    public static CompanyId valueOf(final String value) {
        if (value == null) {
            return null;
        }
        requireArgValid("value", value);
        return new CompanyId(UUID.fromString(value));
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
        return AggregateRootUuid.isValid(value);
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
    public static @interface CompanyIdStr {

        String message()

        default "{org.fuin.ddd4j.codegen.test.CompanyId.message}";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};

    }

    /**
     * Validates if a string is compliant with the type.
     */
    public static final class Validator implements ConstraintValidator<CompanyIdStr, String> {

        @Override
        public final void initialize(final CompanyIdStr annotation) {
            // Not used
        }

        @Override
        public final boolean isValid(final String value, final ConstraintValidatorContext context) {
            return CompanyId.isValid(value);
        }

    }

    /**
     * Converts the value object from/to string.
     */
    public static final class Converter extends XmlAdapter<UUID, CompanyId> {

        private CompanyId toVO(final UUID value) {
            if (value == null) {
                return null;
            }
            return new CompanyId(value);
        }

        private UUID fromVO(final CompanyId value) {
            if (value == null) {
                return null;
            }
            return value.asBaseType();
        }
        // JAX-B

        @Override
        public final UUID marshal(final CompanyId value) throws Exception {
            return fromVO(value);
        }

        @Override
        public final CompanyId unmarshal(final UUID value) throws Exception {
            return toVO(value);
        }

    }

}
