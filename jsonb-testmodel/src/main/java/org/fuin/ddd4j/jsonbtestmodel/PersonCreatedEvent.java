/**
 * Copyright (C) 2015 Michael Schnell. All rights reserved.
 * http://www.fuin.org/
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see http://www.gnu.org/licenses/.
 */
package org.fuin.ddd4j.jsonbtestmodel;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbTypeAdapter;
import jakarta.validation.constraints.NotNull;
import org.fuin.ddd4j.core.EntityIdPath;
import org.fuin.ddd4j.core.EventType;
import org.fuin.ddd4j.jsonb.AbstractDomainEvent;
import org.fuin.esc.api.HasSerializedDataTypeConstant;
import org.fuin.esc.api.SerializedDataType;
import org.fuin.objects4j.common.Contract;

import java.io.Serial;

/**
 * A person entity was created.
 */
@HasSerializedDataTypeConstant
public final class PersonCreatedEvent extends AbstractDomainEvent<PersonId> {

    @Serial
    private static final long serialVersionUID = 1000L;

    /** Unique name of the event used to store it - Should never change. */
    public static final EventType TYPE = new EventType(PersonCreatedEvent.class.getSimpleName());

    /** Unique name of the serialized event. */
    public static final SerializedDataType SER_TYPE = new SerializedDataType(TYPE.asBaseType());

    @JsonbProperty("vendor-ref")
    private VendorRef vendorRef;

    @JsonbProperty("person-id")
    @JsonbTypeAdapter(PersonIdJsonbAdapter.class)
    private PersonId personId;

    @JsonbProperty("person-name")
    @JsonbTypeAdapter(PersonNameJsonbAdapter.class)
    private PersonName personName;

    /**
     * Default constructor only for deserialization.
     */
    protected PersonCreatedEvent() {
        super();
    }

    /**
     * Constructor with event data.
     *
     * @param vendorRef
     *            Aggregate root reference.
     * @param personId
     *            Person ID.
     * @param personName
     *            Person name.
     */
    protected PersonCreatedEvent(@NotNull final VendorRef vendorRef,
                                 @NotNull final PersonId personId,
                                 @NotNull final PersonName personName) {
        super(new EntityIdPath(vendorRef.getId()));
        this.vendorRef = vendorRef;
        this.personId = personId;
        this.personName = personName;
    }

    @Override
    public EventType getEventType() {
        return TYPE;
    }

    /**
     * Returns the person ID.
     *
     * @return Person ID.
     */
    @NotNull
    public PersonId getPersonId() {
        return personId;
    }

    /**
     * Returns the person name.
     *
     * @return Person name.
     */
    @NotNull
    public PersonName getPersonName() {
        return personName;
    }

    @Override
    public String toString() {
        return "Created person #" + personId + " with name '" + personName + "' for " + vendorRef;
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
    public static final class Builder extends AbstractDomainEvent.Builder<PersonId, PersonCreatedEvent, Builder> {

        private PersonCreatedEvent delegate;

        private Builder() {
            super(new PersonCreatedEvent());
            delegate = delegate();
        }

        /**
         * Sets the vendor reference.
         *
         * @param vendorRef Vendor reference.
         * @return This builder.
         */
        @SuppressWarnings("unchecked")
        public final Builder vendorRef(@NotNull final VendorRef vendorRef) {
            Contract.requireArgNotNull("vendorRef", vendorRef);
            delegate.vendorRef = vendorRef;
            return this;
        }

        /**
         * Sets the unique identifier of the person.
         *
         * @param personId Unique identifier of the person.
         * @return This builder.
         */
        @SuppressWarnings("unchecked")
        public final Builder personId(@NotNull final PersonId personId) {
            Contract.requireArgNotNull("personId", personId);
            delegate.personId = personId;
            return this;
        }

        /**
         * Sets the name of the person.
         *
         * @param personName Name of the person.
         * @return This builder.
         */
        @SuppressWarnings("unchecked")
        public final Builder personName(@NotNull final PersonName personName) {
            Contract.requireArgNotNull("personName", personName);
            delegate.personName = personName;
            return this;
        }

        /**
         * Creates the event and clears the builder.
         *
         * @return New instance.
         */
        public PersonCreatedEvent build() {
            ensureBuildableAbstractDomainEvent();
            final PersonCreatedEvent result = delegate;
            delegate = new PersonCreatedEvent();
            resetAbstractDomainEvent(delegate);
            return result;
        }

    }

}
