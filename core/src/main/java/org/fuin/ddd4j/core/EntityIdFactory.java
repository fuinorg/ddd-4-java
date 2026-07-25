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

import java.util.Set;

/**
 * Factory to create entity identifier.
 * All implementations are expected to be thread safe.
 */
@ThreadSafe
public interface EntityIdFactory {

    /**
     * Returns the identifier classes this factory can create instances of.
     * Knowing them allows a serialization framework to register a (de)serializer per class instead of
     * requiring every application to list its identifiers by hand.
     * An implementation that cannot enumerate its classes returns an empty set - the default.
     *
     * @return Known identifier classes, never {@literal null}.
     */
    default Set<Class<? extends EntityId>> getIdClasses() {
        return Set.of();
    }

    /**
     * Verifies if the given type string is a valid one.
     *
     * @param type
     *            Type to be verified.
     *
     * @return TRUE if the factory can create identifiers for the given type.
     */
    boolean containsType(String type);

    /**
     * Determines if an identifier of the given type is valid.
     *
     * @param type
     *            Type of the identifier.
     * @param id
     *            Identifier to be verified.
     *
     * @return TRUE if the factory can create and identifier for the given type and value.
     */
    boolean isValid(String type, String id);

    /**
     * Creates an entity id by type and string identifier.
     *
     * @param type
     *            Type of the identifier.
     * @param id
     *            Identifier.
     *
     * @return Entity identifier.
     */
    EntityId createEntityId(String type, String id);

}
