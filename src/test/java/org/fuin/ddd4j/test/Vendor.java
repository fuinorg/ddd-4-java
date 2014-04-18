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

import org.fuin.ddd4j.ddd.AbstractAggregateRoot;
import org.fuin.ddd4j.ddd.EntityType;
import org.fuin.ddd4j.ddd.EventHandler;

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
	 */
	public Vendor(final VendorId id, final VendorKey key, final VendorName name) {
		super();
		apply(new VendorCreatedEvent(new VendorRef(id, key, name)));
	}

	@Override
	public final EntityType getType() {
		return VendorId.ENTITY_TYPE;
	}

	@Override
	public final VendorId getId() {
		return id;
	}

	@EventHandler
	private final void handle(final VendorCreatedEvent event) {
		this.id = event.getEntityId();
	}

}
