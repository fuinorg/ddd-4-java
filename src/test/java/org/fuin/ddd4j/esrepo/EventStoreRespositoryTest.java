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

import java.nio.charset.Charset;
import java.util.UUID;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.fuin.ddd4j.ddd.BasicEventMetaData;
import org.fuin.ddd4j.ddd.EntityId;
import org.fuin.ddd4j.ddd.EntityIdFactory;
import org.fuin.ddd4j.ddd.EntityIdPathConverter;
import org.fuin.ddd4j.test.DuplicateVendorKeyException;
import org.fuin.ddd4j.test.Vendor;
import org.fuin.ddd4j.test.VendorCreatedEvent;
import org.fuin.ddd4j.test.VendorId;
import org.fuin.ddd4j.test.VendorKey;
import org.fuin.ddd4j.test.VendorName;
import org.fuin.ddd4j.test.VendorStream;
import org.fuin.esc.api.EventStoreSync;
import org.fuin.esc.api.StreamEventsSlice;
import org.fuin.esc.api.StreamId;
import org.fuin.esc.jpa.JpaEventStore;
import org.fuin.esc.jpa.JpaIdStreamFactory;
import org.fuin.esc.jpa.JpaStream;
import org.fuin.esc.spi.DeserializerRegistry;
import org.fuin.esc.spi.SerializedDataType;
import org.fuin.esc.spi.SerializerRegistry;
import org.fuin.esc.spi.SimpleSerializerDeserializerRegistry;
import org.fuin.esc.spi.XmlDeSerializer;
import org.fuin.units4j.AbstractPersistenceTest;
import org.junit.Test;

//CHECKSTYLE:OFF
public class EventStoreRespositoryTest extends AbstractPersistenceTest {

    @Test
    public void testCreateAggregate() throws Exception {

        // PREPARE
        final SerializedDataType serMetaType = new SerializedDataType(
                BasicEventMetaData.TYPE);
        final SimpleSerializerDeserializerRegistry registry = new SimpleSerializerDeserializerRegistry();
        addTypes(registry);

        final EventStoreSync eventStore = createEventStore(registry, registry,
                serMetaType);

        final VendorRepository repo = new VendorRepository(eventStore);

        final VendorId vendorId = new VendorId();
        final VendorKey vendorKey = new VendorKey("V00001");
        final VendorName vendorName = new VendorName(
                "Hazards International Inc.");
        final Vendor vendor = new Vendor(vendorId, vendorKey, vendorName,
                new Vendor.ConstructorService() {
                    @Override
                    public void addVendorKey(VendorKey key)
                            throws DuplicateVendorKeyException {
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
        final StreamEventsSlice slice = eventStore.readEventsForward(streamId,
                1, 1);
        commitTransaction();
        assertThat(slice.getEvents()).hasSize(1);

    }

    private EntityIdFactory createEntityIdFactory() {
        final EntityIdFactory entityIdFactory = new EntityIdFactory() {
            @Override
            public EntityId createEntityId(String type, String id) {
                if (type.equals(VendorId.ENTITY_TYPE.asString())) {
                    return new VendorId(UUID.fromString(id));
                }
                throw new IllegalArgumentException("Unknown type: " + type);
            }

            @Override
            public boolean containsType(String type) {
                if (type.endsWith(VendorId.ENTITY_TYPE.asString())) {
                    return true;
                }
                return false;
            }
        };
        return entityIdFactory;
    }

    private void addTypes(final SimpleSerializerDeserializerRegistry registry) {

        final String contentType = "application/xml";

        final EntityIdFactory entityIdFactory = createEntityIdFactory();
        final XmlAdapter<?, ?>[] adapters = new XmlAdapter<?, ?>[] { new EntityIdPathConverter(
                entityIdFactory) };

        final XmlDeSerializer xmlDeSer = new XmlDeSerializer(
                Charset.forName("utf-8"), adapters, VendorCreatedEvent.class,
                BasicEventMetaData.class);

        final SerializedDataType vendorCreatedEventType = new SerializedDataType(
                VendorCreatedEvent.TYPE.asBaseType());
        registry.addSerializer(vendorCreatedEventType, xmlDeSer);
        registry.addDeserializer(vendorCreatedEventType, contentType, xmlDeSer);

        final SerializedDataType metaType = new SerializedDataType(
                BasicEventMetaData.TYPE);
        registry.addSerializer(metaType, xmlDeSer);
        registry.addDeserializer(metaType, contentType, xmlDeSer);

    }

    private JpaEventStore createEventStore(
            final SerializerRegistry serRegistry,
            final DeserializerRegistry desRegistry,
            final SerializedDataType serMetaType) {

        final JpaEventStore eventStore = new JpaEventStore(getEm(),
                new JpaIdStreamFactory() {
                    @Override
                    public JpaStream createStream(final StreamId streamId) {
                        final String vendorId = streamId.getSingleParamValue();
                        return new VendorStream(VendorId.valueOf(vendorId));
                    }

                    @Override
                    public boolean containsType(StreamId streamId) {
                        return true;
                    }
                }, serRegistry, desRegistry, serMetaType);
        return eventStore;
    }

}
// CHECKSTYLE:ON
