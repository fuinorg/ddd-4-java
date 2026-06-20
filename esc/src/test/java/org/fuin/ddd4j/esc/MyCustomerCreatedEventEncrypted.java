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
import org.fuin.ddd4j.core.RequiresPartialDecryption;
import org.fuin.ddd4j.jsonb.AbstractDomainEvent;
import org.fuin.objects4j.crypto.DecryptionFailedException;
import org.fuin.objects4j.crypto.EncryptedData;
import org.fuin.objects4j.crypto.EncryptedDataService;
import org.fuin.objects4j.crypto.EncryptionKeyIdUnknownException;
import org.fuin.objects4j.crypto.EncryptionKeyVersionUnknownException;
import org.fuin.utils4j.TestOmitted;

import java.io.Serial;

/**
 * Encrypted variant of {@link MyCustomerCreatedEvent} as it is stored at rest. The plain name is restored by replacing it with a
 * {@link MyCustomerCreatedEvent} via {@link #decrypt(ObjectSerDeserializer, EncryptedDataService)}.
 */
@TestOmitted("Only a test class")
@SuppressWarnings("NullAway.Init")
public final class MyCustomerCreatedEventEncrypted extends AbstractDomainEvent<MyCustomerId>
        implements RequiresPartialDecryption<MyCustomerCreatedEventEncrypted, MyCustomerCreatedEvent> {

    @Serial
    private static final long serialVersionUID = 1000L;

    /** Unique name of the event used to store it - Should never change. */
    public static final EventType TYPE = new EventType(MyCustomerCreatedEventEncrypted.class.getSimpleName());

    private MyCustomerId customerId;

    private EncryptedData encryptedName;

    /**
     * Default constructor only for deserialization.
     */
    protected MyCustomerCreatedEventEncrypted() {
        super();
    }

    /**
     * Constructor with event data.
     *
     * @param customerId    Identifier of the customer.
     * @param encryptedName Encrypted customer name.
     */
    public MyCustomerCreatedEventEncrypted(final MyCustomerId customerId, final EncryptedData encryptedName) {
        super(new EntityIdPath(customerId));
        this.customerId = customerId;
        this.encryptedName = encryptedName;
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
     * Returns the encrypted customer name.
     *
     * @return Encrypted name.
     */
    public EncryptedData getEncryptedName() {
        return encryptedName;
    }

    @Override
    public MyCustomerCreatedEvent decrypt(final ObjectSerDeserializer serDeserializer, final EncryptedDataService service) {
        try {
            final byte[] clear = service.decrypt(encryptedName);
            final String name = serDeserializer.deserialize(clear, String.class);
            return new MyCustomerCreatedEvent(customerId, name);
        } catch (final EncryptionKeyIdUnknownException | EncryptionKeyVersionUnknownException | DecryptionFailedException ex) {
            // The data can no longer be decrypted (for example the key was destroyed / crypto-shredded):
            // return a redacted name instead of failing.
            return new MyCustomerCreatedEvent(customerId, "***");
        }
    }

}
