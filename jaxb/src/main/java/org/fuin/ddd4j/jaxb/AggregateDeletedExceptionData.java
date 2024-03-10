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
package org.fuin.ddd4j.jaxb;

import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.fuin.ddd4j.core.AggregateDeletedException;

import java.io.Serial;

import static org.fuin.ddd4j.core.AggregateDeletedException.ELEMENT_NAME;

/**
 * Stores the data from a {@link AggregateDeletedException} for marshalling and allows recreating it after unmarshalling.
 * The idea is to transport an exception from the server to the client (without stack trace) and recreate it to be thrown on the client.
 */
@XmlRootElement(name = ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
public final class AggregateDeletedExceptionData extends AbstractAggregateExceptionData<AggregateDeletedException> {

    @Serial
    private static final long serialVersionUID = 1000L;

    @XmlElement(name = "sid")
    private String sid;

    /**
     * Constructor only for marshalling/unmarshalling.
     */
    protected AggregateDeletedExceptionData() {
        super();
    }

    /**
     * Constructor with all data.
     *
     * @param ex Exception to copy data from.
     */
    public AggregateDeletedExceptionData(@NotNull final AggregateDeletedException ex) {
        super(ex);
        this.sid = ex.getShortId();
    }

    @Override
    public String getDataElement() {
        return ELEMENT_NAME;
    }

    /**
     * Returns the unique short identifier of the contained exception.
     *
     * @return Unique and human readable identifier.
     */
    public String getShortId() {
        return sid;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getAggregateId() == null) ? 0 : getAggregateId().hashCode());
        result = prime * result + ((getAggregateType() == null) ? 0 : getAggregateType().hashCode());
        result = prime * result + ((getMessage() == null) ? 0 : getMessage().hashCode());
        result = prime * result + ((sid == null) ? 0 : sid.hashCode());
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
        AggregateDeletedExceptionData other = (AggregateDeletedExceptionData) obj;
        if (getAggregateId() == null) {
            if (other.getAggregateId() != null)
                return false;
        } else if (!getAggregateId().equals(other.getAggregateId()))
            return false;
        if (getAggregateType() == null) {
            if (other.getAggregateType() != null)
                return false;
        } else if (!getAggregateType().equals(other.getAggregateType()))
            return false;
        if (getMessage() == null) {
            if (other.getMessage() != null)
                return false;
        } else if (!getMessage().equals(other.getMessage()))
            return false;
        if (sid == null) {
            if (other.sid != null)
                return false;
        } else if (!sid.equals(other.sid))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "AggregateDeletedExceptionData [getMessage()=" + getMessage() + ", sid=" + sid + ", getAggregateType()=" + getAggregateType() + ", getAggregateId()=" + getAggregateId() + "]";
    }

    @Override
    public AggregateDeletedException toException() {
        return new AggregateDeletedException(getAggregateType(), getAggregateId());
    }

}

