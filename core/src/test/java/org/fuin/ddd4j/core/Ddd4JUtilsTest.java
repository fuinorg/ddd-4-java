package org.fuin.ddd4j.core;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for the {@link Ddd4JUtils} class.
 */
public class Ddd4JUtilsTest {

    @Test
    public void testCalculateChecksum() {

        // PREPARE
        final Set<EventType> eventTypes = new HashSet<>();
        eventTypes.add(new EventType("PersonCreatedEvent"));
        eventTypes.add(new EventType("PersonRenamedEvent"));
        eventTypes.add(new EventType("PersonDeletedEvent"));

        // TEST
        final long checksum = Ddd4JUtils.calculateChecksum(eventTypes);

        // VERIFY
        assertThat(checksum).isEqualTo(1341789591L);

    }



    @Test
    public void testEventCategoriesReturnsTheMarkersAnEventImplements() {

        // The categories are discovered reflectively, so a new marker interface needs no registration -
        // which is exactly why it is worth asserting that adding one is enough.
        assertThat(Ddd4JUtils.eventCategories(new SuspendedEvent()))
                .containsExactlyInAnyOrder("ExileEvent");
        assertThat(Ddd4JUtils.eventCategories(new ResumedEvent()))
                .containsExactlyInAnyOrder("ReturnFromExileEvent");
        assertThat(Ddd4JUtils.eventCategories(new CreatedAndDeletedEvent()))
                .containsExactlyInAnyOrder("GenesisEvent", "ExodusEvent");
    }

    @Test
    public void testEventCategoriesAreInheritedAndNeverIncludeTheBaseMarker() {

        // A category reached through a superclass counts, and 'EventCategory' itself is not a category.
        assertThat(Ddd4JUtils.eventCategories(new InheritsItsCategory()))
                .containsExactlyInAnyOrder("ExileEvent");
        assertThat(Ddd4JUtils.eventCategories(new PlainEvent())).isEmpty();
        assertThat(Ddd4JUtils.eventCategories(null)).isEmpty();
    }

    private static class SuspendedEvent implements ExileEvent {
    }

    private static class ResumedEvent implements ReturnFromExileEvent {
    }

    private static class CreatedAndDeletedEvent implements GenesisEvent, ExodusEvent {
    }

    private static class InheritsItsCategory extends SuspendedEvent {
    }

    private static class PlainEvent implements EventCategory {
    }

}
