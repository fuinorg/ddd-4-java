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
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.fuin.esc.jpa.JpaEvent;
import org.fuin.esc.jpa.JpaStreamEvent;
import org.fuin.objects4j.common.Contract;

/**
 * Vendor event.
 */
@Table(name = "VENDOR_EVENTS")
@Entity
@IdClass(VendorEventId.class)
public class VendorEvent extends JpaStreamEvent {

    @Id
    @NotNull
    @Column(name = "VENDOR_ID")
    private String vendorId;

    @Id
    @NotNull
    @Column(name = "EVENT_NUMBER")
    private Integer eventNumber;

    private transient VendorId id;

    /**
     * Protected default constructor only required for JPA.
     */
    protected VendorEvent() {
        super();
    }

    /**
     * Constructor with all mandatory data.
     * 
     * @param vendorId
     *            Unique vendor identifier.
     * @param version
     *            Version.
     * @param event
     *            Event entry to connect.
     */
    public VendorEvent(@NotNull final VendorId vendorId,
            @NotNull final Integer version, final JpaEvent event) {
        super(event);
        Contract.requireArgNotNull("vendorId", vendorId);
        Contract.requireArgNotNull("version", version);
        this.vendorId = vendorId.asString();
        this.eventNumber = version;
        this.id = vendorId;
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
     * Returns the number of the stream.
     * 
     * @return Number that is unique in combination with the name.
     */
    public final Integer getEventNumber() {
        return eventNumber;
    }

    // CHECKSTYLE:OFF Generated code
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((vendorId == null) ? 0 : vendorId.hashCode());
        result = prime * result
                + ((eventNumber == null) ? 0 : eventNumber.hashCode());
        return result;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        VendorEvent other = (VendorEvent) obj;
        if (vendorId == null) {
            if (other.vendorId != null)
                return false;
        } else if (!vendorId.equals(other.vendorId))
            return false;
        if (eventNumber == null) {
            if (other.eventNumber != null)
                return false;
        } else if (!eventNumber.equals(other.eventNumber))
            return false;
        return true;
    }

    // CHECKSTYLE:ON

    @Override
    public final String toString() {
        return vendorId + "-" + eventNumber;
    }

}
