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
package org.fuin.ddd4j.test;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.fuin.esc.jpa.JpaEvent;
import org.fuin.esc.jpa.JpaStream;
import org.fuin.esc.jpa.JpaStreamEvent;
import org.fuin.objects4j.common.Contract;

/**
 * Vendor stream.
 */
@Table(name = "VENDOR_STREAMS")
@Entity
public class VendorStream extends JpaStream {

    @Id
    @NotNull
    @Column(name = "VENDOR_ID", nullable = false, updatable = false, length = 36)
    private String vendorId;

    private transient VendorId id;

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
        super();
        Contract.requireArgNotNull("vendorId", vendorId);
        this.vendorId = vendorId.asString();
        this.id = vendorId;
    }

    /**
     * Returns the unique vendor identifier as string.
     * 
     * @return Vendor identifier.
     */
    public final String getVendorId() {
        return vendorId;
    }

    /**
     * Returns the vendor identifier.
     * 
     * @return Name converted into a vendor ID.
     */
    public final VendorId getId() {
        if (id == null) {
            id = VendorId.valueOf(vendorId);
        }
        return id;
    }

    /**
     * Creates a container that stores the given event entry.
     * 
     * @param jpaEvent
     *            Event entry to convert into a JPA variant.
     * 
     * @return JPA entity.
     */
    public final JpaStreamEvent createEvent(@NotNull final JpaEvent jpaEvent) {
        incVersion();
        return new VendorEvent(getId(), getVersion(), jpaEvent);
    }

    @Override
    public final String toString() {
        return vendorId;
    }

}
