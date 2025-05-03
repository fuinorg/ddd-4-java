package org.fuin.ddd4j.codegen.example;

import jakarta.annotation.Nonnull;
import org.fuin.ddd4j.codegen.api.CommandVO;
import org.fuin.objects4j.ui.Examples;
import org.fuin.objects4j.ui.Label;
import org.fuin.objects4j.ui.ShortLabel;
import org.fuin.objects4j.ui.Tooltip;

@CommandVO(pkg="org.fuin.ddd4j.codegen.example", name = "TheRootCreateCommand",
        entityIdPathClasses = { "TheRootId", "TheEntityId" },
        entityIdPathParams = "theRootId, theEntityId",
        description = "Create the root!",
        jsonb = true,
        serialVersionUID = 1000L,
        aggregateIdClass = "TheRootId",
        entityIdClass = "TheEntityId",
        message = "This is the command!"
)
public interface TheRootCreateCommand_GEN {

    @Nonnull
    @ShortLabel("ROOT-ID")
    @Label("Root Identifier")
    @Tooltip("Uniquely identifies The Root")
    @Examples({"e4baf6c5-ccb9-4580-9d59-41860c140189", "00000000-0000-0000-0000-000000000000"})
    TheRootId id = null;

    @Nonnull
    @ShortLabel("ROOT-NAME")
    @Label("Root Name")
    @Tooltip("Name of The Root")
    @Examples({"FooBar", "Barefoot"})
    TheRootName name = null;

    @ShortLabel("FOO")
    @Label("The foo")
    @Tooltip("Whatever the 'foo' is...")
    @Examples({"0", "1", "123456"})
    int foo = 0;

}
