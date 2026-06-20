# ddd-4-java-esc
Event store based [Repository](src/main/java/org/fuin/ddd4j/esc/IEventStoreRepository.java) that persists the domain events of an aggregate to an [event-store-commons](https://github.com/fuinorg/event-store-commons) `EventStore`.
The abstract [EventStoreRepository](src/main/java/org/fuin/ddd4j/esc/EventStoreRepository.java) loads an aggregate by replaying its events and appends new uncommitted events on `update`.

The implementation supports partial encryption, which means only some of the fields are encrypted.
Encryption of the whole event is already supported by the [event-store-commons](https://github.com/fuinorg/event-store-commons/)
"EncryptingEventStore". Make sure you configure either partial or full encryption for an event; configuring both makes no sense.

## Partial field encryption

Some events carry personal data that must be encrypted at rest (e.g. for GDPR). Such events implement the
[RequiresPartialEncryption](../core/src/main/java/org/fuin/ddd4j/core/RequiresPartialEncryption.java) /
[RequiresPartialDecryption](../core/src/main/java/org/fuin/ddd4j/core/RequiresPartialDecryption.java) contracts from the
[Core](../core) module: they know how to turn themselves into an encrypted variant and back.

When the repository is created with an `ObjectSerDeserializer` and an
[EncryptedDataService](https://github.com/fuinorg/objects4j/tree/master/crypto) (from objects4j), the
`EventStoreRepository` transparently:

- **on append** – replaces every event implementing `RequiresPartialEncryption` with the encrypted variant
  it returns, *before* the event reaches the underlying event store, and
- **on read** – replaces every event implementing `RequiresPartialDecryption` with the decrypted variant
  it returns, *after* it was read from the event store and before it is applied to the aggregate.

```java
final ObjectSerDeserializer serDeserializer = ...;   // e.g. a JSON based (de)serializer
final EncryptedDataService encryptionService = ...;  // your objects4j-crypto implementation

final MyCustomerRepository repo = new MyCustomerRepository(eventStore, serDeserializer, encryptionService);
repo.update(customer);                 // events are encrypted before they are appended
final MyCustomer loaded = repo.read(customerId); // events are decrypted again on read
```

The no-arg-service constructor (`new MyCustomerRepository(eventStore)`) keeps working for aggregates without
encrypted events; encountering an event that requires encryption without a configured service fails fast with
an `IllegalStateException`.

### Example: plain vs. encrypted at rest

Given a `MyCustomerCreatedEvent` whose `name` is personal data, the event looks like this as the aggregate
emits it and as it is transparently restored on read:

```json
{
    "event-type": "MyCustomerCreatedEvent",
    "customer-id": "5f8d1c3a-9e2b-4a7c-8d6e-1f0a2b3c4d5e",
    "name": "John Doe"
}
```

The same event as the underlying event store holds it at rest: it has been replaced by the
`MyCustomerCreatedEventEncrypted` variant, and the `name` is gone from the plain text – only the ciphertext envelope
remains:

```json
{
    "event-type": "MyCustomerCreatedEventEncrypted",
    "customer-id": "5f8d1c3a-9e2b-4a7c-8d6e-1f0a2b3c4d5e",
    "name": {
        "key-id": "5f8d1c3a-9e2b-4a7c-8d6e-1f0a2b3c4d5e",
        "key-version": "1",
        "data-type": "customer-name",
        "content-type": "application/octet-stream; encoding=UTF-8",
        "encrypted-data": "vault:v1:K7m9Qe2pX...truncated...g8Zr0A=="
    }
}
```

With the [OpenBao](https://openbao.org/) Transit backend (the [objects4j-openbao](https://github.com/fuinorg/objects4j/tree/master/openbao) `EncryptedDataService`)
the `encrypted-data` carries OpenBao's `vault:v<keyVersion>:` envelope and is non-deterministic, so it differs on every run.
The `key-id` is derived from the aggregate (one key per customer, so a single customer's data can be crypto-shredded by
destroying the key), and the original `data-type` / `content-type` are kept alongside the ciphertext so the value can be
deserialized again after it has been decrypted, even after the key has been rotated.

Once the key has been destroyed, the data can no longer be decrypted. Rather than failing the read, the
`MyCustomerCreatedEventEncrypted` then returns a redacted name (`"***"`) so a customer whose personal data was
crypto-shredded can still be loaded.

A complete runnable example is the
[EventStoreRepositoryEncryptionTest](src/test/java/org/fuin/ddd4j/esc/EventStoreRepositoryEncryptionTest.java).
