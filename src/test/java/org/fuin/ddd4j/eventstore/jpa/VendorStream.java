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
package org.fuin.ddd4j.eventstore.jpa;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.fuin.ddd4j.test.VendorId;
import org.fuin.ddd4j.test.VendorIdConverter;

/**
 * Represents a stream of events for a single vendor.
 */
@Table(name = "VENDOR_STREAMS", indexes = { @Index(columnList = "VENDOR_ID") })
@Entity
public class VendorStream extends Stream {

	@Id
	@NotNull
	@Column(name = "ID", length = 100, nullable = false, columnDefinition = "VARCHAR(100)")
	private String id;

	@NotNull
	@Convert(converter = VendorIdConverter.class)
	@Column(name = "VENDOR_ID", length = 100, nullable = false)
	private VendorId vendorId;

	/**
	 * Protected default constructor only required for JPA.
	 */
	protected VendorStream() {
		super();
	}

	/**
	 * Constructs a new vendor stream.
	 * 
	 * @param vendorId
	 *            Vendor ID to create a vendor stream for.
	 */
	public VendorStream(final VendorId vendorId) {
		super();
		this.id = vendorId.asString();
		this.vendorId = vendorId;
	}

	/**
	 * Returns the vendor identifier.
	 * 
	 * @return Unique vendor identifier.
	 */
	public final VendorId getVendorId() {
		return vendorId;
	}

	@Override
	public StreamEvent createEvent(final EventEntry eventEntry) {
		incVersion();
		return new VendorEvent(vendorId, getVersion(), eventEntry);
	}

}
