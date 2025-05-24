package org.fuin.ddd4j.codegen.test;

import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbTypeAdapter;
import org.fuin.ddd4j.core.EntityIdPath;
import org.fuin.ddd4j.core.EventId;
import org.fuin.ddd4j.core.EventType;
import org.fuin.cqrs4j.jsonb.AbstractAggregateCommand;
import org.fuin.ddd4j.jsonb.AbstractDomainEvent;
import org.fuin.esc.api.HasSerializedDataTypeConstant;
import org.fuin.esc.api.SerializedDataType;
import org.fuin.objects4j.common.Contract;

import java.io.Serial;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.concurrent.Immutable;
import org.fuin.utils4j.Utils4J;

import jakarta.annotation.Nullable;
import org.fuin.ddd4j.codegen.test.MyId;
import org.fuin.objects4j.ui.Examples;
import org.fuin.objects4j.ui.Label;
import org.fuin.objects4j.ui.ShortLabel;
import org.fuin.objects4j.ui.Tooltip;

/**
 * Do it!.
 */
@Immutable
@Schema(name = "MyCommand", type = SchemaType.OBJECT, description = "Do it!")
@HasSerializedDataTypeConstant
public final class MyCommand extends AbstractAggregateCommand<MyRootId, MyId> {

    @Serial
    private static final long serialVersionUID = 1000L;

    /** Unique name of the command used to store it - Should never change. */
    public static final EventType TYPE = new EventType(MyCommand.class.getSimpleName());

    /** Unique name of the serialized command. */
    public static final SerializedDataType SER_TYPE = new SerializedDataType(TYPE.asBaseType());

    @ShortLabel("ROOT-ID")
    @Label("Root Identifier")
    @Tooltip("Uniquely identifies The Root")
    @Examples({"e4baf6c5-ccb9-4580-9d59-41860c140189", "00000000-0000-0000-0000-000000000000"})
    @JsonbProperty("my-id")
    private MyId myId;

    @Label("Nullable Field")
    @Nullable
    @JsonbProperty("field-nullable")
    private String fieldNullable;

    @Label("Non-Null Field")
    @JsonbProperty("field-non-null")
    private String fieldNonNull;


    /**
     * Protected default constructor for deserialization.
     */
    protected MyCommand() { // NOSONAR Default constructor
        super();
    }

    @Override
    public EventType getEventType() {
        return TYPE;
    }

    /**
     * Returns: Root Identifier.
     *
     * @return Root Identifier. Uniquely identifies The Root.
     */
    @NotNull
    public MyId getMyId() {
        return myId;
    }
    /**
     * Returns: Nullable Field.
     *
     * @return Nullable Field. TODO Add '@Tooltip' annotation.
     */
    @Nullable
    public String getFieldNullable() {
        return fieldNullable;
    }
    /**
     * Returns: Non-Null Field.
     *
     * @return Non-Null Field. TODO Add '@Tooltip' annotation.
     */
    @NotNull
    public String getFieldNonNull() {
        return fieldNonNull;
    }

    @Override
    public String toString() {
        final Map<String, String> vars = new HashMap<>();
        vars.put("entityIdPath", getEntityIdPath().toString());
        vars.put("myId", "" + myId);
        vars.put("fieldNullable", "" + fieldNullable);
        vars.put("fieldNonNull", "" + fieldNonNull);
        return Utils4J.replaceVars("Issued MyCommand", vars);
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
    public static final class Builder extends AbstractAggregateCommand.Builder<MyRootId, MyId, MyCommand, Builder> {

        private MyCommand delegate;

        private Builder() {
            super(new MyCommand());
            delegate = delegate();
        }

        /**
         * Sets: Root Identifier.
         *
         * @param myId Root Identifier.
         * @return This builder.
         */
        @SuppressWarnings("unchecked")
        public final Builder myId(@NotNull final MyId myId) {
            Contract.requireArgNotNull("myId", myId);
            delegate.myId = myId;
            return this;
        }
        /**
         * Sets: Nullable Field.
         *
         * @param fieldNullable Nullable Field.
         * @return This builder.
         */
        @SuppressWarnings("unchecked")
        public final Builder fieldNullable(@Nullable final String fieldNullable) {
            delegate.fieldNullable = fieldNullable;
            return this;
        }
        /**
         * Sets: Non-Null Field.
         *
         * @param fieldNonNull Non-Null Field.
         * @return This builder.
         */
        @SuppressWarnings("unchecked")
        public final Builder fieldNonNull(@NotNull final String fieldNonNull) {
            Contract.requireArgNotNull("fieldNonNull", fieldNonNull);
            delegate.fieldNonNull = fieldNonNull;
            return this;
        }

        /**
         * Creates the command and clears the builder.
         *
         * @return New instance.
         */
        public MyCommand build() {
            ensureBuildableAbstractAggregateCommand();
            if (delegate.getEventId() == null) {
                this.eventId(new EventId());
            }
            if (delegate.getEventTimestamp() == null) {
                this.timestamp(ZonedDateTime.now());
            }
            final MyCommand result = delegate;
            delegate = new MyCommand();
            resetAbstractAggregateCommand(delegate);
            return result;
        }

    }

}
