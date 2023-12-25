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
package org.fuin.ddd4j.esrepo;

import org.fuin.ddd4j.ddd.DomainEvent;
import org.fuin.ddd4j.ddd.EntityType;
import org.fuin.ddd4j.test.PersonCreatedEvent;
import org.fuin.ddd4j.test.PersonNameChangedEvent;
import org.fuin.ddd4j.test.Vendor;
import org.fuin.ddd4j.test.VendorId;
import org.fuin.esc.api.EventStore;

import java.util.List;

/**
 * Implements a repository that is capable of storing vendors.
 */
public final class VendorRepository extends EventStoreRepository<VendorId, Vendor> {

    /**
     * Constructor all mandatory data.
     * 
     * @param eventStore
     *            Event store.
     */
    public VendorRepository(final EventStore eventStore) {
        super(eventStore);
    }

    @Override
    public Class<Vendor> getAggregateClass() {
        return Vendor.class;
    }

    @Override
    public final EntityType getAggregateType() {
        return VendorId.ENTITY_TYPE;
    }

    @Override
    public final Vendor create() {
        return new Vendor();
    }

    @Override
    protected final String getIdParamName() {
        return "vendorId";
    }

    @Override
    protected final boolean conflictsResolved(final List<DomainEvent<?>> uncommittedChanges, final List<DomainEvent<?>> unseenEvents) {

        // Example code allows only "PersonCreatedEvent" and
        // "PersonNameChangedEvent" in parallel
        for (final DomainEvent<?> uncommitedEvent : uncommittedChanges) {
            for (final DomainEvent<?> unseenEvent : unseenEvents) {
                if (!((uncommitedEvent instanceof PersonNameChangedEvent) && (unseenEvent instanceof PersonCreatedEvent))) {
                    return false;
                }
            }
        }

        return true;

    }

}
