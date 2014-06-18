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
package org.fuin.ddd4j.esrepo;

import static org.fest.assertions.Assertions.assertThat;

import java.util.UUID;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.fuin.ddd4j.ddd.EntityId;
import org.fuin.ddd4j.ddd.EntityIdFactory;
import org.fuin.ddd4j.ddd.EntityIdPathConverter;
import org.fuin.ddd4j.ddd.SimpleSerializerDeserializerRegistry;
import org.fuin.ddd4j.ddd.XmlDeSerializer;
import org.fuin.ddd4j.eventstore.intf.StreamEventsSlice;
import org.fuin.ddd4j.eventstore.intf.StreamId;
import org.fuin.ddd4j.eventstore.jpa.IdStreamFactory;
import org.fuin.ddd4j.eventstore.jpa.JpaEventStore;
import org.fuin.ddd4j.eventstore.jpa.Stream;
import org.fuin.ddd4j.test.DuplicateVendorKeyException;
import org.fuin.ddd4j.test.Vendor;
import org.fuin.ddd4j.test.VendorCreatedEvent;
import org.fuin.ddd4j.test.VendorId;
import org.fuin.ddd4j.test.VendorKey;
import org.fuin.ddd4j.test.VendorName;
import org.fuin.ddd4j.test.VendorStream;
import org.fuin.units4j.AbstractPersistenceTest;
import org.junit.Test;

//CHECKSTYLE:OFF
public class EventStoreRespositoryTest extends AbstractPersistenceTest {

    @Test
    public void testCreateAggregate() throws Exception {

        // PREPARE
        final JpaEventStore eventStore = createEventStore();
        final EntityIdFactory entityIdFactory = createEntityIdFactory();
        final XmlAdapter<?, ?>[] adapters = new XmlAdapter<?, ?>[] { new EntityIdPathConverter(
                entityIdFactory) };
        final SimpleSerializerDeserializerRegistry registry = new SimpleSerializerDeserializerRegistry();
        registry.add(new XmlDeSerializer(VendorCreatedEvent.TYPE.asBaseType(),
                adapters, VendorCreatedEvent.class));
        final VendorRepository repo = new VendorRepository(eventStore,
                registry, registry);

        final VendorId vendorId = new VendorId();
        final VendorKey vendorKey = new VendorKey("V00001");
        final VendorName vendorName = new VendorName(
                "Hazards International Inc.");
        final Vendor vendor = new Vendor(vendorId, vendorKey, vendorName, new Vendor.ConstructorService() {
            @Override
            public void addVendorKey(VendorKey key) throws DuplicateVendorKeyException {
                // Do nothing
            }
        });

        // TEST
        beginTransaction();
        repo.update(vendor, null);
        commitTransaction();

        // VERIFY
        beginTransaction();
        final AggregateStreamId streamId = new AggregateStreamId(
                VendorId.ENTITY_TYPE, "vendorId", vendorId);
        final StreamEventsSlice slice = eventStore.readStreamEventsForward(
                streamId, 1, 1);
        commitTransaction();
        assertThat(slice.getEvents()).hasSize(1);

    }

    private EntityIdFactory createEntityIdFactory() {
        final EntityIdFactory entityIdFactory = new EntityIdFactory() {
            @Override
            public EntityId createEntityId(String type, String id) {
                if (type == VendorId.ENTITY_TYPE.asString()) {
                    return new VendorId(UUID.fromString(id));
                }
                throw new IllegalArgumentException("Unknown type: " + type);
            }

            @Override
            public boolean containsType(String type) {
                if (type == VendorId.ENTITY_TYPE.asString()) {
                    return true;
                }
                return false;
            }
        };
        return entityIdFactory;
    }

    private JpaEventStore createEventStore() {
        final JpaEventStore eventStore = new JpaEventStore(getEm(),
                new IdStreamFactory() {
                    @Override
                    public Stream createStream(final StreamId streamId) {
                        final String vendorId = streamId.getSingleParamValue();
                        return new VendorStream(VendorId.valueOf(vendorId));
                    }

                    @Override
                    public boolean containsType(StreamId streamId) {
                        return true;
                    }
                });
        return eventStore;
    }

}
// CHECKSTYLE:ON
