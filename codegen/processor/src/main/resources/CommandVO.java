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
import org.fuin.ddd4j.core.EntityIdPath;
import org.fuin.ddd4j.core.EventId;
import org.fuin.ddd4j.core.EventType;
#if($jsonb)
import org.fuin.cqrs4j.jsonb.AbstractAggregateCommand;
import org.fuin.ddd4j.jsonb.AbstractDomainEvent;
#end
#if($jackson)
import org.fuin.cqrs4j.jackson.AbstractAggregateCommand;
import org.fuin.ddd4j.jackson.AbstractDomainEvent;
#end
#if($jaxb)
import org.fuin.cqrs4j.jaxb.AbstractAggregateCommand;
import org.fuin.ddd4j.jaxb.AbstractDomainEvent;
#end
import org.fuin.esc.api.HasSerializedDataTypeConstant;
import org.fuin.esc.api.SerializedDataType;
import org.fuin.objects4j.common.Contract;
#if($!aggregateIdClass.pkg)
import ${aggregateIdClass};
#end
#if($!entityIdClass.pkg)
import ${entityIdClass};
#end

import java.io.Serial;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.concurrent.Immutable;
import org.fuin.utils4j.Utils4J;

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
public final class ${class} extends AbstractAggregateCommand<${aggregateIdClass.simpleName}, ${entityIdClass.simpleName}> {

    @Serial
    private static final long serialVersionUID = ${serialVersionUID}L;

    /** Unique name of the command used to store it - Should never change. */
    public static final EventType TYPE = new EventType(${class}.class.getSimpleName());

    /** Unique name of the serialized command. */
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
    #if(${field.nullable})
    @Nullable
    #else
    @NotNull
    #end
    public ${field.type} ${field.nameGetter}() {
        return ${field.name};
    }
    #end

    @Override
    public String toString() {
        final Map<String, String> vars = new HashMap<>();
        vars.put("entityIdPath", getEntityIdPath().toString());
        #foreach ($field in $fields)
        vars.put("${field.name}", "" + ${field.name});
        #end
        return Utils4J.replaceVars("${message}", vars);
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
    public static final class Builder extends AbstractAggregateCommand.Builder<${aggregateIdClass.simpleName}, ${entityIdClass.simpleName}, ${class}, Builder> {

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
        public final Builder ${field.name}(#if(${field.nullable})@Nullable#else@NotNull#end final ${field.type} ${field.name}) {
            #if(!${field.nullable})
            Contract.requireArgNotNull("${field.name}", ${field.name});
            #end
            delegate.${field.name} = ${field.name};
            return this;
        }
        #end

        /**
         * Creates the command and clears the builder.
         *
         * @return New instance.
         */
        public ${class} build() {
            ensureBuildableAbstractAggregateCommand();
            if (delegate.getEventId() == null) {
                this.eventId(new EventId());
            }
            if (delegate.getEventTimestamp() == null) {
                this.timestamp(ZonedDateTime.now());
            }
            final ${class} result = delegate;
            delegate = new ${class}();
            resetAbstractAggregateCommand(delegate);
            return result;
        }

    }

}
