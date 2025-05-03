package org.fuin.ddd4j.codegen.example;

import org.fuin.ddd4j.codegen.api.IntegerEntityIdVO;

@IntegerEntityIdVO(
        pkg="org.fuin.ddd4j.codegen.example",
        name = "TheEntityId",
        entityType = "THE_ENTITY",
        description = "Unique identifier of the entity",
        jsonb = true,
        serialVersionUID = 1000L
)
public interface TheEntityId_GEN {
}
