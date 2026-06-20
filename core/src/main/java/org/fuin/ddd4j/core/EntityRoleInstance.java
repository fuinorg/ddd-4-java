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

import java.util.Objects;

/**
 * Defines a role related to an entity.
 *
 * @param entityIdPath Entity identifier path.
 * @param type         Type of the role (like "manager").
 */
public record EntityRoleInstance(EntityIdPath entityIdPath, String type) implements SecurityRole {

    /**
     * Returns the instance as a simple role.
     *
     * @return Representation of the role as stored for example in Keycloak.
     */
    public SimpleRole asSimpleRole() {
        return new SimpleRole(entityIdPath.toString() + EntityIdPath.PATH_SEPARATOR + type);
    }

    /**
     * Verifies if the given string could be parsed into an instance of this type.
     *
     * @param factory Helper to verify entity identifiers from type and id.
     * @param str     String with the entity role to parse.
     * @return In case the string is valid {@literal true}.
     */
    public static boolean isValid(final EntityIdFactory factory, final String str) {
        final int p = str.lastIndexOf(EntityIdPath.PATH_SEPARATOR);
        if (p == -1) {
            throw new IllegalArgumentException("Invalid role: ' " + str + "'");
        }
        final String entityIdPathStr = str.substring(0, p);
        return EntityIdPath.isValid(factory, entityIdPathStr);
    }

    /**
     * Creates an instance based on the given string.
     *
     * @param factory Helper to create entity identifiers from type and id.
     * @param str     String with the entity role to parse.
     * @return New instance.
     */
    public static EntityRoleInstance valueOf(final EntityIdFactory factory, final String str) {
        final int p = str.lastIndexOf(EntityIdPath.PATH_SEPARATOR);
        if (p == -1) {
            throw new IllegalArgumentException("Invalid role: ' " + str + "'");
        }
        final String entityIdPathStr = str.substring(0, p);
        final String type = str.substring(p + 1);
        final EntityIdPath entityIdPath = Objects.requireNonNull(
                EntityIdPath.valueOf(factory, entityIdPathStr), "entityIdPath");
        return new EntityRoleInstance(entityIdPath, type);
    }

}
