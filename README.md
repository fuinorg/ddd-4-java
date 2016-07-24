ddd-4-java
==========

Domain Driven Design for Java
-----------------------------

Base classes for Domain Driven Design (DDD) with Java

[![Build Status](https://fuin-org.ci.cloudbees.com/job/ddd-4-java/badge/icon)](https://fuin-org.ci.cloudbees.com/job/ddd-4-java/)
[![LGPLv3 License](http://img.shields.io/badge/license-LGPLv3-blue.svg)](https://www.gnu.org/licenses/lgpl.html)
[![Java Development Kit 1.8](https://img.shields.io/badge/JDK-1.8-green.svg)](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

<a href="https://fuin-org.ci.cloudbees.com/job/ddd-4-java"><img src="http://www.fuin.org/images/Button-Built-on-CB-1.png" width="213" height="72" border="0" alt="Built on CloudBees"/></a>

Documentation
-------------
You can find the [documentation](https://michael-schnell.gitbooks.io/ddd-4-java/content/) of the project at [gitbook](https://www.gitbook.com/book/michael-schnell/ddd-4-java/) (*Work in progress* - Just started!).

Presentation
------------
http://de.slideshare.net/michael-schnell/ddd-4java/

###Snapshots

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
