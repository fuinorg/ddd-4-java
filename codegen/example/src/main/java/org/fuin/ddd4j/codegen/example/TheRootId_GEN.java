package org.fuin.ddd4j.codegen.example;

import org.fuin.ddd4j.codegen.api.AggregateRootUuidVO;

@AggregateRootUuidVO(
        pkg="org.fuin.ddd4j.codegen.example",
        name = "TheRootId",
        entityType = "THE_ROOT",
        description = "Unique identifier of the root",
        jpa = false, jaxb = false, jsonb = true, openapi = false,
        serialVersionUID = 1000L,
        example = "4d48c20e-6cd2-44c5-8063-767ea0a65ec4"
)
public interface TheRootId_GEN {
}
