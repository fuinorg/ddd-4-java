package ${package};

import java.io.Serializable;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Objects;
#if($pattern)
import java.util.regex.Pattern;
#end

#if($jsonb)
import jakarta.json.bind.adapter.JsonbAdapter;
#end
#if($jpa)
import jakarta.persistence.AttributeConverter;
#end

import jakarta.annotation.Generated;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;
#if($jaxb)import jakarta.xml.bind.annotation.adapters.XmlAdapter;#end

#if($openapi)
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
#end
import org.fuin.objects4j.common.ConstraintViolationException;
import org.fuin.objects4j.common.AsStringCapable;
import org.fuin.objects4j.common.ValueObjectWithBaseType;

import javax.annotation.concurrent.Immutable;

/**
 * <p class="business">
 * ${description}.
 * </p>
 * <p class="technical">
 * CAUTION: Instances of this type may contain invalid values by deserializing it.
 * This means if you create it from JSON, XML or database (JPA) it may not have a correct length or pattern.
 * </p>
 */
@Generated("Generated class - Manual changes will be overwritten")
@Immutable
#if($openapi)@Schema(name = "${class}", type = SchemaType.STRING, description = "${description}"#if($minLength>0), minLength = ${minLength}#end#if($maxLength<$integerMaxValue), maxLength = ${maxLength}#end#if($pattern), pattern = "${pattern}"#end#if($example), example = "${example}"#end)#end

public final class ${class} implements ValueObjectWithBaseType<String>, Comparable<${class}>, Serializable, AsStringCapable {

    private static final long serialVersionUID = ${serialVersionUID}L;

    #if($pattern)
    /** Regular expression of a valid value. */
    public static final Pattern PATTERN = Pattern.compile("${pattern}");
    #end

    #if($minLength > 0)
    /** Minimal length of a valid value. */
    public static final int MIN_LENGTH = ${minLength};
    #end

    #if($maxLength<$integerMaxValue)
    /** Maximum length of a valid value. */
    public static final int MAX_LENGTH = ${maxLength};
    #end

    @NotNull
    @${class}Str
    private String value;

    /**
     * Protected default constructor for deserialization.
     */
    protected ${class}() {
        super();
    }

    /**
     * Constructor with mandatory data.
     * 
     * @param value
     *            Value.
     */
    public ${class}(final String value) {
        this(value, true);
    }

    private ${class}(final String value, final boolean strict) {
        super();
        if (strict) {
            ${class}.requireArgValid("value", value);
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
        final ${class} other = (${class}) obj;
        return Objects.equals(value, other.value);
    }

    @Override
    public final int compareTo(final ${class} other) {
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
        #if($minLength > 0)
        if (value.length() < MIN_LENGTH) {
            return false;
        }
        #end
        final String trimmed = value.trim();
        #if($maxLength < $integerMaxValue)
        if (trimmed.length() > MAX_LENGTH) {
            return false;
        }
        #end
        #if($pattern)
        return PATTERN.matcher(trimmed).matches();
        #else
        return true;
        #end
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
    public static @interface ${class}Str {

        String message()

        default "{${package}.${class}.message}";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};

    }

    /**
     * Validates if a string is compliant with the type.
     */
    public static final class Validator implements ConstraintValidator<${class}Str, String> {

        @Override
        public final void initialize(final ${class}Str annotation) {
            // Not used
        }

        @Override
        public final boolean isValid(final String value, final ConstraintValidatorContext context) {
            return ${class}.isValid(value);
        }

    }

    #if($jaxb || $jsonb || $jpa)
    /**
     * Converts the value object from/to string.
     */
    public static final class Converter#if($jaxb) extends XmlAdapter<String, ${class}>#end#if($jsonb || $jpa) implements#end#if($jsonb) JsonbAdapter<${class}, String>#end#if($jpa)#if($jsonb),#end AttributeConverter<${class}, String>#end {

        private ${class} toVO(final String value) {
            if (value == null) {
                return null;
            }
            return new ${class}(value, false);
        }

        private String fromVO(final ${class} value) {
            if (value == null) {
                return null;
            }
            return value.asBaseType();
        }
        #if($jsonb)
        // JSONB Adapter

        @Override
        public final String adaptToJson(final ${class} obj) throws Exception {
            return fromVO(obj);
        }

        @Override
        public final ${class} adaptFromJson(final String str) throws Exception {
            return toVO(str);
        }

        #end
        #if($jaxb)
        // JAX-B

        @Override
        public final String marshal(final ${class} value) throws Exception {
            return fromVO(value);
        }

        @Override
        public final ${class} unmarshal(final String value) throws Exception {
            return toVO(value);
        }

        #end
        #if($jpa)
        // JPA

        @Override
        public final String convertToDatabaseColumn(final ${class} value) {
            return fromVO(value);
        }

        @Override
        public final ${class} convertToEntityAttribute(final String value) {
            return toVO(value);
        }
        #end
    }
    #end

}
