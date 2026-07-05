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

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import org.fuin.esc.jpa.JpaEvent;
import org.fuin.esc.jpa.JpaStreamEvent;
import org.fuin.objects4j.common.Contract;
import org.fuin.utils4j.TestOmitted;

/**
 * Database table for an event of the vendor aggregate stream.
 */
@TestOmitted("Only a test fixture")
@Table(name = "VENDOR_EVENTS")
@Entity
@IdClass(VendorEventPrimaryKey.class)
public class VendorEvent extends JpaStreamEvent {

    @Id
    @NotNull
    @Column(name = "VENDOR_ID")
    private String vendorId;

    @Id
    @NotNull
    @Column(name = "EVENT_NUMBER")
    private Long eventNumber;

    /**
     * Protected default constructor only required for JPA.
     */
    protected VendorEvent() {
        super();
    }

    /**
     * Constructor with all mandatory data.
     *
     * @param vendorId   Unique vendor identifier.
     * @param version    Version.
     * @param eventEntry Event entry to connect.
     */
    public VendorEvent(@NotNull final String vendorId, @NotNull final Long version, final JpaEvent eventEntry) {
        super(eventEntry);
        Contract.requireArgNotNull("vendorId", vendorId);
        Contract.requireArgNotNull("version", version);
        this.vendorId = vendorId;
        this.eventNumber = version;
    }

    /**
     * Returns the unique vendor identifier.
     *
     * @return Vendor identifier.
     */
    public final String getVendorId() {
        return vendorId;
    }

    /**
     * Returns the number of the event within the stream.
     *
     * @return Order of the event in the stream.
     */
    public final Long getEventNumber() {
        return eventNumber;
    }

}
