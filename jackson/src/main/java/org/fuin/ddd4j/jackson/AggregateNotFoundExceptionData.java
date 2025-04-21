package org.fuin.ddd4j.jackson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.fuin.ddd4j.core.AggregateNotFoundException;

import java.io.Serial;

import static org.fuin.ddd4j.core.AggregateNotFoundException.ELEMENT_NAME;

/**
 * Stores the data from a {@link AggregateNotFoundException} for marshalling and allows recreating it after unmarshalling.
 * The idea is to transport an exception from the server to the client (without stack trace) and recreate it to be thrown on the client.
 */
public final class AggregateNotFoundExceptionData extends AbstractAggregateExceptionData<AggregateNotFoundException> {

    @Serial
    private static final long serialVersionUID = 1000L;

    @JsonProperty("sid")
    private String sid;

    /**
     * Constructor only for marshalling/unmarshalling.
     */
    protected AggregateNotFoundExceptionData() {
        super();
    }

    /**
     * Constructor with all data.
     *
     * @param ex Exception to copy data from.
     */
    public AggregateNotFoundExceptionData(final AggregateNotFoundException ex) {
        super(ex);
        this.sid = ex.getShortId();
    }

    @Override
    @JsonIgnore
    public String getDataElement() {
        return ELEMENT_NAME;
    }

    /**
     * Returns the unique short identifier of the contained exception.
     *
     * @return Unique and human readable identifier.
     */
    @JsonIgnore
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
        AggregateNotFoundExceptionData other = (AggregateNotFoundExceptionData) obj;
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
        return "AggregateNotFoundExceptionData [getMessage()=" + getMessage() + ", sid=" + sid + ", getAggregateType()=" + getAggregateType() + ", getAggregateId()=" + getAggregateId() + "]";
    }

    @Override
    public AggregateNotFoundException toException() {
        return new AggregateNotFoundException(getAggregateType(), getAggregateId());
    }

}
