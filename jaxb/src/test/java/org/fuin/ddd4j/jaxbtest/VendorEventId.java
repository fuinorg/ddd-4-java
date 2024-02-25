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
package org.fuin.ddd4j.jaxbtest;

import jakarta.validation.constraints.NotNull;
import org.fuin.objects4j.common.Contract;

import java.io.Serial;
import java.io.Serializable;

/**
 * Identifies a stream event based on a string and a number.
 */
public final class VendorEventId implements Serializable {

    @Serial
    private static final long serialVersionUID = 1000L;

    private final String vendorId;

    private final Integer eventNumber;

    /**
     * Constructor with all required data.
     *
     * @param vendorId
     *            Unique name.
     * @param eventNumber
     *            Number of the event within the stream.
     */
    public VendorEventId(@NotNull final VendorId vendorId, @NotNull final Integer eventNumber) {
        super();
        Contract.requireArgNotNull("vendorId", vendorId);
        Contract.requireArgNotNull("nueventNumbermber", eventNumber);
        this.vendorId = vendorId.asString();
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
    public final Integer getEventNumber() {
        return eventNumber;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((vendorId == null) ? 0 : vendorId.hashCode());
        result = prime * result + ((eventNumber == null) ? 0 : eventNumber.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        VendorEventId other = (VendorEventId) obj;
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


    @Override
    public final String toString() {
        return vendorId + "-" + eventNumber;
    }

}
