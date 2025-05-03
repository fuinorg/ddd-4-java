package org.fuin.ddd4j.codegen.test;

import org.fuin.ddd4j.core.AggregateRootId;
import org.fuin.ddd4j.core.EntityType;

public class MyRootId implements AggregateRootId {
    @Override
    public EntityType getType() {
        return null;
    }

    @Override
    public String asString() {
        return "";
    }

    @Override
    public String asTypedString() {
        return "";
    }
}
