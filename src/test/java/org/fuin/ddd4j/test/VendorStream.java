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

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.fuin.ddd4j.eventstore.jpa.EventEntry;
import org.fuin.ddd4j.eventstore.jpa.Stream;
import org.fuin.ddd4j.eventstore.jpa.StreamEvent;
import org.fuin.objects4j.common.Contract;

/**
 * Vendor stream.
 */
@Table(name = "VENDOR_STREAMS")
@Entity
public class VendorStream extends Stream {

    @Id
    @NotNull
    @Convert(converter = VendorIdConverter.class)
    @Column(name = "VENDOR_ID", nullable = false, updatable = false, length = 250)
    private VendorId vendorId;

    /**
     * Protected default constructor for JPA.
     */
    protected VendorStream() {
	super();
    }

    /**
     * Constructor with mandatory data.
     * 
     * @param vendorId
     *            Unique vendor identifier.
     */
    public VendorStream(@NotNull final VendorId vendorId) {
	super(false);
	Contract.requireArgNotNull("vendorId", vendorId);
	this.vendorId = vendorId;
    }

    /**
     * Returns the unique vendor identifier as string.
     * 
     * @return Vendor identifier.
     */
    public final VendorId getVendorId() {
	return vendorId;
    }

    /**
     * Creates a container that stores the given event entry.
     * 
     * @param eventEntry
     *            Event entry to convert into a JPA variaant.
     * 
     * @return JPA entity.
     */
    public final StreamEvent createEvent(@NotNull final EventEntry eventEntry) {
	incVersion();
	return new VendorEvent(vendorId, getVersion(), eventEntry);
    }

    @Override
    public final String toString() {
	return vendorId.asString();
    }

}
