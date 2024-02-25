package org.fuin.ddd4j.jsonb;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.fuin.ddd4j.core.AggregateRootUuid;
import org.fuin.ddd4j.core.DomainEvent;
import org.fuin.ddd4j.core.EntityId;
import org.fuin.ddd4j.core.EntityType;
import org.fuin.ddd4j.core.HasEntityTypeConstant;
import org.fuin.ddd4j.jsonbtest.ACreatedEvent;
import org.fuin.esc.api.HasSerializedDataTypeConstant;
import org.fuin.objects4j.common.HasPublicStaticValueOfMethod;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.DependencyRules.NO_CLASSES_SHOULD_DEPEND_UPPER_PACKAGES;

/**
 * Tests architectural aspects.
 */
@AnalyzeClasses(packagesOf = ArchitectureTest.class, importOptions = ImportOption.DoNotIncludeTests.class)
public class ArchitectureTest {

    private static final String THIS_PACKAGE = ArchitectureTest.class.getPackageName();

    private static final String CORE_PACKAGE = EntityType.class.getPackageName();

    @ArchTest
    static final ArchRule no_accesses_to_upper_package = NO_CLASSES_SHOULD_DEPEND_UPPER_PACKAGES;

    @ArchTest
    static final ArchRule core_access_only_to_defined_packages = classes()
            .that()
            .resideInAPackage(THIS_PACKAGE)
            .should()
            .onlyDependOnClassesThat()
            .resideInAnyPackage(THIS_PACKAGE, CORE_PACKAGE,
                    "java..",
                    "jakarta.annotation..",
                    "jakarta.json.bind..",
                    "jakarta.validation.constraints..",
                    "javax.annotation.concurrent..",
                    "org.fuin.objects4j.common..",
                    "org.fuin.objects4j.jsonb..",
                    "org.fuin.objects4j.ui..");

    @Test
    public void testDomainEventsAnnotations() {

        final JavaClasses importedClasses = new ClassFileImporter().importPackages(ACreatedEvent.class.getPackageName());

        classes()
                .that().implement(DomainEvent.class)
                .and().doNotHaveModifier(JavaModifier.ABSTRACT)
                .should().beAnnotatedWith(HasSerializedDataTypeConstant.class)
                .check(importedClasses);
    }

    @Test
    public void testEntityIdAnnotations() {

        final JavaClasses importedClasses = new ClassFileImporter().importPackages(ArchitectureTest.class.getPackageName());

        classes()
                .that().areAssignableTo(AggregateRootUuid.class)
                .and().implement(EntityId.class)
                .and().doNotHaveModifier(JavaModifier.ABSTRACT)
                .should().beAnnotatedWith(HasPublicStaticValueOfMethod.class)
                .andShould().beAnnotatedWith(HasEntityTypeConstant.class)
                .allowEmptyShould(true).check(importedClasses);

    }

}
