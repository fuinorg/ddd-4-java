package org.fuin.ddd4j.jsonb;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.validation.constraints.NotNull;
import org.fuin.ddd4j.core.DecryptionFailedException;
import org.fuin.ddd4j.core.ExceptionData;
import org.fuin.objects4j.common.ToExceptionCapable;
import org.fuin.objects4j.common.ValueObject;

import java.io.Serial;
import java.io.Serializable;

import static org.fuin.ddd4j.core.DecryptionFailedException.ELEMENT_NAME;

/**
 * Stores the data from a {@link DecryptionFailedException} for marshalling and allows recreating it after unmarshalling.
 * The idea is to transport an exception from the server to the client (without stack trace) and recreate it to be thrown on the client.
 */
public class DecryptionFailedExceptionData implements ExceptionData<DecryptionFailedException> {

    @Serial
    private static final long serialVersionUID = 1000L;

    @JsonbProperty("msg")
    private String message;

    @JsonbProperty("sid")
    private String sid;

    /**
     * Constructor only for marshalling/unmarshalling.
     */
    protected DecryptionFailedExceptionData() {
        super();
    }

    /**
     * Constructor with all data.
     *
     * @param ex Exception to copy data from.
     */
    public DecryptionFailedExceptionData(@NotNull final DecryptionFailedException ex) {
        super();
        this.message = ex.getMessage();
        this.sid = ex.getShortId();
    }

    @Override
    public String getDataElement() {
        return ELEMENT_NAME;
    }

    /**
     * Returns the exception message.
     *
     * @return Message.
     */
    public final String getMessage() {
        return message;
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
        result = prime * result + ((message == null) ? 0 : message.hashCode());
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
        DecryptionFailedExceptionData other = (DecryptionFailedExceptionData) obj;
        if (message == null) {
            if (other.message != null)
                return false;
        } else if (!message.equals(other.message))
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
        return "DecryptionFailedExceptionData [message=" + message + ", sid=" + sid + "]";
    }

    @Override
    public DecryptionFailedException toException() {
        return new DecryptionFailedException(message);
    }

}
