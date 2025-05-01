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
package org.fuin.ddd4j.jacksontest;

import org.fuin.ddd4j.core.AbstractAggregateRoot;
import org.fuin.ddd4j.core.AggregateRoot;
import org.fuin.ddd4j.core.ApplyEvent;

/**
 * Example for a base class that is shared between aggregate roots.
 */
public abstract class BaseRoot<ROOT_ID extends ImplRootId, ROOT extends AggregateRoot<ROOT_ID>> extends AbstractAggregateRoot<ROOT_ID> {

    private DEvent stored;

    /**
     * Example of a method in a base aggregate root class.
     */
    public void doD() {
        apply(new DEvent(getId()));
    }

    @ApplyEvent
    public void applyEvent(final DEvent event) {
        stored = event;
    }

    /**
     * Returns the stored event or {@literal null} if the {@link #applyEvent(DEvent)} emthod was never called.
     *
     * @return Event.
     */
    public DEvent getStored() {
        return stored;
    }

}
