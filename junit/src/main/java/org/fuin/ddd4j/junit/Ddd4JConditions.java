package org.fuin.ddd4j.junit;

import com.tngtech.archunit.PublicAPI;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.lang.ArchRule;
import org.fuin.ddd4j.core.DomainEvent;
import org.fuin.ddd4j.core.EntityId;
import org.fuin.ddd4j.core.HasEntityTypeConstant;
import org.fuin.esc.api.HasSerializedDataTypeConstant;
import org.fuin.objects4j.common.HasPublicStaticIsValidMethod;
import org.fuin.objects4j.common.HasPublicStaticValueOfMethod;

import static com.tngtech.archunit.PublicAPI.Usage.ACCESS;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

/**
 * Defines ArchUnit conditions for classes depending on this library.
 */
public final class Ddd4JConditions {

    private Ddd4JConditions() {
    }

    /**
     * Verifies the rules that are required for {@link DomainEvent} classes.
     */
    @PublicAPI(usage = ACCESS)
    public static final ArchRule DOMAIN_EVENT_RULES = classes()
            .that().implement(DomainEvent.class)
            .and().doNotHaveModifier(JavaModifier.ABSTRACT)
            .should().beAnnotatedWith(HasSerializedDataTypeConstant.class);

    /**
     * Verifies the rules that are required for {@link EntityId} classes.
     */
    @PublicAPI(usage = ACCESS)
    public static final ArchRule ENTITY_ID_RULES = classes()
            .that().areAssignableTo(EntityId.class)
            .and().doNotHaveModifier(JavaModifier.ABSTRACT)
            .should().beAnnotatedWith(HasPublicStaticValueOfMethod.class)
            .andShould().beAnnotatedWith(HasPublicStaticIsValidMethod.class)
            .andShould().beAnnotatedWith(HasEntityTypeConstant.class);

}
