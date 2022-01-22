# ddd-4-java

# Domain Driven Design for Java
Base classes for Domain Driven Design (DDD) with Java.

[![Build Status](https://jenkins.fuin.org/job/ddd-4-java/badge/icon)](https://jenkins.fuin.org/job/ddd-4-java/)
[![Coverage Status](https://sonarcloud.io/api/project_badges/measure?project=org.fuin%3Addd-4-java&metric=coverage)](https://sonarcloud.io/dashboard?id=org.fuin%3Addd-4-java)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.fuin/ddd-4-java/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.fuin/ddd-4-java/)
[![LGPLv3 License](http://img.shields.io/badge/license-LGPLv3-blue.svg)](https://www.gnu.org/licenses/lgpl.html)
[![Java Development Kit 11](https://img.shields.io/badge/JDK-11-green.svg)](https://openjdk.java.net/projects/jdk/11/)

## Versions
- 0.5.x (or later) = **Java 11** with new **jakarta** namespace
- 0.3.x/0.4.x = **Java 11** before namespace change from 'javax' to 'jakarta'
- 0.2.1 = **Java 8**


## Documentation
You can find the [documentation](https://app.gitbook.com/@fuinorg/s/ddd-4-java/) of the project at [gitbook](https://app.gitbook.com/@fuinorg/spaces/) (*Work in progress* - Just started!).

## Presentation
http://de.slideshare.net/michael-schnell/ddd-4java/ (Rather old - The git book above is a bit more up-to-date)

## Example
See [ddd-cqrs-4-java-example](https://github.com/fuinorg/ddd-cqrs-4-java-example) for example microservices using the classes of this library.

## Additional information

### EncryptedData
For example the [GDPR](https://gdpr.eu/) data protection rules requires to "delete" personal data on request. This can be done by encrypting the personal data in the event with a secret key. When the user is deleted, you can simply throw away that key and it is no longer possible to access the personal data in the stored events.

The [src/main/java/org/fuin/ddd4j/ddd/EncryptedData.java](EncryptedData) class provides a basic structure with the relevant information to encrypt/decrypt such personal data in events.

Here is an example of an event with "personal-data" of type [src/main/java/org/fuin/ddd4j/ddd/EncryptedData.java](EncryptedData.java):

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
The [src/main/java/org/fuin/ddd4j/ddd/EncryptedDataService.java](EncryptedDataService) interface defines for encrypting/decrypting [src/main/java/org/fuin/ddd4j/ddd/EncryptedData.java](EncryptedData) and handling versioned secret keys.

There are two implementations that can be used for tests:

- [https://github.com/fuinorg/ddd-cqrs-unit/src/main/java/org/fuin/dddcqrsunit/InMemoryCryptoService.java](InMemoryCryptoService) for in-memory unit tests
- [https://github.com/fuinorg/ddd-cqrs-unit/src/main/java/org/fuin/dddcqrsunit/VaultCryptoService.java](VaultCryptoService) for the HashiCorp Vault [https://www.vaultproject.io/docs/secrets/transit](Transit Secrets Engine)


## Snapshots

Snapshots can be found on the [OSS Sonatype Snapshots Repository](http://oss.sonatype.org/content/repositories/snapshots/org/fuin "Snapshot Repository"). 

Add the following to your [.m2/settings.xml](http://maven.apache.org/ref/3.2.1/maven-settings/settings.html "Reference configuration") to enable snapshots in your Maven build:

```xml
<repository>
    <id>sonatype.oss.snapshots</id>
    <name>Sonatype OSS Snapshot Repository</name>
    <url>http://oss.sonatype.org/content/repositories/snapshots</url>
    <releases>
        <enabled>false</enabled>
    </releases>
    <snapshots>
        <enabled>true</enabled>
    </snapshots>
</repository>
```

