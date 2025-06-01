# objects4j-jpa
Jakarta Persistence API ([JPA](https://jakarta.ee/specifications/persistence/3.1/jakarta-persistence-spec-3.1)) attribute converters for the types defined in [Core](../core).

## Getting started
Simply add [@Convert](https://jakarta.ee/specifications/persistence/3.1/apidocs/jakarta.persistence/jakarta/persistence/convert) to your field:
```java
@Entity(name = "MY_ENTITY")
public class MyEntity {
    @Column(name = "AGGREGATE_VERSION")
    @Convert(converter = AggregateVersionAttributeConverter.class)
    private AggregateVersion version;
```
You can also create a `package-info.java` file and add all the converters at once if you use Hibernate's [@ConverterRegistration](https://docs.jboss.org/hibernate/orm/6.3/javadocs/org/hibernate/annotations/ConverterRegistration.html) annotation:
```java
@ConverterRegistration(converter = AggregateVersionAttributeConverter.class, autoApply = true)
package foo.bar;

import org.hibernate.annotations.ConverterRegistration;
import org.fuin.ddd4j.jpa.AggregateVersionAttributeConverter;
```
