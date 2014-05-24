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

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.common.NeverNull;

/**
 * Identifies a stream event based on a string and a number.
 */
public class VendorEventId implements Serializable {

    private static final long serialVersionUID = 1000L;

    private String vendorId;

    private Integer eventNumber;

    /**
     * Default constructor for JPA. <b><i>CAUTION:</i> DO NOT USE IN APPLICATION
     * CODE.</b>
     */
    public VendorEventId() {
        super();
    }

    /**
     * Constructor with all required data.
     * 
     * @param vendorId
     *            Unique name.
     * @param eventNumber
     *            Number of the event within the stream.
     */
    public VendorEventId(@NotNull final VendorId vendorId,
            @NotNull final Integer eventNumber) {
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
    @NeverNull
    public String getVendorId() {
        return vendorId;
    }

    /**
     * Returns the number of the event within the stream.
     * 
     * @return Order of the event in the stream.
     */
    @NeverNull
    public Integer getEventNumber() {
        return eventNumber;
    }

    // CHECKSTYLE:OFF Generated code
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((vendorId == null) ? 0 : vendorId.hashCode());
        result = prime * result
                + ((eventNumber == null) ? 0 : eventNumber.hashCode());
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

    // CHECKSTYLE:ON

    @Override
    public String toString() {
        return vendorId + "-" + eventNumber;
    }

}
