package org.fuin.ddd4j.jsonbtestmodel;

import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.fuin.ddd4j.core.DomainEvent;
import org.fuin.ddd4j.core.EntityId;
import org.fuin.ddd4j.core.EntityType;
import org.fuin.ddd4j.core.HasEntityTypeConstant;
import org.fuin.esc.api.HasSerializedDataTypeConstant;
import org.fuin.objects4j.common.HasPublicStaticIsValidMethod;
import org.fuin.objects4j.common.HasPublicStaticValueOfMethod;

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
    static final ArchRule access_only_to_defined_packages = classes()
            .that()
            .resideInAPackage(THIS_PACKAGE)
            .should()
            .onlyDependOnClassesThat()
            .resideInAnyPackage(THIS_PACKAGE, CORE_PACKAGE,
                    "java..",
                    "jakarta.annotation..",
                    "jakarta.json.bind..",
                    "jakarta.validation..",
                    "javax.annotation.concurrent..",
                    "org.fuin.ddd4j.jsonb..",
                    "org.fuin.esc.api..",
                    "org.fuin.objects4j.common..",
                    "org.fuin.objects4j.core..",
                    "org.fuin.objects4j.jsonb..",
                    "org.fuin.objects4j.ui..");

    @ArchTest
    static final ArchRule verify_domain_events = classes()
            .that().implement(DomainEvent.class)
            .and().doNotHaveModifier(JavaModifier.ABSTRACT)
            .should().beAnnotatedWith(HasSerializedDataTypeConstant.class);

    @ArchTest
    static final ArchRule verify_entity_ids = classes()
            .that().areAssignableTo(EntityId.class)
            .and().doNotHaveModifier(JavaModifier.ABSTRACT)
            .should().beAnnotatedWith(HasPublicStaticValueOfMethod.class)
            .andShould().beAnnotatedWith(HasPublicStaticIsValidMethod.class)
            .andShould().beAnnotatedWith(HasEntityTypeConstant.class);

}