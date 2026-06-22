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

import org.fuin.objects4j.common.Immutable;

import java.util.Objects;

/**
 * Defines a role based on an entity.
 * <p>
 * Example: A company (aggregate COMPANY) has departements (entity DEPARTEMENT) which
 * can have sub-departements (same entity DEPARTEMENT).
 * </p>
 * <p>
 * The company has a "managing director" and several "department heads".
 * So the two entity roles are: "MANAGING_DIRECTOR" and "DEPARTEMENT_HEAD".
 * </p>
 *
 * @param name Role name that follows pattern {@link #PATTERN}.
 */
@Immutable
public record EntityRole(String name) implements SecurityRole {

    public static final String PATTERN = "[A-Z][A-Z0-9_]{0,19}";

    public EntityRole {
        Objects.requireNonNull(name);
        if (!name.matches(PATTERN)) {
            throw new IllegalArgumentException("Role name is expected to follow the pattern '"
                    + PATTERN + "', but was: " + name);
        }
    }

}
