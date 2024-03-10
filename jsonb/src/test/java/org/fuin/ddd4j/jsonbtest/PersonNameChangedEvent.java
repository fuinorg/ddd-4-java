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
package org.fuin.ddd4j.jsonbtest;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.validation.constraints.NotNull;
import org.fuin.ddd4j.core.EntityIdPath;
import org.fuin.ddd4j.core.EventType;
import org.fuin.ddd4j.jsonb.AbstractDomainEvent;
import org.fuin.esc.api.HasSerializedDataTypeConstant;
import org.fuin.esc.api.SerializedDataType;
import org.fuin.objects4j.common.Contract;

import java.io.Serial;

/**
 * The name of a person entity was changed.
 */
@HasSerializedDataTypeConstant
public final class PersonNameChangedEvent extends AbstractDomainEvent<PersonId> {

    @Serial
    private static final long serialVersionUID = 1000L;

    /**
     * Unique name of the event used to store it - Should never change.
     */
    public static final EventType TYPE = new EventType(PersonNameChangedEvent.class.getSimpleName());

    /**
     * Unique name of the serialized event.
     */
    public static final SerializedDataType SER_TYPE = new SerializedDataType(TYPE.asBaseType());

    @JsonbProperty("vendor-ref")
    private VendorRef vendorRef;

    @JsonbProperty("person-id")
    private PersonId personId;

    @JsonbProperty("old-name")
    private PersonName oldName;

    @JsonbProperty("new-name")
    private PersonName newName;

    /**
     * Default constructor only for deserialization.
     */
    protected PersonNameChangedEvent() {
        super();
    }

    /**
     * Constructor with event data.
     *
     * @param vendorRef Aggregate root reference.
     * @param personId  Person ID.
     * @param oldName   Person name.
     * @param newName   Person name.
     */
    public PersonNameChangedEvent(@NotNull final VendorRef vendorRef, @NotNull final PersonId personId, @NotNull final PersonName oldName,
                                  @NotNull final PersonName newName) {
        super(new EntityIdPath(vendorRef.getId(), personId));
        Contract.requireArgNotNull("vendorRef", vendorRef);
        Contract.requireArgNotNull("personId", personId);
        Contract.requireArgNotNull("oldName", oldName);
        Contract.requireArgNotNull("newName", newName);
        this.vendorRef = vendorRef;
        this.personId = personId;
        this.oldName = oldName;
        this.newName = newName;
    }

    @Override
    public final EventType getEventType() {
        return TYPE;
    }

    /**
     * Returns the person ID.
     *
     * @return Person ID.
     */
    @NotNull
    public final PersonId getPersonId() {
        return personId;
    }

    /**
     * Returns the old person name.
     *
     * @return Old name.
     */
    @NotNull
    public final PersonName getOldName() {
        return oldName;
    }

    /**
     * Returns the new person name.
     *
     * @return New name.
     */
    @NotNull
    public final PersonName getNewName() {
        return newName;
    }

    @Override
    public final String toString() {
        return "Changed name of person #" + personId + " from '" + oldName + "' to '" + newName + "' for " + vendorRef;
    }

}
