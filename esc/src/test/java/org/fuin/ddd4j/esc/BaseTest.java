/**
 * Copyright (C) 2013 Future Invent Informationsmanagement GmbH. All rights
 * reserved. <http://www.fuin.org/>
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fuin.ddd4j.esc;

import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchIgnore;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static org.fuin.units4j.archunit.AllTopLevelClassesHaveATestCondition.haveACorrespondingClassEndingWith;

@AnalyzeClasses(packagesOf = BaseTest.class)
class BaseTest {

    @ArchTest
    static final ArchRule all_classes_should_have_tests =
            classes()
                    .that()
                    .areTopLevelClasses()
                    .and().areNotInterfaces()
                    .and().areNotRecords()
                    .and().areNotEnums()
                    .and().doNotHaveModifier(JavaModifier.ABSTRACT)
                    .and().areNotAnnotatedWith(ArchIgnore.class)
                    .should(haveACorrespondingClassEndingWith("Test"));

}

