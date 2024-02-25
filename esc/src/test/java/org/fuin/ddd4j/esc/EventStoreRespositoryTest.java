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
package org.fuin.ddd4j.esc;

import org.assertj.core.api.Assertions;
import org.fuin.ddd4j.core.AggregateNotFoundException;
import org.fuin.ddd4j.jsonbtestmodel.PersonCreatedEvent;
import org.fuin.ddd4j.jsonbtestmodel.PersonName;
import org.fuin.ddd4j.jsonbtestmodel.Vendor;
import org.fuin.ddd4j.jsonbtestmodel.VendorId;
import org.fuin.ddd4j.jsonbtestmodel.VendorKey;
import org.fuin.ddd4j.jsonbtestmodel.VendorName;
import org.fuin.esc.api.EventStore;
import org.fuin.esc.api.StreamEventsSlice;
import org.fuin.esc.mem.InMemoryEventStore;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

public class EventStoreRespositoryTest {

    @Test
    public void testCreateAggregate() throws Exception {

        // PREPARE
        try (final EventStore eventStore = new InMemoryEventStore(Executors.newCachedThreadPool())) {
            eventStore.open();

            final VendorRepository repo = new VendorRepository(eventStore);

            final VendorId vendorId = new VendorId();
            final VendorKey vendorKey = new VendorKey("V00001");
            final VendorName vendorName = new VendorName("Hazards International Inc.");
            Vendor vendor = new Vendor(vendorId, vendorKey, vendorName, key -> {
                // Do nothing
            });

            // TEST
            repo.update(vendor);

            // VERIFY
            final AggregateStreamId streamId = new AggregateStreamId(VendorId.TYPE, "vendorId", vendorId);
            final StreamEventsSlice slice = eventStore.readEventsForward(streamId, 0, 100);
            Assertions.assertThat(slice.getEvents()).hasSize(1);
            vendor = repo.read(vendorId, vendor.getVersion());
            Assertions.assertThat(vendor.getVersion()).isEqualTo(0);
            vendor = repo.read(vendorId);
            Assertions.assertThat(vendor.getVersion()).isEqualTo(0);

        }

    }

    @Test
    public void testUpdateAggregate() throws Exception {

        // PREPARE
        try (final EventStore eventStore = new InMemoryEventStore(Executors.newCachedThreadPool())) {
            eventStore.open();

            final VendorRepository repo = new VendorRepository(eventStore);

            final VendorId vendorId = new VendorId();
            final VendorKey vendorKey = new VendorKey("V00001");
            final VendorName vendorName = new VendorName("Hazards International Inc.");
            Vendor vendor = new Vendor(vendorId, vendorKey, vendorName, key -> {
                // Do nothing
            });
            repo.update(vendor);

            // TEST
            vendor.addPerson(new PersonName("Peter Parker"));
            repo.update(vendor);

            // VERIFY
            final AggregateStreamId streamId = new AggregateStreamId(VendorId.TYPE, "vendorId", vendorId);
            final StreamEventsSlice slice = eventStore.readEventsForward(streamId, 0, 100);
            Assertions.assertThat(slice.getEvents()).hasSize(2);
            vendor = repo.read(vendorId, vendor.getVersion());
            Assertions.assertThat(vendor.getVersion()).isEqualTo(1);
            vendor = repo.read(vendorId);
            Assertions.assertThat(vendor.getVersion()).isEqualTo(1);

        }

    }

    @Test
    public void testDeleteAggregate() throws Exception {

        // PREPARE
        try (final EventStore eventStore = new InMemoryEventStore(Executors.newCachedThreadPool())) {
            eventStore.open();

            final VendorRepository repo = new VendorRepository(eventStore);

            final VendorId vendorId = new VendorId();
            final VendorKey vendorKey = new VendorKey("V00001");
            final VendorName vendorName = new VendorName("Hazards International Inc.");
            Vendor vendor = new Vendor(vendorId, vendorKey, vendorName, key -> {
                // Do nothing
            });
            repo.update(vendor);

            // TEST
            repo.delete(vendorId, vendor.getVersion());

            // VERIFY
            final AggregateStreamId streamId = new AggregateStreamId(VendorId.TYPE, "vendorId", vendorId);
            Assertions.assertThat(eventStore.streamExists(streamId)).isFalse();
            try {
                repo.read(vendorId);
                fail();
            } catch (final AggregateNotFoundException ex) {
                // OK
            }
        }

    }

    @Test
    public void testConflictsResolved() throws Exception {

        // PREPARE
        try (final EventStore eventStore = new InMemoryEventStore(Executors.newCachedThreadPool())) {
            eventStore.open();

            final VendorRepository repo = new VendorRepository(eventStore);

            // Create version 0 of the vendor
            final VendorId vendorId = new VendorId();
            final VendorKey vendorKey = new VendorKey("V00001");
            final VendorName vendorName = new VendorName("Hazards International Inc.");
            final Vendor vendor = new Vendor(vendorId, vendorKey, vendorName, key -> {
                // Do nothing
            });
            repo.update(vendor); // VERSION 0

            // TEST

            // The first user adds a person with a typo in the name
            Vendor vendorUser1 = repo.read(vendorId, 0);
            vendorUser1.addPerson(new PersonName("Peter Parrker"));
            final PersonCreatedEvent pce = (PersonCreatedEvent) vendorUser1.getUncommittedChanges().get(0);
            repo.update(vendorUser1); // VERSION 1
            assertThat(repo.read(vendorId).getVersion()).isEqualTo(1);

            // The second user loads the data and realizes the typo
            final Vendor vendorUser2 = repo.read(vendorId, 1);

            // The first user continues adding more persons
            vendorUser1 = repo.read(vendorId, 1);
            vendorUser1.addPerson(new PersonName("Mary Jane Watson"));
            vendorUser1.addPerson(new PersonName("Harry Osborn"));
            repo.update(vendorUser1); // VERSION 3
            assertThat(repo.read(vendorId).getVersion()).isEqualTo(3);

            // The second user saves
            vendorUser2.changePersonName(pce.getPersonId(), new PersonName("Peter Parker"));
            repo.update(vendorUser2); // VERSION 4
            assertThat(repo.read(vendorId).getVersion()).isEqualTo(4);

            // VERIFY

        }

    }

}
