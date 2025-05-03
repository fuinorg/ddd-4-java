# Release Notes

## 0.7.1
- Fixed issue #9: Codegen template "StringVO.java" has JSON-B import even if JSON-B is not enabled
- New [annotation](codegen/api/src/main/java/org/fuin/ddd4j/codegen/api/CommandVO.java) and code generation template for commands
- Added missing annotation "@HasPublicStaticIsValidMethod" to generated entity and aggregate identifiers

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