package org.fuin.ddd4j.base;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.fuin.ddd4j.ddd.AggregateRootUuid;
import org.fuin.ddd4j.ddd.DomainEvent;
import org.fuin.ddd4j.ddd.EntityId;
import org.fuin.ddd4j.ddd.HasEntityTypeConstant;
import org.fuin.esc.api.HasSerializedDataTypeConstant;
import org.fuin.objects4j.common.HasPublicStaticValueOfMethod;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

/**
 * Tests architectural aspects.
 */
public class ArchitectureTest {

    private static JavaClasses importedClasses;

    @BeforeAll
    public static void beforeAll() {
        importedClasses = new ClassFileImporter().importPackages("org.fuin.ddd4j");
    }

    @Test
    public void testDomainEventsAnnotations() {

        classes()
                .that().implement(DomainEvent.class)
                .and().doNotHaveModifier(JavaModifier.ABSTRACT)
                .should().beAnnotatedWith(HasSerializedDataTypeConstant.class)
                .check(importedClasses);
    }

    @Test
    public void testEntityIdAnnotations() {

        classes()
                .that().areAssignableTo(AggregateRootUuid.class)
                .and().implement(EntityId.class)
                .and().doNotHaveModifier(JavaModifier.ABSTRACT)
                .should().beAnnotatedWith(HasPublicStaticValueOfMethod.class)
                .andShould().beAnnotatedWith(HasEntityTypeConstant.class)
                .allowEmptyShould(true).check(importedClasses);

    }

}
