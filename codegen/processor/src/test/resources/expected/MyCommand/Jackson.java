package org.fuin.ddd4j.codegen.test;

import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.fuin.ddd4j.core.EntityIdPath;
import org.fuin.ddd4j.core.EventId;
import org.fuin.ddd4j.core.EventType;
import org.fuin.cqrs4j.jackson.AbstractAggregateCommand;
import org.fuin.ddd4j.jackson.AbstractDomainEvent;
import org.fuin.esc.api.HasSerializedDataTypeConstant;
import org.fuin.esc.api.SerializedDataType;
import org.fuin.objects4j.common.Contract;

import java.io.Serial;
import java.time.ZonedDateTime;

import javax.annotation.concurrent.Immutable;

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
    @JsonProperty("my-id")
    private MyId myId;


    /**
     * Protected default constructor for deserialization.
     */
    protected MyCommand() { // NOSONAR Default constructor
        super();
    }

    @Override
    @JsonIgnore
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

    @Override
    public String toString() {
        return "Issued MyCommand";
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
