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

import static org.fest.assertions.Assertions.assertThat;

import java.util.concurrent.Executors;

import org.fuin.ddd4j.test.DuplicateVendorKeyException;
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

        final VendorRepository repo = new VendorRepository(eventStore);

        final VendorId vendorId = new VendorId();
        final VendorKey vendorKey = new VendorKey("V00001");
        final VendorName vendorName = new VendorName("Hazards International Inc.");
        final Vendor vendor = new Vendor(vendorId, vendorKey, vendorName, new Vendor.ConstructorService() {
            @Override
            public void addVendorKey(VendorKey key) throws DuplicateVendorKeyException {
                // Do nothing
            }
        });

        // TEST
        repo.update(vendor);

        // VERIFY
        final AggregateStreamId streamId = new AggregateStreamId(VendorId.ENTITY_TYPE, "vendorId", vendorId);
        final StreamEventsSlice slice = eventStore.readEventsForward(streamId, 0, 1);
        assertThat(slice.getEvents()).hasSize(1);

    }

}
// CHECKSTYLE:ON
