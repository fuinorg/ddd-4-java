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

import jakarta.validation.constraints.NotNull;
import org.fuin.objects4j.common.Contract;
import org.fuin.utils4j.TestOmitted;

import java.io.Serializable;

/**
 * Composite primary key for the {@link VendorEvent} table (vendor id + event number).
 */
@TestOmitted("Only a test fixture")
public class VendorEventPrimaryKey implements Serializable {

    private static final long serialVersionUID = 1000L;

    private String vendorId;

    private Long eventNumber;

    /**
     * Default constructor for JPA.
     */
    public VendorEventPrimaryKey() {
        super();
    }

    /**
     * Constructor with all required data.
     *
     * @param vendorId    Unique vendor identifier.
     * @param eventNumber Number of the event within the stream.
     */
    public VendorEventPrimaryKey(@NotNull final String vendorId, @NotNull final Long eventNumber) {
        super();
        Contract.requireArgNotNull("vendorId", vendorId);
        Contract.requireArgNotNull("eventNumber", eventNumber);
        this.vendorId = vendorId;
        this.eventNumber = eventNumber;
    }

    /**
     * Returns the vendor ID.
     *
     * @return Unique vendor identifier.
     */
    @NotNull
    public final String getVendorId() {
        return vendorId;
    }

    /**
     * Returns the number of the event within the stream.
     *
     * @return Order of the event in the stream.
     */
    @NotNull
    public final Long getEventNumber() {
        return eventNumber;
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((vendorId == null) ? 0 : vendorId.hashCode());
        result = prime * result + ((eventNumber == null) ? 0 : eventNumber.hashCode());
        return result;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final VendorEventPrimaryKey other = (VendorEventPrimaryKey) obj;
        if (vendorId == null) {
            if (other.vendorId != null) {
                return false;
            }
        } else if (!vendorId.equals(other.vendorId)) {
            return false;
        }
        if (eventNumber == null) {
            return other.eventNumber == null;
        }
        return eventNumber.equals(other.eventNumber);
    }

    @Override
    public final String toString() {
        return vendorId + "-" + eventNumber;
    }

}
