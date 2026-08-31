# Release Notes

## 0.8.0
- Added [UuidEntityId](core/src/main/java/org/fuin/ddd4j/core/UuidEntityId.java), the counterpart of [IntegerEntityId](core/src/main/java/org/fuin/ddd4j/core/IntegerEntityId.java) for a child entity identified by a UUID instead of a number sequential within its aggregate
- Fixed issue #10 JandexEntityIdfactory does not find "HasPublicStaticIsValidMethods" annotations
- Added new interface [IEventStoreRepository](esc/src/main/java/org/fuin/ddd4j/esc/IEventStoreRepository.java)
- Added new [JPA module](jpa)
- [Repository](core/src/main/java/org/fuin/ddd4j/core/Repository.java) now supports a [TenantContext](core/src/main/java/org/fuin/ddd4j/core/TenantContext.java)
  (also see [WritableTenantContext](core/src/main/java/org/fuin/ddd4j/core/WritableTenantContext.java)) that contains the current [TenantId](core/src/main/java/org/fuin/ddd4j/core/TenantId.java)
- **Incompatible** Added [TenantId](core/src/main/java/org/fuin/ddd4j/core/TenantId.java) parameter to [AggregateCache](core/src/main/java/org/fuin/ddd4j/core/AggregateCache.java) interface methods.
- Added new BOM
- Removed "codegen" module that was an experimental annotation based code generator
- Added [JSpecify](https://jspecify.dev/) and [NullAway](https://github.com/uber/nullaway)
- Use encryption functionality from [event-store-commons](https://github.com/fuinorg/event-store-commons)
- Use @JsonIgnoreProperties(ignoreUnknown = true) on Jackson to be aligned with JSON-B/JAXB which already tolerate unknowns
- Added a correlation/causation auto-propagation mechanism via [MessageContext](core/src/main/java/org/fuin/ddd4j/core/MessageContext.java) / [WritableMessageContext](core/src/main/java/org/fuin/ddd4j/core/WritableMessageContext.java) / [ThreadLocalMessageContext](core/src/main/java/org/fuin/ddd4j/core/ThreadLocalMessageContext.java)
- Added a [ReturnFromExileEvent](core/src/main/java/org/fuin/ddd4j/core/ReturnFromExileEvent.java) category marker: the counterpart of [ExileEvent](core/src/main/java/org/fuin/ddd4j/core/ExileEvent.java), for the event that recalls a soft-deleted entity. A view dropping a row on exile needs it to put the row back, and nothing returns from an [ExodusEvent](core/src/main/java/org/fuin/ddd4j/core/ExodusEvent.java), so the two are not symmetric.
- Documented the [crypto-shredding](README.md#crypto-shredding) pattern (encrypt personal data per subject via [RequiresPartialEncryption](core/src/main/java/org/fuin/ddd4j/core/RequiresPartialEncryption.java) / [RequiresPartialDecryption](core/src/main/java/org/fuin/ddd4j/core/RequiresPartialDecryption.java), forget the key, redact on read) and added a [RemovedPrivateData](core/src/main/java/org/fuin/ddd4j/core/RemovedPrivateData.java) marker that signals downstream consumers to purge a subject's derived data

## 0.7.0
- Added [Jackson](jackson) module

## 0.6.0
- Dependency updates
- **Incompatible** Previously there was only one artifact. Now there are multiple modules.
  The existing classes were moved to one of these modules to allow different serialization libraries
  like JSON-B, JAX-B and Jackson.
- New (experimental) annotation processing based code generator
- New ArchUnit rules in [Ddd4JConditions](junit/src/main/java/org/fuin/ddd4j/junit/Ddd4JConditions.java) that
  can be used in your application code.