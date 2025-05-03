package org.fuin.ddd4j.codegen.test;

import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbTypeAdapter;
import javax.annotation.Nullable;
import org.fuin.ddd4j.core.EntityIdPath;
import org.fuin.ddd4j.core.EventType;
import org.fuin.ddd4j.jsonb.AbstractDomainEvent;
import org.fuin.esc.api.HasSerializedDataTypeConstant;
import org.fuin.esc.api.SerializedDataType;
import org.fuin.objects4j.common.Contract;

import java.io.Serial;

import javax.annotation.concurrent.Immutable;

import org.fuin.ddd4j.codegen.test.MyId;
import jakarta.validation.constraints.NotNull;
import org.fuin.objects4j.ui.ShortLabel;
import org.fuin.objects4j.ui.Label;
import org.fuin.objects4j.ui.Tooltip;
import org.fuin.objects4j.ui.Examples;

/**
 * Something important happened.
 */
@Immutable
@Schema(name = "MyEvent", type = SchemaType.OBJECT, description = "Something important happened")
@HasSerializedDataTypeConstant
public final class MyEvent extends AbstractDomainEvent<MyId> {

    @Serial
    private static final long serialVersionUID = 1000L;

    /** Unique name of the event used to store it - Should never change. */
    public static final EventType TYPE = new EventType(MyEvent.class.getSimpleName());

    /** Unique name of the serialized event. */
    public static final SerializedDataType SER_TYPE = new SerializedDataType(TYPE.asBaseType());

    @NotNull
    @ShortLabel("ROOT-ID")
    @Label("Root Identifier")
    @Tooltip("Uniquely identifies The Root")
    @Examples({"e4baf6c5-ccb9-4580-9d59-41860c140189", "00000000-0000-0000-0000-000000000000"})
    @JsonbProperty("my-id")
    private MyId myId;


    /**
     * Protected default constructor for deserialization.
     */
    protected MyEvent() { // NOSONAR Default constructor
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
    public MyId getMyId() {
        return myId;
    }

    @Override
    public String toString() {
        return "MyEvent happened";
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
    public static final class Builder extends AbstractDomainEvent.Builder<MyId, MyEvent, Builder> {

        private MyEvent delegate;

        private Builder() {
            super(new MyEvent());
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
         * Creates the event and clears the builder.
         *
         * @return New instance.
         */
        public MyEvent build() {
            ensureBuildableAbstractDomainEvent();
            final MyEvent result = delegate;
            delegate = new MyEvent();
            resetAbstractDomainEvent(delegate);
            return result;
        }

    }

}
