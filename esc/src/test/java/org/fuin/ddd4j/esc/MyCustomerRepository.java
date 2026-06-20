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

import org.fuin.ddd4j.core.EntityType;
import org.fuin.ddd4j.core.ObjectSerDeserializer;
import org.fuin.esc.api.EventStore;
import org.fuin.objects4j.crypto.EncryptedDataService;
import org.fuin.utils4j.TestOmitted;

/**
 * Repository for {@link MyCustomer} aggregates with partial encryption support.
 */
@TestOmitted("Only a test class")
public final class MyCustomerRepository extends EventStoreRepository<MyCustomerId, MyCustomer> {

    /**
     * Constructor with all mandatory data.
     *
     * @param eventStore           Event store.
     * @param serDeserializer      Serializes/deserializes the encrypted fields.
     * @param encryptedDataService Performs the actual encryption/decryption.
     */
    public MyCustomerRepository(final EventStore eventStore, final ObjectSerDeserializer serDeserializer,
                               final EncryptedDataService encryptedDataService) {
        super(eventStore, serDeserializer, encryptedDataService);
    }

    @Override
    public Class<MyCustomer> getAggregateClass() {
        return MyCustomer.class;
    }

    @Override
    public EntityType getAggregateType() {
        return MyCustomerId.TYPE;
    }

    @Override
    public MyCustomer create() {
        return new MyCustomer();
    }

    @Override
    protected String getIdParamName() {
        return "customerId";
    }

}
