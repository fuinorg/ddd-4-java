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
package org.fuin.ddd4j.esc;

import org.fuin.ddd4j.core.AggregateRootUuid;
import org.fuin.ddd4j.core.EntityType;
import org.fuin.ddd4j.core.StringBasedEntityType;
import org.fuin.utils4j.TestOmitted;

import java.util.UUID;

/**
 * Unique identifier of a customer aggregate.
 */
@TestOmitted("Only a test class")
public final class MyCustomerId extends AggregateRootUuid {

    /** Type of entity this identifier represents. */
    public static final EntityType TYPE = new StringBasedEntityType("MyCustomer");

    /**
     * Default constructor that generates a random identifier.
     */
    public MyCustomerId() {
        super(TYPE);
    }

    /**
     * Constructor with UUID.
     *
     * @param uuid UUID.
     */
    public MyCustomerId(final UUID uuid) {
        super(TYPE, uuid);
    }

}
