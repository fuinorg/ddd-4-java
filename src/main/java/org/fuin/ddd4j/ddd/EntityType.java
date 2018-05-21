/**
 * Copyright (C) 2015 Michael Schnell. All rights reserved. 
 * http://www.fuin.org/
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see http://www.gnu.org/licenses/.
 */
package org.fuin.ddd4j.ddd;

import java.io.Serializable;

/**
 * Identifies a type of entity within all entity types of the context. As the
 * simple name of an entity class should be defined by the Ubiquitous Language,
 * the simple class name is a good choice for the type.
 */
public interface EntityType extends Serializable {

    /**
     * Returns the entity type name as string.
     * 
     * @return Unique entity type name.
     */
    public String asString();

}
