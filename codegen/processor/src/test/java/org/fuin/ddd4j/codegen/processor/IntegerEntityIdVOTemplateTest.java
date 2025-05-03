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
 * Test for the {@link IntegerEntityIdVOTemplate} class.
 */
public final class IntegerEntityIdVOTemplateTest {

    CuteApi.BlackBoxTestSourceFilesInterface compileTestBuilder;

    @BeforeEach
    public void init() {
        compileTestBuilder = Cute.blackBoxTest().given().processors(ValueObjectProcessor.class);
    }

    @Test
    public void testAllConverters() {

        TestUtils.testAnnotation(compileTestBuilder, "RampId", "AllConverters", """
                package input;
                
                import org.fuin.ddd4j.codegen.api.IntegerEntityIdVO;
                
                @IntegerEntityIdVO(pkg="org.fuin.ddd4j.codegen.test",
                         name = "RampId", entityType = "RAMP", description = "Unique identifier of a ramp",
                         jpa = true, jaxb = true, jsonb = true, openapi = true,
                         serialVersionUID = 1000L, minValue = 1, maxValue = 100
                )
                public interface RampIdExample {
                }
                """);
    }

    @Test
    public void testNoConverters() {

        TestUtils.testAnnotation(compileTestBuilder, "RampId", "NoConverters", """
                package input;
                
                import org.fuin.ddd4j.codegen.api.IntegerEntityIdVO;
                
                @IntegerEntityIdVO(pkg="org.fuin.ddd4j.codegen.test",
                         name = "RampId", entityType = "RAMP", description = "Unique identifier of a ramp",
                         serialVersionUID = 1000L, minValue = 1, maxValue = 100
                )
                public interface RampIdExample {
                }
                """);
    }

    @Test
    public void testJpaOnly() {

        TestUtils.testAnnotation(compileTestBuilder, "RampId", "JpaOnly", """
                package input;
                
                import org.fuin.ddd4j.codegen.api.IntegerEntityIdVO;
                
                @IntegerEntityIdVO(pkg="org.fuin.ddd4j.codegen.test",
                         name = "RampId", entityType = "RAMP", description = "Unique identifier of a ramp",
                         jpa = true,
                         serialVersionUID = 1000L, minValue = 1, maxValue = 100
                )
                public interface RampIdExample {
                }
                """);
    }

    @Test
    public void testJaxbOnly() {

        TestUtils.testAnnotation(compileTestBuilder, "RampId", "JaxbOnly", """
                package input;
                
                import org.fuin.ddd4j.codegen.api.IntegerEntityIdVO;
                
                @IntegerEntityIdVO(pkg="org.fuin.ddd4j.codegen.test",
                         name = "RampId", entityType = "RAMP", description = "Unique identifier of a ramp",
                         jaxb = true,
                         serialVersionUID = 1000L, minValue = 1, maxValue = 100
                )
                public interface RampIdExample {
                }
                """);
    }

    @Test
    public void testJsonbOnly() {

        TestUtils.testAnnotation(compileTestBuilder, "RampId", "JsonbOnly", """
                package input;
                
                import org.fuin.ddd4j.codegen.api.IntegerEntityIdVO;
                
                @IntegerEntityIdVO(pkg="org.fuin.ddd4j.codegen.test",
                         name = "RampId", entityType = "RAMP", description = "Unique identifier of a ramp",
                         jsonb = true,
                         serialVersionUID = 1000L, minValue = 1, maxValue = 100
                )
                public interface RampIdExample {
                }
                """);
    }

    @Test
    public void testOpenapiOnly() {

        TestUtils.testAnnotation(compileTestBuilder, "RampId", "OpenApiOnly", """
                package input;
                
                import org.fuin.ddd4j.codegen.api.IntegerEntityIdVO;
                
                @IntegerEntityIdVO(pkg="org.fuin.ddd4j.codegen.test",
                         name = "RampId", entityType = "RAMP", description = "Unique identifier of a ramp",
                         openapi = true,
                         serialVersionUID = 1000L, minValue = 1, maxValue = 100
                )
                public interface RampIdExample {
                }
                """);
    }

}
