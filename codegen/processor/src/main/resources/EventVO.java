package ${package};

#if($jaxb)
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;
#end
import jakarta.validation.constraints.NotNull;
#if($openapi)
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
#end
#if($jsonb)
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbTypeAdapter;
#end
#if($jackson)
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
#end
import javax.annotation.Nullable;
import org.fuin.ddd4j.core.EntityIdPath;
import org.fuin.ddd4j.core.EventType;
#if($jsonb)
import org.fuin.ddd4j.jsonb.AbstractDomainEvent;
#end
#if($jackson)
import org.fuin.ddd4j.jackson.AbstractDomainEvent;
#end
#if($jaxb)
import org.fuin.ddd4j.jaxb.AbstractDomainEvent;
#end
import org.fuin.esc.api.HasSerializedDataTypeConstant;
import org.fuin.esc.api.SerializedDataType;
import org.fuin.objects4j.common.Contract;
#if($!idClass.pkg)
import ${idClass};
#end

import java.io.Serial;

import javax.annotation.concurrent.Immutable;

#foreach($import in $additionalImports)
import ${import};
#end

/**
 * ${description}.
 */
@Immutable
#if($openapi)
@Schema(name = "${class}", type = SchemaType.OBJECT, description = "${description}")
#end
#if($jaxb)
@XmlRootElement(name = "${class}")
#end
@HasSerializedDataTypeConstant
public final class ${class} extends AbstractDomainEvent<${idClass.simpleName}> {

    @Serial
    private static final long serialVersionUID = ${serialVersionUID}L;

    /** Unique name of the event used to store it - Should never change. */
    public static final EventType TYPE = new EventType(${class}.class.getSimpleName());

    /** Unique name of the serialized event. */
    public static final SerializedDataType SER_TYPE = new SerializedDataType(TYPE.asBaseType());

    #foreach ($field in $fields)
    #foreach ($annotation in $field.annotations)
    ${annotation}
    #end
    #if($jsonb)
    @JsonbProperty("${field.property}")
    #end
    #if($jackson)
    @JsonProperty("${field.property}")
    #end
    #if($jaxb)
    @XmlAttribute(name = "${field.property}")
    #end
    private ${field.type} ${field.name};

    #end

    /**
     * Protected default constructor for deserialization.
     */
    protected ${class}() { // NOSONAR Default constructor
        super();
    }

    @Override
    #if($jackson)
    @JsonIgnore
    #end
    public EventType getEventType() {
        return TYPE;
    }

    #foreach ($field in $fields)
    /**
     * Returns: ${field.label}.
     *
     * @return ${field.label}. ${field.description}.
     */
    #if(${field.nullable})@NotNull#end
    public ${field.type} ${field.nameGetter}() {
        return ${field.name};
    }
    #end

    @Override
    public String toString() {
        return "${message}";
    }

    /**
     * Creates a new builder instance.
     *
     * @return New builder instance.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builds an instance of the outer class.
     */
    public static final class Builder extends AbstractDomainEvent.Builder<${idClass.simpleName}, ${class}, Builder> {

        private ${class} delegate;

        private Builder() {
            super(new ${class}());
            delegate = delegate();
        }

        #foreach (${field} in ${fields})
        /**
         * Sets: ${field.label}.
         *
         * @param ${field.name} ${field.label}.
         * @return This builder.
         */
        @SuppressWarnings("unchecked")
        public final Builder ${field.name}(@NotNull final ${field.type} ${field.name}) {
            Contract.requireArgNotNull("${field.name}", ${field.name});
            delegate.${field.name} = ${field.name};
            return this;
        }
        #end

        /**
         * Creates the event and clears the builder.
         *
         * @return New instance.
         */
        public ${class} build() {
            ensureBuildableAbstractDomainEvent();
            final ${class} result = delegate;
            delegate = new ${class}();
            resetAbstractDomainEvent(delegate);
            return result;
        }

    }

}
