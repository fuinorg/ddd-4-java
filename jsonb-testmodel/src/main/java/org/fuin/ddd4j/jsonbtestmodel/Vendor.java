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

import jakarta.validation.constraints.NotNull;
import org.fuin.ddd4j.core.AbstractAggregateRoot;
import org.fuin.ddd4j.core.ApplyEvent;
import org.fuin.ddd4j.core.ChildEntityLocator;
import org.fuin.ddd4j.core.EntityType;
import org.fuin.objects4j.common.Contract;

import java.util.ArrayList;
import java.util.List;

/**
 * Vendor aggregate.
 */
public class Vendor extends AbstractAggregateRoot<VendorId> {

    private VendorRef ref;

    private Integer lastPersonId;

    private List<Person> persons;

    /**
     * Default constructor used by the repositories. NEVER use in your application code!
     */
    public Vendor() {
        super();
    }

    /**
     * Constructor with all data.
     *
     * @param id
     *            Unique identifier.
     * @param key
     *            Unique key.
     * @param name
     *            Name.
     * @param service
     *            Service used by the method.
     *
     * @throws DuplicateVendorKeyException
     *             The given key already exists.
     */
    public Vendor(@NotNull final VendorId id, @NotNull final VendorKey key, @NotNull final VendorName name,
                  @NotNull final ConstructorService service) throws DuplicateVendorKeyException {
        super();

        // CHECK PRECONDITIONS
        Contract.requireArgNotNull("id", id);
        Contract.requireArgNotNull("key", key);
        Contract.requireArgNotNull("name", name);
        Contract.requireArgNotNull("service", service);

        // VERIFY BUSINESS RULES
        service.addVendorKey(key);

        // HANDLE EVENT
        apply(new VendorCreatedEvent(new VendorRef(id, key, name)));
    }

    @ApplyEvent
    private void applyEvent(final VendorCreatedEvent event) {
        this.ref = event.getVendorRef();
    }

    /**
     * Adds a person with the given name.
     *
     * @param name
     *            Name of the person to add.
     */
    public final void addPerson(@NotNull final PersonName name) {

        // CHECK PRECONDITIONS
        Contract.requireArgNotNull("name", name);

        // VERIFY BUSINESS RULES
        // Nothing to do here

        // HANDLE EVENT
        apply(new PersonCreatedEvent(ref, new PersonId(nextId()), name));

    }

    @ApplyEvent
    private void applyEvent(final PersonCreatedEvent event) {
        this.lastPersonId = event.getPersonId().asBaseType();
        if (persons == null) {
            persons = new ArrayList<>();
        }
        persons.add(new Person(this, event.getPersonId(), event.getPersonName()));
    }

    private int nextId() {
        if (lastPersonId == null) {
            return 1;
        }
        return lastPersonId + 1;
    }

    /**
     * Changes the name of a person.
     *
     * @param personId
     *            Person ID.
     * @param newName
     *            New name.
     *
     * @throws PersonNotFoundException
     *             No person with teh given ID was found.
     */
    public final void changePersonName(@NotNull final PersonId personId, @NotNull final PersonName newName) throws PersonNotFoundException {

        // CHECK PRECONDITIONS
        Contract.requireArgNotNull("personId", personId);
        Contract.requireArgNotNull("newName", newName);

        // LOCATE PERSON
        final Person person = findPerson(personId);
        if (person == null) {
            throw new PersonNotFoundException(getRef(), personId);
        }

        // FORWARD CHANGE REQUEST TO PERSON
        person.changeName(newName);

    }

    @ChildEntityLocator
    private Person findPerson(final PersonId personId) {
        for (final Person child : persons) {
            if (child.getId().equals(personId)) {
                return child;
            }
        }
        return null;
    }

    @Override
    public final EntityType getType() {
        return VendorId.TYPE;
    }

    @Override
    public final VendorId getId() {
        return ref.getId();
    }

    /**
     * Returns the reference.
     *
     * @return Vendor reference.
     */
    public final VendorRef getRef() {
        return ref;
    }

    /**
     * Interface for the constructor.
     */
    public interface ConstructorService {

        /**
         * Adds a new vendor key to the context. The key will be persisted or an exception will be thrown if it already exists.
         *
         * @param key
         *            Key to verify and persist.
         *
         * @throws DuplicateVendorKeyException
         *             The given key already exists.
         */
        void addVendorKey(VendorKey key) throws DuplicateVendorKeyException;

    }

}
