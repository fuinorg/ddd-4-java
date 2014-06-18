/**
 * Copyright (C) 2013 Future Invent Informationsmanagement GmbH. All rights
 * reserved. <http://www.fuin.org/>
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fuin.ddd4j.test;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.fuin.ddd4j.ddd.AbstractDomainEvent;
import org.fuin.ddd4j.ddd.EntityIdPath;
import org.fuin.ddd4j.ddd.EventType;
import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.common.NeverNull;

/**
 * The name of a person entity was changed.
 */
@XmlRootElement(name = "person-name-changed-event")
public final class PersonNameChangedEvent extends AbstractDomainEvent<PersonId> {

    private static final long serialVersionUID = 1000L;

    /** Unique name of the event used to store it - Should never change. */
    public static final EventType TYPE = new EventType(
            PersonNameChangedEvent.class.getSimpleName());

    @XmlElement(name = "vendor-ref")
    private VendorRef vendorRef;

    @XmlAttribute(name = "person-id")
    private PersonId personId;

    @XmlAttribute(name = "old-name")
    private PersonName oldName;

    @XmlAttribute(name = "new-name")
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
     * @param vendorRef
     *            Aggregate root reference.
     * @param personId
     *            Person ID.
     * @param oldName
     *            Person name.
     * @param newName
     *            Person name.
     */
    public PersonNameChangedEvent(@NotNull final VendorRef vendorRef,
            @NotNull final PersonId personId,
            @NotNull final PersonName oldName, @NotNull final PersonName newName) {
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
    @NeverNull
    public final PersonId getPersonId() {
        return personId;
    }

    /**
     * Returns the old person name.
     * 
     * @return Old name.
     */
    @NeverNull
    public final PersonName getOldName() {
        return oldName;
    }

    /**
     * Returns the new person name.
     * 
     * @return New name.
     */
    @NeverNull
    public final PersonName getNewName() {
        return newName;
    }

    @Override
    public final String toString() {
        return "Changed name of person #" + personId + " from '" + oldName
                + "' to '" + newName + "' for " + vendorRef;
    }

}
