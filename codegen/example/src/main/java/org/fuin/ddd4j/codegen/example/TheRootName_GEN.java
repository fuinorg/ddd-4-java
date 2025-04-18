package org.fuin.ddd4j.codegen.example;

import org.fuin.ddd4j.codegen.api.StringVO;

@StringVO(pkg="org.fuin.ddd4j.codegen.example",
        name = "TheRootName", description = "The name of the root",
        jsonb = true, minLength = 1, maxLength = 100,
        pattern = "[a-z][0-9|a-z]*")
public interface TheRootName_GEN {

}
