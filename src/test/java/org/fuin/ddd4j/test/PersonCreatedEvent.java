/**
 * Copyright (C) 2015 Michael Schnell. All rights reserved. 
 * http://www.fuin.org/
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
 * along with this library. If not, see http://www.gnu.org/licenses/.
 */
package org.fuin.ddd4j.test;

import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import org.fuin.ddd4j.ddd.AbstractDomainEvent;
import org.fuin.ddd4j.ddd.EntityIdPath;
import org.fuin.ddd4j.ddd.EventType;

/**
 * A person entity was created.
 */
@XmlRootElement(name = "person-created-event")
public final class PersonCreatedEvent extends AbstractDomainEvent<PersonId> {

    private static final long serialVersionUID = 1000L;

    /** Unique name of the event used to store it - Should never change. */
    public static final EventType TYPE = new EventType(PersonCreatedEvent.class.getSimpleName());

    @XmlElement(name = "vendor-ref")
    private VendorRef vendorRef;

    @XmlAttribute(name = "person-id")
    private PersonId personId;

    @XmlAttribute(name = "person-name")
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
    public PersonCreatedEvent(@NotNull final VendorRef vendorRef, @NotNull final PersonId personId, @NotNull final PersonName personName) {
        super(new EntityIdPath(vendorRef.getId()));
        this.vendorRef = vendorRef;
        this.personId = personId;
        this.personName = personName;
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
     * Returns the person name.
     * 
     * @return Person name.
     */
    @NotNull
    public final PersonName getPersonName() {
        return personName;
    }

    @Override
    public final String toString() {
        return "Created person #" + personId + " with name '" + personName + "' for " + vendorRef;
    }

}
