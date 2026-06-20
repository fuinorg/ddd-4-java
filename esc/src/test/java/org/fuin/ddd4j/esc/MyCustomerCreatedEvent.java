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

import org.fuin.ddd4j.core.EntityIdPath;
import org.fuin.ddd4j.core.EventType;
import org.fuin.ddd4j.core.ObjectSerDeserializer;
import org.fuin.ddd4j.core.RequiresPartialEncryption;
import org.fuin.ddd4j.jsonb.AbstractDomainEvent;
import org.fuin.objects4j.crypto.EncryptedData;
import org.fuin.objects4j.crypto.EncryptedDataService;
import org.fuin.objects4j.crypto.EncryptionKeyIdUnknownException;
import org.fuin.utils4j.TestOmitted;

import java.io.Serial;

/**
 * A customer was created. The {@link #getName() name} is personal data that must be encrypted before it is stored, so the event
 * is replaced by a {@link MyCustomerCreatedEventEncrypted} via {@link #encrypt(ObjectSerDeserializer, EncryptedDataService)}.
 */
@TestOmitted("Only a test class")
@SuppressWarnings("NullAway.Init")
public final class MyCustomerCreatedEvent extends AbstractDomainEvent<MyCustomerId>
        implements RequiresPartialEncryption<MyCustomerCreatedEvent, MyCustomerCreatedEventEncrypted> {

    @Serial
    private static final long serialVersionUID = 1000L;

    /** Unique name of the event used to store it - Should never change. */
    public static final EventType TYPE = new EventType(MyCustomerCreatedEvent.class.getSimpleName());

    private MyCustomerId customerId;

    private String name;

    /**
     * Default constructor only for deserialization.
     */
    protected MyCustomerCreatedEvent() {
        super();
    }

    /**
     * Constructor with event data.
     *
     * @param customerId Identifier of the customer.
     * @param name       Customer name (personal data).
     */
    public MyCustomerCreatedEvent(final MyCustomerId customerId, final String name) {
        super(new EntityIdPath(customerId));
        this.customerId = customerId;
        this.name = name;
    }

    @Override
    public EventType getEventType() {
        return TYPE;
    }

    /**
     * Returns the identifier of the customer.
     *
     * @return Customer identifier.
     */
    public MyCustomerId getCustomerId() {
        return customerId;
    }

    /**
     * Returns the customer name.
     *
     * @return Customer name.
     */
    public String getName() {
        return name;
    }

    @Override
    public MyCustomerCreatedEventEncrypted encrypt(final ObjectSerDeserializer serDeserializer, final EncryptedDataService service)
            throws EncryptionKeyIdUnknownException {
        final byte[] clear = serDeserializer.serialize(name);
        final EncryptedData encrypted = service.encrypt(customerId.asString(), "customer-name", serDeserializer.getContentType(), clear);
        return new MyCustomerCreatedEventEncrypted(customerId, encrypted);
    }

}
