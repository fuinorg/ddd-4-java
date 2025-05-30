/**
 * Copyright (C) 2020 Michael Schnell. All rights reserved. http://www.fuin.org/
 * <p>
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.fuin.ddd4j.codegen.processor;

import io.toolisticon.cute.Cute;
import io.toolisticon.cute.CuteApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test for the {@link StringVOTemplate} class.
 */
public final class StringVOTemplateTest {

    CuteApi.BlackBoxTestSourceFilesInterface compileTestBuilder;

    @BeforeEach
    public void init() {
        compileTestBuilder = Cute.blackBoxTest().given().processors(ValueObjectProcessor.class);
    }

    @Test
    public void testAllConverters() {
        TestUtils.testAnnotation(compileTestBuilder, "CompanyKey",  "AllConverters","""
                package input;
                
                import org.fuin.ddd4j.codegen.api.StringVO;
                
                @StringVO(pkg="org.fuin.ddd4jcodegen.test", name = "CompanyKey", description = "Human readable unique key of a company",
                        jpa = true, jaxb = true, jsonb = true, openapi = true,
                        serialVersionUID = 1000L, pattern = "[a-z0-9][a-z0-9-]+",
                        minLength = 0, maxLength = 50, example = "john-doe-inc"
                )
                public interface CompanyKeyExample {
                }
                """);
    }

}
