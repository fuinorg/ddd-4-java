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


}
