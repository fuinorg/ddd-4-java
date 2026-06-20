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

import com.google.gson.JsonObject;
import org.fuin.ddd4j.core.DomainEvent;
import org.fuin.esc.api.CommonEvent;
import org.fuin.esc.api.EventStore;
import org.fuin.esc.api.StreamEventsSlice;
import org.fuin.esc.mem.InMemoryEventStore;
import org.fuin.objects4j.crypto.EncryptedData;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.Executors;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test that verifies {@link EventStoreRepository} encrypts events implementing
 * {@code RequiresPartialEncryption} before they are appended and decrypts events implementing
 * {@code RequiresPartialDecryption} after they are read.
 */
public class EventStoreRepositoryEncryptionTest {

    @Test
    public void testPartialEncryptionRoundTrip() throws Exception {

        // PREPARE
        try (final EventStore eventStore = new InMemoryEventStore(Executors.newCachedThreadPool()).open()) {
            final MyCustomerRepository repo = new MyCustomerRepository(eventStore,
                    new GsonObjectSerDeserializer(), new FakeEncryptedDataService());

            final MyCustomerId customerId = new MyCustomerId();
            final MyCustomer customer = new MyCustomer(customerId, "John Doe");

            // TEST - append: the event must be encrypted before it reaches the store
            repo.update(customer);

            // VERIFY at rest: the stored event is the encrypted variant. Only the customer id and the random ciphertext
            // are non-deterministic (matched by placeholders); the personal name is gone from the plain text.
            final AggregateStreamId streamId = new AggregateStreamId(MyCustomerId.TYPE, "customerId", customerId);
            final StreamEventsSlice slice = eventStore.readEventsForward(streamId, 0, 100);
            assertThat(slice.getEvents()).hasSize(1);
            final CommonEvent stored = slice.getEvents().get(0);
            assertThat(stored.getDataType().asBaseType()).isEqualTo("MyCustomerCreatedEventEncrypted");
            assertThatJson(toJson(stored.getData())).isEqualTo("""
                    {
                        "event-type": "MyCustomerCreatedEventEncrypted",
                        "customer-id": "${json-unit.any-string}",
                        "encrypted-name": {
                            "key-id": "${json-unit.any-string}",
                            "key-version": "1",
                            "data-type": "customer-name",
                            "content-type": "application/json; charset=utf-8",
                            "encrypted-data": "${json-unit.any-string}"
                        }
                    }
                    """);

            // VERIFY read events: the encrypted variant is replaced by the decrypted original (name in plain text again)
            final List<DomainEvent<?>> events = repo.readEvents(customerId, 0);
            assertThat(events).hasSize(1);
            assertThatJson(toJson(events.get(0))).isEqualTo("""
                    {
                        "event-type": "MyCustomerCreatedEvent",
                        "customer-id": "${json-unit.any-string}",
                        "name": "John Doe"
                    }
                    """);

            // VERIFY read aggregate: state is reconstructed from the decrypted event
            final MyCustomer loaded = repo.read(customerId);
            assertThat(loaded.getName()).isEqualTo("John Doe");
        }

    }

    @Test
    public void testDecryptionFailureRedactsName() throws Exception {

        // PREPARE
        try (final EventStore eventStore = new InMemoryEventStore(Executors.newCachedThreadPool()).open()) {
            final FakeEncryptedDataService service = new FakeEncryptedDataService();
            final MyCustomerRepository repo = new MyCustomerRepository(eventStore, new GsonObjectSerDeserializer(), service);

            final MyCustomerId customerId = new MyCustomerId();
            repo.update(new MyCustomer(customerId, "John Doe"));

            // TEST - crypto-shredding: destroy the customer's key so the name can no longer be decrypted
            service.destroyKey(customerId.asString());

            // VERIFY the name is redacted instead of the read failing with a decryption exception
            final MyCustomer loaded = repo.read(customerId);
            assertThat(loaded.getName()).isEqualTo("***");
        }

    }

    /**
     * Renders a customer event to JSON: the plain event shows the name, the encrypted event shows the ciphertext envelope.
     *
     * @param event Plain or encrypted customer event.
     *
     * @return JSON representation.
     */
    private static String toJson(final Object event) {
        final JsonObject json = new JsonObject();
        if (event instanceof MyCustomerCreatedEventEncrypted enc) {
            json.addProperty("event-type", enc.getEventType().asBaseType());
            json.addProperty("customer-id", enc.getCustomerId().asString());
            final EncryptedData data = enc.getEncryptedName();
            final JsonObject envelope = new JsonObject();
            envelope.addProperty("key-id", data.getKeyId());
            envelope.addProperty("key-version", data.getKeyVersion());
            envelope.addProperty("data-type", data.getDataType());
            envelope.addProperty("content-type", data.getContentType());
            envelope.addProperty("encrypted-data", new String(data.getEncryptedData(), StandardCharsets.UTF_8));
            json.add("encrypted-name", envelope);
        } else if (event instanceof MyCustomerCreatedEvent evt) {
            json.addProperty("event-type", evt.getEventType().asBaseType());
            json.addProperty("customer-id", evt.getCustomerId().asString());
            json.addProperty("name", evt.getName());
        } else {
            throw new IllegalArgumentException("Unexpected event type: " + event);
        }
        return json.toString();
    }

}
