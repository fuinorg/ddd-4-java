# Release Notes

## 0.8.0
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