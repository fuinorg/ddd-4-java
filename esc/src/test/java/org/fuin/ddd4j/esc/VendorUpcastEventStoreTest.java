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

import org.fuin.ddd4j.jsonbtestmodel.Vendor;
import org.fuin.ddd4j.jsonbtestmodel.VendorCreatedEvent;
import org.fuin.ddd4j.jsonbtestmodel.VendorId;
import org.fuin.esc.api.ConverterRegistry;
import org.fuin.esc.api.EventId;
import org.fuin.esc.api.ExpectedVersion;
import org.fuin.esc.api.SerializedDataType;
import org.fuin.esc.api.SimpleCommonEvent;
import org.fuin.esc.api.SimpleSerializerDeserializerRegistry;
import org.fuin.esc.api.StreamId;
import org.fuin.esc.api.TypeName;
import org.fuin.esc.jaxb.XmlDeSerializer;
import org.fuin.esc.jpa.JpaEventStore;
import org.fuin.esc.jpa.JpaIdStreamFactory;
import org.fuin.esc.jpa.JpaStream;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * DDD-1 keystone integration test: a {@code …v1} event stream folds into a {@code …v2} aggregate on replay
 * <b>through the ddd {@link EventStoreRepository}</b>. A version-1 event is stored via a real serializing
 * {@link JpaEventStore} (in-memory HSQLDB, no Docker); when the store is built with a {@link ConverterRegistry}
 * (folded from a {@link ConverterRegistration}), the up-cast to the current
 * {@link VendorCreatedEvent} (v2) happens inside the store's deserializer, so aggregate replay
 * ({@code loadFromHistory} + {@code @ApplyEvent}) transparently rebuilds the {@link Vendor} from the newer
 * representation. The repository itself stays version-agnostic.
 */
public final class VendorUpcastEventStoreTest extends AbstractPersistenceTest {

    private static final SerializedDataType SER_TYPE = VendorCreatedEvent.SER_TYPE;

    private static final String KEY = "V00001";

    private static final String NAME = "Peter Parker Inc.";

    @Test
    public void v1StreamFoldsIntoV2Aggregate() throws Exception {

        // PREPARE: a v1 (de)serializer for "VendorCreatedEvent" and a v1 -> v2 up-caster wired via the helper.
        final XmlDeSerializer v1 = XmlDeSerializer.builder().add(VendorCreatedEventV1.class).version("1").build();
        final SimpleSerializerDeserializerRegistry registry = new SimpleSerializerDeserializerRegistry.Builder(v1.getMimeType())
                .add(SER_TYPE, v1, v1.getMimeType())
                .build();
        final ConverterRegistry converters = ConverterRegistration.toRegistry(
                List.of(new ConverterRegistration(SER_TYPE, "1", "2", new VendorCreatedEventV1ToV2Converter())));

        final VendorId vendorId = new VendorId();
        final StreamId streamId = new AggregateStreamId(VendorId.TYPE, "vendorId", vendorId);

        beginTransaction();
        try (JpaEventStore es = new JpaEventStore(getEm(), vendorStreamFactory(), registry, registry, converters)) {
            es.open();

            // TEST: store a raw v1 event directly, then read the aggregate back through the repository.
            es.appendToStream(streamId, ExpectedVersion.NO_OR_EMPTY_STREAM.getNo(),
                    new SimpleCommonEvent(new EventId(), new TypeName(VendorCreatedEvent.TYPE.asBaseType()),
                            new VendorCreatedEventV1(vendorId.asString(), KEY, NAME), null));

            final Vendor vendor = new VendorRepository(es).read(vendorId);

            // VERIFY: the aggregate was rebuilt from the up-cast v2 event.
            assertThat(vendor).isNotNull();
            assertThat(vendor.getId()).isEqualTo(vendorId);
            assertThat(vendor.getRef().getId()).isEqualTo(vendorId);
            assertThat(vendor.getRef().getKey().asBaseType()).isEqualTo(KEY);
            assertThat(vendor.getRef().getName().asBaseType()).isEqualTo(NAME);
        } finally {
            rollbackTransaction();
        }
    }

    @Test
    public void withoutConvertersTheV1CannotBeReplayed() {

        // PREPARE: same v1 (de)serializer, but a store built WITHOUT converters (the pre-DDD-1 default).
        final XmlDeSerializer v1 = XmlDeSerializer.builder().add(VendorCreatedEventV1.class).version("1").build();
        final SimpleSerializerDeserializerRegistry registry = new SimpleSerializerDeserializerRegistry.Builder(v1.getMimeType())
                .add(SER_TYPE, v1, v1.getMimeType())
                .build();

        final VendorId vendorId = new VendorId();
        final StreamId streamId = new AggregateStreamId(VendorId.TYPE, "vendorId", vendorId);

        beginTransaction();
        try (JpaEventStore es = new JpaEventStore(getEm(), vendorStreamFactory(), registry, registry)) {
            es.open();

            es.appendToStream(streamId, ExpectedVersion.NO_OR_EMPTY_STREAM.getNo(),
                    new SimpleCommonEvent(new EventId(), new TypeName(VendorCreatedEvent.TYPE.asBaseType()),
                            new VendorCreatedEventV1(vendorId.asString(), KEY, NAME), null));

            // TEST & VERIFY: without up-cast wiring the stored v1 (not a DomainEvent) reaches the repository as-is,
            // so replay cannot apply it - which is exactly why a versioned stream needs the converter chain.
            final VendorRepository repo = new VendorRepository(es);
            assertThatThrownBy(() -> repo.read(vendorId)).isInstanceOf(ClassCastException.class);
        } finally {
            rollbackTransaction();
        }
    }

    private static JpaIdStreamFactory vendorStreamFactory() {
        return new JpaIdStreamFactory() {
            @Override
            public JpaStream createStream(final StreamId streamId) {
                return new VendorStream(streamId.getSingleParamValue());
            }

            @Override
            public boolean containsType(final StreamId streamId) {
                return true;
            }
        };
    }

}
