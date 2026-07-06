/**
 * Copyright (C) 2015 Michael Schnell. All rights reserved.
 * http://www.fuin.org/
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see http://www.gnu.org/licenses/.
 */
package org.fuin.ddd4j.core;

import org.fuin.objects4j.common.ThreadSafe;
import org.jspecify.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.zip.Adler32;

/**
 * Utility methods and constants.
 */
@ThreadSafe
public final class Ddd4JUtils {

    /**
     * Prefix for unique short identifiers.
     */
    public static final String SHORT_ID_PREFIX = "DDD4J";

    /**
     * Private by intention.
     */
    private Ddd4JUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates an Adler32 checksum based on event type names.
     *
     * @param eventTypes
     *            Types to calculate a checksum for.
     *
     * @return Checksum based on all names.
     */
    public static long calculateChecksum(final Collection<EventType> eventTypes) {
        final Adler32 checksum = new Adler32();
        for (final EventType eventType : eventTypes) {
            checksum.update(eventType.asBaseType().getBytes(StandardCharsets.US_ASCII));
        }
        return checksum.getValue();
    }

    /**
     * Returns the category names of an event - the simple names of all {@link EventCategory} interfaces the
     * event's class implements (searched transitively across super-interfaces and super-classes). These names
     * are recorded in the event's metadata so an event-store projection can select the event by category.
     *
     * @param event Event to inspect (may be {@literal null}).
     *
     * @return Unmodifiable set of category simple names, never {@literal null} (empty if the event belongs to
     *         no category).
     */
    public static Set<String> eventCategories(@Nullable final Object event) {
        if (event == null) {
            return Set.of();
        }
        final Set<String> categories = new LinkedHashSet<>();
        collectEventCategories(event.getClass(), categories);
        return Set.copyOf(categories);
    }

    private static void collectEventCategories(@Nullable final Class<?> clazz, final Set<String> categories) {
        if (clazz == null) {
            return;
        }
        for (final Class<?> iface : clazz.getInterfaces()) {
            if (EventCategory.class.isAssignableFrom(iface) && !iface.equals(EventCategory.class)) {
                categories.add(iface.getSimpleName());
            }
            collectEventCategories(iface, categories);
        }
        collectEventCategories(clazz.getSuperclass(), categories);
    }

}
