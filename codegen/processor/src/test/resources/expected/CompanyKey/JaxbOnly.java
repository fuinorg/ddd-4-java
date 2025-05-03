package org.fuin.ddd4jcodegen.test;

import java.io.Serializable;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Objects;
import java.util.regex.Pattern;


import jakarta.annotation.Generated;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import org.fuin.objects4j.common.ConstraintViolationException;
import org.fuin.objects4j.common.AsStringCapable;
import org.fuin.objects4j.common.ValueObjectWithBaseType;

import javax.annotation.concurrent.Immutable;

/**
 * Human readable unique key of a company.
 * 
 * CAUTION: Instances of this type may contain invalid values by deserializing it.
 * This means if you create it from JSON, XML or database (JPA) it may not have a correct length or pattern.
 */
@Generated("Generated class - Manual changes will be overwritten")
@Immutable

public final class CompanyKey implements ValueObjectWithBaseType<String>, Comparable<CompanyKey>, Serializable, AsStringCapable {

    private static final long serialVersionUID = 1000L;

    /** Regular expression of a valid value. */
    public static final Pattern PATTERN = Pattern.compile("[a-z0-9][a-z0-9-]+");


    /** Maximum length of a valid value. */
    public static final int MAX_LENGTH = 50;

    @NotNull
    @CompanyKeyStr
    private String value;

    /**
     * Protected default constructor for deserialization.
     */
    protected CompanyKey() {
        super();
    }

    /**
     * Constructor with mandatory data.
     * 
     * @param value
     *            Value.
     */
    public CompanyKey(final String value) {
        this(value, true);
    }

    private CompanyKey(final String value, final boolean strict) {
        super();
        if (strict) {
            CompanyKey.requireArgValid("value", value);
        }
        this.value = value;
    }

    @Override
    public final String asBaseType() {
        return value;
    }

    @Override
    public final String toString() {
        return value;
    }

    @Override
    public final String asString() {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CompanyKey other = (CompanyKey) obj;
        return Objects.equals(value, other.value);
    }

    @Override
    public final int compareTo(final CompanyKey other) {
        return value.compareTo(other.value);
    }

    @Override
    @NotNull
    public final Class<String> getBaseType() {
        return String.class;
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
        final String trimmed = value.trim();
        if (trimmed.length() > MAX_LENGTH) {
            return false;
        }
        return PATTERN.matcher(trimmed).matches();
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
    public static @interface CompanyKeyStr {

        String message()

        default "{org.fuin.ddd4jcodegen.test.CompanyKey.message}";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};

    }

    /**
     * Validates if a string is compliant with the type.
     */
    public static final class Validator implements ConstraintValidator<CompanyKeyStr, String> {

        @Override
        public final void initialize(final CompanyKeyStr annotation) {
            // Not used
        }

        @Override
        public final boolean isValid(final String value, final ConstraintValidatorContext context) {
            return CompanyKey.isValid(value);
        }

    }

    /**
     * Converts the value object from/to string.
     */
    public static final class Converter extends XmlAdapter<String, CompanyKey> {

        private CompanyKey toVO(final String value) {
            if (value == null) {
                return null;
            }
            return new CompanyKey(value, false);
        }

        private String fromVO(final CompanyKey value) {
            if (value == null) {
                return null;
            }
            return value.asBaseType();
        }
        // JAX-B

        @Override
        public final String marshal(final CompanyKey value) throws Exception {
            return fromVO(value);
        }

        @Override
        public final CompanyKey unmarshal(final String value) throws Exception {
            return toVO(value);
        }

    }

}
