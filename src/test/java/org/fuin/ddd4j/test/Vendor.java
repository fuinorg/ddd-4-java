/**
 * Copyright (C) 2013 Future Invent Informationsmanagement GmbH. All rights
 * reserved. <http://www.fuin.org/>
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
 * along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fuin.ddd4j.test;

import javax.validation.constraints.NotNull;

import org.fuin.ddd4j.ddd.AbstractAggregateRoot;
import org.fuin.ddd4j.ddd.EntityType;
import org.fuin.ddd4j.ddd.EventHandler;
import org.fuin.objects4j.common.Contract;

/**
 * Vendor aggregate.
 */
public class Vendor extends AbstractAggregateRoot<VendorId> {

    private VendorId id;

    /**
     * Default constructor used by the repositories. NEVER use in your
     * application code!
     */
    public Vendor() {
        super();
    }

    /**
     * Constructor with all data.
     * 
     * @param id
     *            Unique identifier.
     * @param key
     *            Unique key.
     * @param name
     *            Name.
     * @param service
     *            Service used by the method.
     * 
     * @throws DuplicateVendorKeyException
     *             The given key already exists.
     */
    public Vendor(@NotNull final VendorId id, @NotNull final VendorKey key,
            @NotNull final VendorName name,
            @NotNull final ConstructorService service)
            throws DuplicateVendorKeyException {
        super();

        // CHECK PRECONDITIONS
        Contract.requireArgNotNull("id", id);
        Contract.requireArgNotNull("key", key);
        Contract.requireArgNotNull("name", name);
        Contract.requireArgNotNull("service", service);

        // VERIFY BUSINESS RULES
        service.addVendorKey(key);

        // HANDLE EVENT
        apply(new VendorCreatedEvent(new VendorRef(id, key, name)));
    }

    @EventHandler
    private final void handle(final VendorCreatedEvent event) {
        this.id = event.getEntityId();
    }

    @Override
    public final EntityType getType() {
        return VendorId.ENTITY_TYPE;
    }

    @Override
    public final VendorId getId() {
        return id;
    }

    /**
     * Interface for the constructor.
     */
    public interface ConstructorService {

        /**
         * Adds a new vendor key to the context. The key will be persisted or an
         * exception will be thrown if it already exists.
         * 
         * @param key
         *            Key to verify and persist.
         * 
         * @throws DuplicateVendorKeyException
         *             The given key already exists.
         */
        public void addVendorKey(VendorKey key)
                throws DuplicateVendorKeyException;

    }

}
