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
package org.fuin.ddd4j.esrepo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.util.concurrent.Executors;

import org.fuin.ddd4j.ddd.AggregateNotFoundException;
import org.fuin.ddd4j.test.DuplicateVendorKeyException;
import org.fuin.ddd4j.test.PersonName;
import org.fuin.ddd4j.test.Vendor;
import org.fuin.ddd4j.test.VendorId;
import org.fuin.ddd4j.test.VendorKey;
import org.fuin.ddd4j.test.VendorName;
import org.fuin.esc.api.EventStore;
import org.fuin.esc.api.StreamEventsSlice;
import org.fuin.esc.mem.InMemoryEventStore;
import org.junit.Test;

//CHECKSTYLE:OFF
public class EventStoreRespositoryTest {

    @Test
    public void testCreateAggregate() throws Exception {

        // PREPARE
        final EventStore eventStore = new InMemoryEventStore(Executors.newCachedThreadPool());
        eventStore.open();
        try {

            final VendorRepository repo = new VendorRepository(eventStore);        
    
            final VendorId vendorId = new VendorId();
            final VendorKey vendorKey = new VendorKey("V00001");
            final VendorName vendorName = new VendorName("Hazards International Inc.");
            Vendor vendor = new Vendor(vendorId, vendorKey, vendorName, new Vendor.ConstructorService() {
                @Override
                public void addVendorKey(VendorKey key) throws DuplicateVendorKeyException {
                    // Do nothing
                }
            });
    
            // TEST
            repo.update(vendor);
    
            // VERIFY
            final AggregateStreamId streamId = new AggregateStreamId(VendorId.ENTITY_TYPE, "vendorId", vendorId);
            final StreamEventsSlice slice = eventStore.readEventsForward(streamId, 0, 100);
            assertThat(slice.getEvents()).hasSize(1);
            vendor = repo.read(vendorId, vendor.getVersion());
            assertThat(vendor.getVersion()).isEqualTo(0);
            vendor = repo.read(vendorId);
            assertThat(vendor.getVersion()).isEqualTo(0);
            
        } finally {
            eventStore.close();
        }
        
    }

    @Test
    public void testUpdateAggregate() throws Exception {

        // PREPARE
        final EventStore eventStore = new InMemoryEventStore(Executors.newCachedThreadPool());
        eventStore.open();
        try {

            final VendorRepository repo = new VendorRepository(eventStore);        
    
            final VendorId vendorId = new VendorId();
            final VendorKey vendorKey = new VendorKey("V00001");
            final VendorName vendorName = new VendorName("Hazards International Inc.");
            Vendor vendor = new Vendor(vendorId, vendorKey, vendorName, new Vendor.ConstructorService() {
                @Override
                public void addVendorKey(VendorKey key) throws DuplicateVendorKeyException {
                    // Do nothing
                }
            });
            repo.update(vendor);

            // TEST            
            vendor.addPerson(new PersonName("Peter Parker"));
            repo.update(vendor);
            
            // VERIFY
            final AggregateStreamId streamId = new AggregateStreamId(VendorId.ENTITY_TYPE, "vendorId", vendorId);
            final StreamEventsSlice slice = eventStore.readEventsForward(streamId, 0, 100);
            assertThat(slice.getEvents()).hasSize(2);
            vendor = repo.read(vendorId, vendor.getVersion());
            assertThat(vendor.getVersion()).isEqualTo(1);
            vendor = repo.read(vendorId);
            assertThat(vendor.getVersion()).isEqualTo(1);
            
            
        } finally {
            eventStore.close();
        }
        
    }

    @Test
    public void testDeleteAggregate() throws Exception {

        // PREPARE
        final EventStore eventStore = new InMemoryEventStore(Executors.newCachedThreadPool());
        eventStore.open();
        try {

            final VendorRepository repo = new VendorRepository(eventStore);        
    
            final VendorId vendorId = new VendorId();
            final VendorKey vendorKey = new VendorKey("V00001");
            final VendorName vendorName = new VendorName("Hazards International Inc.");
            Vendor vendor = new Vendor(vendorId, vendorKey, vendorName, new Vendor.ConstructorService() {
                @Override
                public void addVendorKey(VendorKey key) throws DuplicateVendorKeyException {
                    // Do nothing
                }
            });
            repo.update(vendor);

            // TEST            
            repo.delete(vendorId, vendor.getVersion());
            
            // VERIFY
            final AggregateStreamId streamId = new AggregateStreamId(VendorId.ENTITY_TYPE, "vendorId", vendorId);
            assertThat(eventStore.streamExists(streamId)).isFalse();
            try {
                repo.read(vendorId);
                fail();
            } catch (final AggregateNotFoundException ex) {
                // OK
            }
        } finally {
            eventStore.close();
        }
        
    }
    
    
    @Test
    public void testConflictsResolved() throws Exception {

        // PREPARE
        final EventStore eventStore = new InMemoryEventStore(Executors.newCachedThreadPool());
        eventStore.open();
        try {

            final VendorRepository repo = new VendorRepository(eventStore);        
    
            // Create version 0 of the vendor
            final VendorId vendorId = new VendorId();
            final VendorKey vendorKey = new VendorKey("V00001");
            final VendorName vendorName = new VendorName("Hazards International Inc.");
            final Vendor vendor = new Vendor(vendorId, vendorKey, vendorName, new Vendor.ConstructorService() {
                @Override
                public void addVendorKey(VendorKey key) throws DuplicateVendorKeyException {
                    // Do nothing
                }
            });
            repo.update(vendor);

            // TEST
            
            // The first user adds a person and stores the result it in the repository
            final Vendor vendor1 = repo.read(vendorId, 0);
            vendor1.addPerson(new PersonName("Peter Parker"));
            vendor1.addPerson(new PersonName("Mary Jane Watson"));
            repo.update(vendor1);
            assertThat(repo.read(vendorId).getVersion()).isEqualTo(2);
            
            // The second user also adds a person and stores the result in the repository
            final Vendor vendor2 = repo.read(vendorId, 0);
            vendor2.addPerson(new PersonName("Harry Osborn"));
            repo.update(vendor2);
            
            // VERIFY
            
            
        } finally {
            eventStore.close();
        }
        
    }
    
}
// CHECKSTYLE:ON
