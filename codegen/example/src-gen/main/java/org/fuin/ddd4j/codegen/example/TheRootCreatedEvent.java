package org.fuin.ddd4j.codegen.example;

import jakarta.validation.constraints.NotNull;
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

import jakarta.annotation.Nonnull;
import org.fuin.objects4j.ui.ShortLabel;
import org.fuin.objects4j.ui.Label;
import org.fuin.objects4j.ui.Tooltip;
import org.fuin.objects4j.ui.Examples;
import jakarta.annotation.Nonnull;
import org.fuin.objects4j.ui.ShortLabel;
import org.fuin.objects4j.ui.Label;
import org.fuin.objects4j.ui.Tooltip;
import org.fuin.objects4j.ui.Examples;
import org.fuin.objects4j.ui.ShortLabel;
import org.fuin.objects4j.ui.Label;
import org.fuin.objects4j.ui.Tooltip;
import org.fuin.objects4j.ui.Examples;

/**
 * The root was created.
 */
@Immutable
@HasSerializedDataTypeConstant
public final class TheRootCreatedEvent extends AbstractDomainEvent<TheRootId> {

    @Serial
    private static final long serialVersionUID = 1000L;

    /** Unique name of the event used to store it - Should never change. */
    public static final EventType TYPE = new EventType(TheRootCreatedEvent.class.getSimpleName());

    /** Unique name of the serialized event. */
    public static final SerializedDataType SER_TYPE = new SerializedDataType(TYPE.asBaseType());

    @Nonnull
    @ShortLabel("ROOT-ID")
    @Label("Root Identifier")
    @Tooltip("Uniquely identifies The Root")
    @Examples({"e4baf6c5-ccb9-4580-9d59-41860c140189", "00000000-0000-0000-0000-000000000000"})
    @JsonbProperty("id")
    private TheRootId id;

    @Nonnull
    @ShortLabel("ROOT-NAME")
    @Label("Root Name")
    @Tooltip("Name of The Root")
    @Examples({"FooBar", "Barefoot"})
    @JsonbProperty("name")
    private TheRootName name;

    @ShortLabel("FOO")
    @Label("The foo")
    @Tooltip("Whatever the \'foo\' is...")
    @Examples({"0", "1", "123456"})
    @JsonbProperty("foo")
    private int foo;


    /**
     * Protected default constructor for deserialization.
     */
    protected TheRootCreatedEvent() { // NOSONAR Default constructor
        super();
    }

    /**
     * Constructor with event data.
     *
     * @param id Root Identifier. Uniquely identifies The Root.
     * @param name Root Name. Name of The Root.
     * @param foo The foo. Whatever the 'foo' is....
     */
    protected TheRootCreatedEvent(
        final TheRootId id,
        final TheRootName name,
        final int foo
    ) {
        super(new EntityIdPath(id));
        this.id = id;
        this.name = name;
        this.foo = foo;
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
    public TheRootId getId() {
        return id;
    }
    /**
     * Returns: Root Name.
     *
     * @return Root Name. Name of The Root.
     */
    public TheRootName getName() {
        return name;
    }
    /**
     * Returns: The foo.
     *
     * @return The foo. Whatever the 'foo' is....
     */
    public int getFoo() {
        return foo;
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
    public static final class Builder extends AbstractDomainEvent.Builder<TheRootId, TheRootCreatedEvent, Builder> {

        private TheRootCreatedEvent delegate;

        private Builder() {
            super(new TheRootCreatedEvent());
            delegate = delegate();
        }

        /**
         * Sets: Root Identifier.
         *
         * @param id Root Identifier.
         * @return This builder.
         */
        @SuppressWarnings("unchecked")
        public final Builder id(@NotNull final TheRootId id) {
            Contract.requireArgNotNull("id", id);
            delegate.id = id;
            return this;
        }
        /**
         * Sets: Root Name.
         *
         * @param name Root Name.
         * @return This builder.
         */
        @SuppressWarnings("unchecked")
        public final Builder name(@NotNull final TheRootName name) {
            Contract.requireArgNotNull("name", name);
            delegate.name = name;
            return this;
        }
        /**
         * Sets: The foo.
         *
         * @param foo The foo.
         * @return This builder.
         */
        @SuppressWarnings("unchecked")
        public final Builder foo(@NotNull final int foo) {
            Contract.requireArgNotNull("foo", foo);
            delegate.foo = foo;
            return this;
        }

        /**
         * Creates the event and clears the builder.
         *
         * @return New instance.
         */
        public TheRootCreatedEvent build() {
            ensureBuildableAbstractDomainEvent();
            final TheRootCreatedEvent result = delegate;
            delegate = new TheRootCreatedEvent();
            resetAbstractDomainEvent(delegate);
            return result;
        }

    }

}
