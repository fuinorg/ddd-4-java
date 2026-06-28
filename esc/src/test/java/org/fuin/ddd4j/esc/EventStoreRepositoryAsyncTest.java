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

import org.fuin.ddd4j.core.AggregateNotFoundException;
import org.fuin.ddd4j.core.DomainEvent;
import org.fuin.ddd4j.jsonbtestmodel.PersonCreatedEvent;
import org.fuin.ddd4j.jsonbtestmodel.PersonName;
import org.fuin.ddd4j.jsonbtestmodel.Vendor;
import org.fuin.ddd4j.jsonbtestmodel.VendorId;
import org.fuin.ddd4j.jsonbtestmodel.VendorKey;
import org.fuin.ddd4j.jsonbtestmodel.VendorName;
import org.fuin.esc.api.StreamEventsSlice;
import org.fuin.esc.mem.InMemoryEventStoreAsync;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test for {@link EventStoreRepositoryAsync} using the asynchronous in-memory event store.
 */
public class EventStoreRepositoryAsyncTest {

    private InMemoryEventStoreAsync eventStore;

    private VendorRepositoryAsync repo;

    @BeforeEach
    public void setup() {
        eventStore = new InMemoryEventStoreAsync(Executors.newCachedThreadPool());
        eventStore.open();
        repo = new VendorRepositoryAsync(eventStore);
    }

    @AfterEach
    public void teardown() {
        eventStore.close();
    }

    private static Vendor newVendor(final VendorId vendorId) throws Exception {
        return new Vendor(vendorId, new VendorKey("V00001"), new VendorName("Hazards International Inc."), key -> {
            // Do nothing
        });
    }

    @Test
    public void testCreateAggregate() throws Exception {

        // PREPARE
        final VendorId vendorId = new VendorId();
        final Vendor vendor = newVendor(vendorId);

        // TEST
        repo.update(vendor).get();

        // VERIFY
        final AggregateStreamId streamId = new AggregateStreamId(VendorId.TYPE, "vendorId", vendorId);
        final StreamEventsSlice slice = eventStore.readEventsForward(streamId, 0, 100).get();
        assertThat(slice.getEvents()).hasSize(1);
        assertThat(repo.read(vendorId, vendor.getVersion()).get().getVersion()).isEqualTo(0);
        assertThat(repo.read(vendorId).get().getVersion()).isEqualTo(0);
    }

    @Test
    public void testUpdateAggregate() throws Exception {

        // PREPARE
        final VendorId vendorId = new VendorId();
        final Vendor vendor = newVendor(vendorId);
        repo.update(vendor).get();

        // TEST
        vendor.addPerson(new PersonName("Peter Parker"));
        repo.update(vendor).get();

        // VERIFY
        final AggregateStreamId streamId = new AggregateStreamId(VendorId.TYPE, "vendorId", vendorId);
        final StreamEventsSlice slice = eventStore.readEventsForward(streamId, 0, 100).get();
        assertThat(slice.getEvents()).hasSize(2);
        assertThat(repo.read(vendorId, vendor.getVersion()).get().getVersion()).isEqualTo(1);
        assertThat(repo.read(vendorId).get().getVersion()).isEqualTo(1);
    }

    @Test
    public void testDeleteAggregate() throws Exception {

        // PREPARE
        final VendorId vendorId = new VendorId();
        final Vendor vendor = newVendor(vendorId);
        repo.update(vendor).get();

        // TEST
        repo.delete(vendorId, vendor.getVersion()).get();

        // VERIFY
        final AggregateStreamId streamId = new AggregateStreamId(VendorId.TYPE, "vendorId", vendorId);
        assertThat(eventStore.streamExists(streamId).get()).isFalse();
        assertThatThrownBy(() -> repo.read(vendorId).join()).hasCauseInstanceOf(AggregateNotFoundException.class);
    }

    @Test
    public void testReadEvents() throws Exception {

        // PREPARE
        final VendorId vendorId = new VendorId();
        final Vendor vendor = newVendor(vendorId);
        vendor.addPerson(new PersonName("Peter Parker"));
        repo.update(vendor).get();

        // TEST
        final List<DomainEvent<?>> events = repo.readEvents(vendorId, 0).get();

        // VERIFY
        assertThat(events).hasSize(2);
        assertThat(events.get(0)).isInstanceOf(org.fuin.ddd4j.jsonbtestmodel.VendorCreatedEvent.class);
        assertThat(events.get(1)).isInstanceOf(PersonCreatedEvent.class);

        // Reading a non-existing aggregate fails the future
        assertThatThrownBy(() -> repo.readEvents(new VendorId(), 0).join())
                .hasCauseInstanceOf(AggregateNotFoundException.class);
    }

    @Test
    public void testConflictsResolved() throws Exception {

        // PREPARE - Create version 0 of the vendor
        final VendorId vendorId = new VendorId();
        final Vendor vendor = newVendor(vendorId);
        repo.update(vendor).get(); // VERSION 0

        // TEST

        // The first user adds a person with a typo in the name
        Vendor vendorUser1 = repo.read(vendorId, 0).get();
        vendorUser1.addPerson(new PersonName("Peter Parrker"));
        final PersonCreatedEvent pce = (PersonCreatedEvent) vendorUser1.getUncommittedChanges().get(0);
        repo.update(vendorUser1).get(); // VERSION 1
        assertThat(repo.read(vendorId).get().getVersion()).isEqualTo(1);

        // The second user loads the data and realizes the typo
        final Vendor vendorUser2 = repo.read(vendorId, 1).get();

        // The first user continues adding more persons
        vendorUser1 = repo.read(vendorId, 1).get();
        vendorUser1.addPerson(new PersonName("Mary Jane Watson"));
        vendorUser1.addPerson(new PersonName("Harry Osborn"));
        repo.update(vendorUser1).get(); // VERSION 3
        assertThat(repo.read(vendorId).get().getVersion()).isEqualTo(3);

        // The second user saves - conflict is resolved by VendorRepositoryAsync#conflictsResolved
        vendorUser2.changePersonName(pce.getPersonId(), new PersonName("Peter Parker"));
        repo.update(vendorUser2).get(); // VERSION 4
        assertThat(repo.read(vendorId).get().getVersion()).isEqualTo(4);
    }

}
