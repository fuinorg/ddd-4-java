/**
 * Copyright (C) 2020 Michael Schnell. All rights reserved. http://www.fuin.org/
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.fuin.ddd4j.codegen.processor;

import io.toolisticon.cute.Cute;
import io.toolisticon.cute.CuteApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * Test for the {@link AggregateRootUuidVOTemplate} class.
 */
public final class AggregateRootUuidVOTemplateTest {

    CuteApi.BlackBoxTestSourceFilesInterface compileTestBuilder;

    @BeforeEach
    public void init() {
        compileTestBuilder = Cute.blackBoxTest().given().processors(ValueObjectProcessor.class);
    }

    @Test
    public void testAllConverters() throws IOException {
        TestUtils.testAnnotation(compileTestBuilder, "CompanyId", "AllConverters", """
                package input;
                
                import org.fuin.ddd4j.codegen.api.AggregateRootUuidVO;
                
                @AggregateRootUuidVO(pkg="org.fuin.ddd4j.codegen.test", 
                        name = "CompanyId",
                        entityType = "COMPANY",
                        description = "Unique identifier of a company",
                        jpa = true, jaxb = true, jsonb = true, openapi = true,
                        serialVersionUID = 1000L, 
                        example = "4d48c20e-6cd2-44c5-8063-767ea0a65ec4"
                )
                public interface CompanyIdExample {
                }
                
                """);
    }

    @Test
    public void testNoConverters() throws IOException {
        TestUtils.testAnnotation(compileTestBuilder, "CompanyId", "NoConverters", """
                package input;
                
                import org.fuin.ddd4j.codegen.api.AggregateRootUuidVO;
                
                @AggregateRootUuidVO(pkg="org.fuin.ddd4j.codegen.test", 
                        name = "CompanyId",
                        entityType = "COMPANY",
                        description = "Unique identifier of a company",
                        serialVersionUID = 1000L, 
                        example = "4d48c20e-6cd2-44c5-8063-767ea0a65ec4"
                )
                public interface CompanyIdExample {
                }
                
                """);
    }

    @Test
    public void testJaxbOnly() throws IOException {
        TestUtils.testAnnotation(compileTestBuilder, "CompanyId", "JaxbOnly", """
                package input;
                
                import org.fuin.ddd4j.codegen.api.AggregateRootUuidVO;
                
                @AggregateRootUuidVO(pkg="org.fuin.ddd4j.codegen.test", 
                        name = "CompanyId",
                        entityType = "COMPANY",
                        description = "Unique identifier of a company",
                        jaxb = true,
                        serialVersionUID = 1000L, 
                        example = "4d48c20e-6cd2-44c5-8063-767ea0a65ec4"
                )
                public interface CompanyIdExample {
                }
                
                """);
    }

    @Test
    public void testJpaOnly() throws IOException {
        TestUtils.testAnnotation(compileTestBuilder, "CompanyId", "JpaOnly", """
                package input;
                
                import org.fuin.ddd4j.codegen.api.AggregateRootUuidVO;
                
                @AggregateRootUuidVO(pkg="org.fuin.ddd4j.codegen.test", 
                        name = "CompanyId",
                        entityType = "COMPANY",
                        description = "Unique identifier of a company",
                        jpa = true,
                        serialVersionUID = 1000L, 
                        example = "4d48c20e-6cd2-44c5-8063-767ea0a65ec4"
                )
                public interface CompanyIdExample {
                }
                
                """);
    }

    @Test
    public void testJsonbOnly() throws IOException {
        TestUtils.testAnnotation(compileTestBuilder, "CompanyId", "JsonbOnly", """
                package input;
                
                import org.fuin.ddd4j.codegen.api.AggregateRootUuidVO;
                
                @AggregateRootUuidVO(pkg="org.fuin.ddd4j.codegen.test", 
                        name = "CompanyId",
                        entityType = "COMPANY",
                        description = "Unique identifier of a company",
                        jsonb = true,
                        serialVersionUID = 1000L, 
                        example = "4d48c20e-6cd2-44c5-8063-767ea0a65ec4"
                )
                public interface CompanyIdExample {
                }
                
                """);
    }

    @Test
    public void testOpenApiOnly() throws IOException {
        TestUtils.testAnnotation(compileTestBuilder, "CompanyId", "OpenApiOnly", """
                package input;
                
                import org.fuin.ddd4j.codegen.api.AggregateRootUuidVO;
                
                @AggregateRootUuidVO(pkg="org.fuin.ddd4j.codegen.test", 
                        name = "CompanyId",
                        entityType = "COMPANY",
                        description = "Unique identifier of a company",
                        openapi = true,
                        serialVersionUID = 1000L, 
                        example = "4d48c20e-6cd2-44c5-8063-767ea0a65ec4"
                )
                public interface CompanyIdExample {
                }
                
                """);
    }

}
