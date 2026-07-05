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

import org.fuin.ddd4j.core.AggregateRootId;
import org.fuin.ddd4j.core.EntityIdPath;
import org.fuin.ddd4j.core.EventType;
import org.fuin.ddd4j.core.RemovedPrivateData;
import org.fuin.ddd4j.jsonb.AbstractDomainEvent;
import org.fuin.utils4j.TestOmitted;

import java.io.Serial;

/**
 * The customer's private data was crypto-shredded (its encryption key was destroyed). This is a payload-free
 * tombstone - it carries no personal data - that signals downstream consumers to purge any derived data they
 * hold for the customer. See {@link RemovedPrivateData}.
 */
@TestOmitted("Only a test class")
@SuppressWarnings("NullAway.Init")
public final class MyCustomerPrivateDataRemoved extends AbstractDomainEvent<MyCustomerId>
        implements RemovedPrivateData {

    @Serial
    private static final long serialVersionUID = 1000L;

    /** Unique name of the event used to store it - Should never change. */
    public static final EventType TYPE = new EventType(MyCustomerPrivateDataRemoved.class.getSimpleName());

    private MyCustomerId customerId;

    /**
     * Default constructor only for deserialization.
     */
    protected MyCustomerPrivateDataRemoved() {
        super();
    }

    /**
     * Constructor with event data.
     *
     * @param customerId Identifier of the customer whose private data was removed.
     */
    public MyCustomerPrivateDataRemoved(final MyCustomerId customerId) {
        super(new EntityIdPath(customerId));
        this.customerId = customerId;
    }

    @Override
    public EventType getEventType() {
        return TYPE;
    }

    @Override
    public AggregateRootId getSubjectId() {
        return customerId;
    }

}
