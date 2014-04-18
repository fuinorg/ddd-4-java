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
package org.fuin.ddd4j.esrepo;

import org.fuin.ddd4j.ddd.DeserializerRegistry;
import org.fuin.ddd4j.ddd.EntityType;
import org.fuin.ddd4j.ddd.SerializerRegistry;
import org.fuin.ddd4j.eventstore.intf.EventStore;
import org.fuin.ddd4j.test.Vendor;
import org.fuin.ddd4j.test.VendorId;

/**
 * Implements a repository that is capable of storing vendors.
 */
public final class VendorRepository extends
		EventStoreRepository<VendorId, Vendor> {

	/**
	 * Constructor with event store to use as storage.
	 * 
	 * @param eventStore
	 *            Event store.
	 * @param serRegistry
	 *            Registry used to locate serializers.
	 * @param desRegistry
	 *            Registry used to locate deserializers.
	 */
	public VendorRepository(final EventStore eventStore,
			final SerializerRegistry serRegistry,
			final DeserializerRegistry desRegistry) {
		super(eventStore, serRegistry, desRegistry);
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

}
