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

import org.fuin.ddd4j.ddd.DomainEvent;
import org.fuin.ddd4j.test.Vendor.ConstructorService;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

//CHECKSTYLE:OFF
public class VendorExampleTest {

    @Test
    public void testConstructor() throws DuplicateVendorKeyException {

        // PREPARE
        VendorId id = new VendorId(UUID.randomUUID());
        VendorKey key = new VendorKey("V00001");
        VendorName name = new VendorName("Peter Parker Inc.");
        ConstructorService service = new ConstructorService() {
            @Override
            public void addVendorKey(VendorKey key) throws DuplicateVendorKeyException {
                // Do nothing
            }
        };

        // TEST
        Vendor vendor = new Vendor(id, key, name, service);

        // VERIFY
        List<DomainEvent<?>> events = vendor.getUncommittedChanges();
        assertThat(events).hasSize(1);
        assertThat(events.get(0)).isInstanceOf(VendorCreatedEvent.class);

    }

    @Test
    public void testAddPerson() throws DuplicateVendorKeyException {

        // PREPARE
        Vendor vendor = createVendor();
        final PersonName personName = new PersonName("Peter Parker");

        // TEST
        vendor.addPerson(personName);

        // VERIFY
        List<DomainEvent<?>> events = vendor.getUncommittedChanges();
        assertThat(events).hasSize(1);
        assertThat(events.get(0)).isInstanceOf(PersonCreatedEvent.class);

    }

    @Test
    public void testChangePersonName() throws DuplicateVendorKeyException, PersonNotFoundException {

        // PREPARE
        Vendor vendor = createVendor();
        final PersonName oldName = new PersonName("Peter Parker");
        vendor.addPerson(oldName);
        final PersonId personId = ((PersonCreatedEvent) vendor.getUncommittedChanges().get(0)).getPersonId();
        vendor.markChangesAsCommitted();
        final PersonName newName = new PersonName("Harry Osborn");

        // TEST
        vendor.changePersonName(personId, newName);

        // VERIFY
        List<DomainEvent<?>> events = vendor.getUncommittedChanges();
        assertThat(events).hasSize(1);
        assertThat(events.get(0)).isInstanceOf(PersonNameChangedEvent.class);
        PersonNameChangedEvent event = (PersonNameChangedEvent) events.get(0);
        assertThat(event.getOldName()).isEqualTo(oldName);
        assertThat(event.getNewName()).isEqualTo(newName);

    }

    private Vendor createVendor() throws DuplicateVendorKeyException {
        VendorId id = new VendorId(UUID.randomUUID());
        VendorKey key = new VendorKey("V00001");
        VendorName name = new VendorName("Peter Parker Inc.");
        ConstructorService service = new ConstructorService() {
            @Override
            public void addVendorKey(VendorKey key) throws DuplicateVendorKeyException {
                // Do nothing
            }
        };
        final Vendor vendor = new Vendor(id, key, name, service);
        vendor.markChangesAsCommitted();
        return vendor;
    }

}
// CHECKSTYLE:ON
