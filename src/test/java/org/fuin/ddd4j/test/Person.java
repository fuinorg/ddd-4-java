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
import org.fuin.ddd4j.ddd.AbstractEntity;
import org.fuin.ddd4j.ddd.ApplyEvent;
import org.fuin.ddd4j.ddd.EntityType;
import org.fuin.objects4j.common.Contract;

/**
 * Person entity.
 */
public class Person extends AbstractEntity<VendorId, Vendor, PersonId> {

    private PersonId id;

    private PersonName name;

    /**
     * Constructor with all data.
     * 
     * @param vendor
     *            Aggregate root.
     * @param id
     *            Unique identifier.
     * @param name
     *            Name.
     */
    public Person(@NotNull final Vendor vendor, final PersonId id, @NotNull final PersonName name) {
        super(vendor);

        // CHECK PRECONDITIONS
        Contract.requireArgNotNull("vendor", vendor);
        Contract.requireArgNotNull("id", id);
        Contract.requireArgNotNull("name", name);

        // NO EVENT HERE! THE EVENT FOR CONSTRUCTION IS
        // ALWAYS FIRED BY THE PARENT ENTITY
        this.id = id;
        this.name = name;

    }

    /**
     * Changes the name.
     * 
     * @param newName
     *            New name.
     */
    public final void changeName(@NotNull final PersonName newName) {

        // CHECK PRECONDITIONS
        Contract.requireArgNotNull("newName", newName);

        // VERIFY BUSINESS RULES
        // Nothing to do

        // HANDLE EVENT
        apply(new PersonNameChangedEvent(getRoot().getRef(), id, name, newName));

    }

    @ApplyEvent
    private final void applyEvent(final PersonNameChangedEvent event) {
        this.name = event.getNewName();
    }

    @Override
    public final EntityType getType() {
        return PersonId.ENTITY_TYPE;
    }

    @Override
    public final PersonId getId() {
        return id;
    }

}
