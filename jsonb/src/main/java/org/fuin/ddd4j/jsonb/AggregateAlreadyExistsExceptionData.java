package org.fuin.ddd4j.jsonb;

import jakarta.json.bind.annotation.JsonbProperty;
import org.fuin.ddd4j.core.AggregateAlreadyExistsException;

import java.io.Serial;

import static org.fuin.ddd4j.core.AggregateAlreadyExistsException.ELEMENT_NAME;

/**
 * Stores the data from a {@link AggregateAlreadyExistsException} for marshalling and allows recreating it after unmarshalling.
 * The idea is to transport an exception from the server to the client (without stack trace) and recreate it to be thrown on the client.
 */
public final class AggregateAlreadyExistsExceptionData extends AbstractVersionedAggregateExceptionData<AggregateAlreadyExistsException> {

    @Serial
    private static final long serialVersionUID = 1000L;

    @JsonbProperty("sid")
    private String sid;

    /**
     * Constructor only for marshalling/unmarshalling.
     */
    protected AggregateAlreadyExistsExceptionData() {
        super();
    }

    /**
     * Constructor with exception.
     *
     * @param ex Exception to copy data from.
     */
    public AggregateAlreadyExistsExceptionData(final AggregateAlreadyExistsException ex) {
        super(ex);
        this.sid = ex.getShortId();
    }

    @Override
    public String getDataElement() {
        return ELEMENT_NAME;
    }

    /**
     * Returns the unique short identifier od the exception.
     *
     * @return Unique and human readable identifer.
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
        result = prime * result + getVersion();
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
        AggregateAlreadyExistsExceptionData other = (AggregateAlreadyExistsExceptionData) obj;
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
        return getVersion() == other.getVersion();
    }

    @Override
    public String toString() {
        return "Data [message=" + getMessage() + ", sid=" + sid + ", aggregateType=" + getAggregateType()
                + ", aggregateId=" + getAggregateId() + ", version=" + getVersion() + "]";
    }

    @Override
    public AggregateAlreadyExistsException toException() {
        return new AggregateAlreadyExistsException(getAggregateType(), getAggregateId(), getVersion());
    }

}
