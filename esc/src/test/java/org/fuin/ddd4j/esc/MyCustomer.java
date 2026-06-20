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

import org.fuin.ddd4j.core.AbstractAggregateRoot;
import org.fuin.ddd4j.core.ApplyEvent;
import org.fuin.ddd4j.core.EntityType;
import org.fuin.objects4j.common.Contract;
import org.fuin.utils4j.TestOmitted;

/**
 * Customer aggregate. The {@code MyCustomerCreatedEvent} it emits carries the customer name as personal data that requires
 * encryption at rest.
 */
@TestOmitted("Only a test class")
@SuppressWarnings("NullAway.Init")
public final class MyCustomer extends AbstractAggregateRoot<MyCustomerId> {

    private MyCustomerId id;

    private String name;

    /**
     * Default constructor used by the repository. NEVER use in application code!
     */
    public MyCustomer() {
        super();
    }

    /**
     * Constructor with all data.
     *
     * @param id   Unique identifier.
     * @param name Customer name (personal data).
     */
    public MyCustomer(final MyCustomerId id, final String name) {
        super();
        Contract.requireArgNotNull("id", id);
        Contract.requireArgNotNull("name", name);
        apply(new MyCustomerCreatedEvent(id, name));
    }

    @ApplyEvent
    private void applyEvent(final MyCustomerCreatedEvent event) {
        this.id = event.getCustomerId();
        this.name = event.getName();
    }

    @Override
    public EntityType getType() {
        return MyCustomerId.TYPE;
    }

    @Override
    public MyCustomerId getId() {
        return id;
    }

    /**
     * Returns the customer name.
     *
     * @return Customer name.
     */
    public String getName() {
        return name;
    }

}
