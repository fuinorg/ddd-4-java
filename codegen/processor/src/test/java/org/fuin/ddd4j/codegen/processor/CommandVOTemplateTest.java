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
 * Test for the {@link CommandVOTemplate} class.
 */
public final class CommandVOTemplateTest {

    CuteApi.BlackBoxTestSourceFilesInterface compileTestBuilder;

    @BeforeEach
    public void init() {
        compileTestBuilder = Cute.blackBoxTest().given().processors(ValueObjectProcessor.class);
    }

    @Test
    public void testJsonb() {

        TestUtils.testAnnotation(compileTestBuilder, "MyCommand",  "Jsonb","org.fuin.ddd4j.codegen.test", """
                package input;
                
                import jakarta.validation.constraints.NotNull;
                import jakarta.annotation.Nullable;
                import org.fuin.ddd4j.codegen.api.CommandVO;
                import org.fuin.objects4j.ui.*;
                
                @CommandVO(pkg="org.fuin.ddd4j.codegen.test", name = "MyCommand",
                        entityIdPathClasses = {" MyRootId", "MyId" },
                        entityIdPathParams = "theAId, myId",
                        description = "Do it!",
                        jsonb = true, openapi = true,
                        serialVersionUID = 1000L,
                        aggregateIdClass = "MyRootId",
                        entityIdClass = "MyId",
                        message = "Issued MyCommand"
                )
                public interface MyCommandExample {
                
                    @ShortLabel("ROOT-ID")
                    @Label("Root Identifier")
                    @Tooltip("Uniquely identifies The Root")
                    @Examples({"e4baf6c5-ccb9-4580-9d59-41860c140189", "00000000-0000-0000-0000-000000000000"})
                    org.fuin.ddd4j.codegen.test.MyId myId = null;
                
                    @Label("Nullable Field")
                    @Nullable
                    String fieldNullable = null;
                
                    @Label("Non-Null Field")
                    String fieldNonNull = null;
                
                }
                """);
    }

    @Test
    public void testJackson() {

        TestUtils.testAnnotation(compileTestBuilder, "MyCommand",  "Jackson","org.fuin.ddd4j.codegen.test", """
                package input;
                
                import jakarta.validation.constraints.NotNull;
                import org.fuin.ddd4j.codegen.api.CommandVO;
                import org.fuin.objects4j.ui.*;
                
                @CommandVO(pkg="org.fuin.ddd4j.codegen.test", name = "MyCommand",
                        entityIdPathClasses = {" MyRootId", "MyId" },
                        entityIdPathParams = "theAId, myId",
                        description = "Do it!",
                        jackson = true, openapi = true,
                        serialVersionUID = 1000L,
                        aggregateIdClass = "MyRootId",
                        entityIdClass = "MyId",
                        message = "Issued MyCommand"
                )
                public interface MyCommandExample {
                
                    @ShortLabel("ROOT-ID")
                    @Label("Root Identifier")
                    @Tooltip("Uniquely identifies The Root")
                    @Examples({"e4baf6c5-ccb9-4580-9d59-41860c140189", "00000000-0000-0000-0000-000000000000"})
                    org.fuin.ddd4j.codegen.test.MyId myId = null;
                
                }
                """);
    }

    @Test
    public void testJaxb() {

        TestUtils.testAnnotation(compileTestBuilder, "MyCommand",  "Jaxb","org.fuin.ddd4j.codegen.test", """
                package input;
                
                import jakarta.validation.constraints.NotNull;
                import org.fuin.ddd4j.codegen.api.CommandVO;
                import org.fuin.objects4j.ui.*;
                
                @CommandVO(pkg="org.fuin.ddd4j.codegen.test", name = "MyCommand",
                        entityIdPathClasses = {" MyRootId", "MyId" },
                        entityIdPathParams = "theAId, myId",
                        description = "Do it!",
                        jaxb = true, openapi = true,
                        serialVersionUID = 1000L,
                        aggregateIdClass = "MyRootId",
                        entityIdClass = "MyId",
                        message = "Issued MyCommand"
                )
                public interface MyCommandExample {
                
                    @ShortLabel("ROOT-ID")
                    @Label("Root Identifier")
                    @Tooltip("Uniquely identifies The Root")
                    @Examples({"e4baf6c5-ccb9-4580-9d59-41860c140189", "00000000-0000-0000-0000-000000000000"})
                    org.fuin.ddd4j.codegen.test.MyId myId = null;
                
                }
                """);
    }

}
