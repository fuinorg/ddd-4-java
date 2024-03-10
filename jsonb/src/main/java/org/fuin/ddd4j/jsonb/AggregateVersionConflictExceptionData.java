package org.fuin.ddd4j.jsonb;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.validation.constraints.NotNull;
import org.fuin.ddd4j.core.AggregateVersionConflictException;

import java.io.Serial;

import static org.fuin.ddd4j.core.AggregateVersionConflictException.ELEMENT_NAME;

/**
 * Stores the data from a {@link AggregateVersionConflictException} for marshalling and allows recreating it after unmarshalling.
 * The idea is to transport an exception from the server to the client (without stack trace) and recreate it to be thrown on the client.
 */
public final class AggregateVersionConflictExceptionData extends AbstractAggregateExceptionData<AggregateVersionConflictException> {

    @Serial
    private static final long serialVersionUID = 1000L;

    @JsonbProperty("sid")
    private String sid;

    @JsonbProperty("expected-version")
    private int expected;

    @JsonbProperty("actual-version")
    private int actual;

    /**
     * Constructor only for marshalling/unmarshalling.
     */
    protected AggregateVersionConflictExceptionData() {
        super();
    }

    /**
     * Constructor with all data.
     *
     * @param ex Exception to copy data from.
     */
    public AggregateVersionConflictExceptionData(@NotNull final AggregateVersionConflictException ex) {
        super(ex);
        this.sid = ex.getShortId();
        this.expected = ex.getExpected();
        this.actual = ex.getActual();
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
        result = prime * result + actual;
        result = prime * result + ((getAggregateId() == null) ? 0 : getAggregateId().hashCode());
        result = prime * result + ((getAggregateType() == null) ? 0 : getAggregateType().hashCode());
        result = prime * result + expected;
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
        AggregateVersionConflictExceptionData other = (AggregateVersionConflictExceptionData) obj;
        if (actual != other.actual)
            return false;
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
        if (expected != other.expected)
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
        return "AggregateVersionConflictExceptionData [getMessage()=" + getMessage() + ", sid=" + sid + ", getAggregateType()=" + getAggregateType() + ", getAggregateId()=" + getAggregateId()
                + ", expected=" + expected + ", actual=" + actual + "]";
    }

    @Override
    public AggregateVersionConflictException toException() {
        return new AggregateVersionConflictException(getAggregateType(), getAggregateId(), expected, actual);
    }

}
