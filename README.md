# ddd-4-java

# Domain Driven Design for Java

Base classes for Domain Driven Design (DDD) with Java.

[![Java Maven Build](https://github.com/fuinorg/ddd-4-java/actions/workflows/maven.yml/badge.svg)](https://github.com/fuinorg/ddd-4-java/actions/workflows/maven.yml)
[![Coverage Status](https://sonarcloud.io/api/project_badges/measure?project=org.fuin.ddd4j%3Addd-4-java&metric=coverage)](https://sonarcloud.io/dashboard?id=org.fuin.ddd4j%3Addd-4-java)
[![Maven Central](https://img.shields.io/maven-central/v/org.fuin/ddd-4-java.svg)](https://central.sonatype.com/artifact/org.fuin/ddd-4-java)
[![LGPLv3 License](http://img.shields.io/badge/license-LGPLv3-blue.svg)](https://www.gnu.org/licenses/lgpl.html)
[![Java Development Kit 17](https://img.shields.io/badge/JDK-17-green.svg)](https://openjdk.java.net/projects/jdk/17/)

## Versions
- See [CHANGELOG.md](CHANGELOG.md)
- 0.5.x (or later) = **Java 17** with new **jakarta** namespace
- 0.3.x/0.4.x = **Java 11** before namespace change from 'javax' to 'jakarta'
- 0.2.1 = **Java 8**

## Maven BOM

A [Bill of Materials (BOM)](https://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html#bill-of-materials-bom-poms)
is provided to keep the versions of the ddd-4-java modules aligned. Import it in the `dependencyManagement` section of
your project and then declare the modules you need without specifying a version:

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.fuin.ddd4j</groupId>
            <artifactId>ddd-4-java-bom</artifactId>
            <version>0.8.0</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<dependencies>
    <dependency>
        <groupId>org.fuin.ddd4j</groupId>
        <artifactId>ddd-4-java-core</artifactId>
    </dependency>
    <!-- Other modules without a version tag -->
</dependencies>
```

The BOM manages the following modules:

- `ddd-4-java-core`
- `ddd-4-java-esc`
- `ddd-4-java-jaxb`
- `ddd-4-java-jsonb`
- `ddd-4-java-jackson`
- `ddd-4-java-jpa`
- `ddd-4-java-junit`

## Documentation

You can find the [documentation](https://app.gitbook.com/@fuinorg/s/ddd-4-java/) of the project
at [gitbook](https://app.gitbook.com/@fuinorg/spaces/) (*Work in progress* - Just started!).

## Presentation

http://de.slideshare.net/michael-schnell/ddd-4java/ (Rather old - The git book above is a bit more up-to-date)

## Example

See [ddd-cqrs-4-java-example](https://github.com/fuinorg/ddd-cqrs-4-java-example) for example microservices using the
classes of this library.

## Additional information

### EncryptedData

For example the [GDPR](https://gdpr.eu/) data protection rules requires to "delete" personal data on request. This can
be done by encrypting the personal data in the event with a secret key. When the user is deleted, you can simply throw
away that key, and it is no longer possible to access the personal data in the stored events.

The `EncryptedData` class (provided by the `org.fuin.objects4j:objects4j-crypto` dependency, package
`org.fuin.objects4j.crypto`) provides a basic structure with the relevant information to encrypt/decrypt such personal
data in events.

Here is an example of an event with "personal-data" of type `EncryptedData`:

```json
{
	"event-id": "518efad9-fb09-419e-acb6-50f1bc0c1e3e",
	"event-timestamp": "2022-01-22T09:04:18.811501046+01:00[Europe/Luxembourg]",
	"entity-id-path": "USER 5c09fc35-11e8-49d0-87ef-47c1d2738998",
	"personal-data": {
		"key-id": "secret/user/5c09fc35-11e8-49d0-87ef-47c1d2738998",
		"key-version": "1",
		"content-type": "application/json; encoding=UTF-8; version=1",
		"data-type": "PersonalData",
		"encrypted-data": "gK1UpxAwislfXCcB3yAPo83uxCPxdIJsf1x64lWckEi21oZiwIjHudEoeJge7KksfougPkHKl08/1ZW/iU7tqnVF8uv5a3Fh79lHPcHBkePhCOzoDnIh05IfVA2IrTQ6"
	}
}
```

### EncryptedDataService

The `EncryptedDataService` interface (also in `org.fuin.objects4j:objects4j-crypto`) defines the operations for
encrypting/decrypting `EncryptedData` and handling versioned secret keys. A production implementation over OpenBao /
HashiCorp Vault [Transit Secrets Engine](https://openbao.org/docs/secrets/transit/) is available as
`org.fuin.objects4j.openbao.BaoEncryptedDataService` (dependency `org.fuin.objects4j:objects4j-openbao`); an in-memory
fake is used in this project's own tests.

### Crypto-Shredding

*Crypto-shredding* is how this library implements GDPR-style "erase a subject's personal data" while **keeping** the
rest of the aggregate's (often legally required) history. The mechanism is already wired into the repository - you only
author the events:

1. **Encrypt per subject.** A domain event that carries personal data implements
   [RequiresPartialEncryption](core/src/main/java/org/fuin/ddd4j/core/RequiresPartialEncryption.java) and produces a
   separate encrypted variant implementing
   [RequiresPartialDecryption](core/src/main/java/org/fuin/ddd4j/core/RequiresPartialDecryption.java). Use a key that
   is unique per subject - by convention `keyId = aggregateId.asString()`. On `update(...)` the
   [EventStoreRepository](esc/src/main/java/org/fuin/ddd4j/esc/EventStoreRepository.java) encrypts the event before it
   is appended (construct the repository with an `ObjectSerDeserializer` + `EncryptedDataService`).
2. **Forget the key.** To erase a subject, destroy its key in the key store (e.g. delete it in OpenBao/Vault). The
   stored events can no longer be decrypted.
3. **Redact on read.** The encrypted variant's `decrypt(...)` should catch the key-loss exceptions and return a
   redacted plain event (e.g. name = `"***"`) instead of failing, so replay of the shredded subject keeps working -
   anonymized.
4. **Signal downstream.** Crypto-shredding only makes the *source* events unreadable; read models, search indexes and
   other consumers already persisted derived copies while the key existed. Deleting the aggregate stream does **not**
   help - a stream delete is not delivered to catch-up subscribers as an ordered event. Append an event implementing
   [RemovedPrivateData](core/src/main/java/org/fuin/ddd4j/core/RemovedPrivateData.java) (a payload-free tombstone
   carrying only the subject id); every consumer sees it in stream order and purges the subject's derived data:
   ```java
   if (event instanceof RemovedPrivateData removed) {
       purgeDerivedData(removed.getSubjectId());
   }
   ```

A worked end-to-end example (encrypt → forget key → redact → `RemovedPrivateData` signal) is in
[EventStoreRepositoryEncryptionTest](esc/src/test/java/org/fuin/ddd4j/esc/EventStoreRepositoryEncryptionTest.java).

**Alternatives / when not to use it.** If you must remove the *whole* aggregate (not just the PII), use the coarser
`Repository.delete(id, expectedVersion)` stream delete instead - but note it loses the non-personal history and does
not by itself notify projections. A **public/private stream split** (keep private data in a separate `…-private`
stream you can hard-delete, non-personal data in a `…-public` stream) is another option; ddd-4-java currently uses one
stream per aggregate, so that split is an application-level pattern rather than a built-in helper.

## Snapshots

Snapshots can be found on the [Central Portal Snapshots Repository](https://central.sonatype.com/repository/maven-snapshots/org/fuin "Snapshot Repository").

Add the following to your .m2/settings.xml to enable snapshots in your Maven build:

```xml
<repository>
    <id>central-portal-snapshots</id>
    <name>Central Portal Snapshots</name>
    <url>https://central.sonatype.com/repository/maven-snapshots/</url>
    <releases>
        <enabled>false</enabled>
    </releases>
    <snapshots>
        <enabled>true</enabled>
    </snapshots>
</repository>
```
