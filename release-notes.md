# Release Notes

## 0.8.0
- Fixed issue #10 JandexEntityIdfactory does not find "HasPublicStaticIsValidMethods" annotations
- Code generation
  - Fixed issue #11: Generated method "isValid" of Integer EntityId failed to validate correct values 
  - New [annotation](codegen/api/src/main/java/org/fuin/ddd4j/codegen/api/CommandVO.java) and code generation template for commands
  - Fixed bug: Code was not generated in the package configured in the annotation.
  - Added default value in generated command and event builders for eventId and timestamp
  - Allow nullable fields for generated commands and events
  - Added variable replacement for event and command message
  - Fixed issue #9: Codegen template "StringVO.java" has JSON-B import even if JSON-B is not enabled
- Added new interface [IEventStoreRepository](esc/src/main/java/org/fuin/ddd4j/esc/IEventStoreRepository.java)

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