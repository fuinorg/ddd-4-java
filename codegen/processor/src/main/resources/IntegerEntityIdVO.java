package ${package};

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.text.NumberFormat;
import java.text.ParsePosition;

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
import org.fuin.ddd4j.core.IntegerEntityId;
import org.fuin.ddd4j.core.EntityType;
import org.fuin.ddd4j.core.HasEntityTypeConstant;
import org.fuin.ddd4j.core.StringBasedEntityType;
import org.fuin.objects4j.common.ConstraintViolationException;
import org.fuin.objects4j.common.HasPublicStaticValueOfMethod;

import javax.annotation.concurrent.Immutable;

/**
 * ${description}.
 * 
 * CAUTION: Instances of this type may contain invalid values by deserializing it.
 * This means if you create it from JSON, XML or database (JPA) it may not have a correct min or max value.
 */
@Generated("Generated class - Manual changes will be overwritten")
@Immutable
@HasPublicStaticValueOfMethod
@HasEntityTypeConstant
#if($openapi)@Schema(name = "${class}", type = SchemaType.INTEGER, description = "${description}", format="int32", minimum = "${minValue}", maximum = "${maxValue}")#end

public final class ${class} extends IntegerEntityId {

    private static final long serialVersionUID = ${serialVersionUID}L;

    /** Unique name of the aggregate this identifier refers to. */
    public static final EntityType TYPE = new StringBasedEntityType("${entityType}");
    
    private static final int MIN = ${minValue};

    private static final int MAX = ${maxValue};
    
    /**
     * Constructor with mandatory data.
     * 
     * @param value
     *            Value.
     */
    public ${class}(final Integer value) {
        this(value, true);
    }

    private ${class}(final Integer value, final boolean strict) {
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
    public static ${class} valueOf(final String value) {
        if (value == null) {
            return null;
        }
        requireArgValid("value", value);
        return new ${class}(Integer.valueOf(value));
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
    public static final class Converter#if($jaxb) extends XmlAdapter<Integer, ${class}>#end#if($jsonb || $jpa) implements#end#if($jsonb) JsonbAdapter<${class}, Integer>#end#if($jpa)#if($jsonb),#end AttributeConverter<${class}, Integer>#end {

        private ${class} toVO(final Integer value) {
            if (value == null) {
                return null;
            }
            return new ${class}(value, false);
        }

        private Integer fromVO(final ${class} value) {
            if (value == null) {
                return null;
            }
            return value.asBaseType();
        }
        #if($jsonb)
        // JSONB Adapter

        @Override
        public final Integer adaptToJson(final ${class} obj) throws Exception {
            return fromVO(obj);
        }

        @Override
        public final ${class} adaptFromJson(final Integer str) throws Exception {
            return toVO(str);
        }

        #end
        #if($jaxb)
        // JAX-B

        @Override
        public final Integer marshal(final ${class} value) throws Exception {
            return fromVO(value);
        }

        @Override
        public final ${class} unmarshal(final Integer value) throws Exception {
            return toVO(value);
        }

        #end
        #if($jpa)
        // JPA

        @Override
        public final Integer convertToDatabaseColumn(final ${class} value) {
            return fromVO(value);
        }

        @Override
        public final ${class} convertToEntityAttribute(final Integer value) {
            return toVO(value);
        }
        #end
    }
    #end

}
