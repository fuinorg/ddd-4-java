package org.fuin.ddd4j.jsonb;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.validation.constraints.NotNull;
import org.fuin.ddd4j.core.DuplicateEncryptionKeyIdException;
import org.fuin.ddd4j.core.ExceptionData;
import org.fuin.objects4j.common.ToExceptionCapable;
import org.fuin.objects4j.common.ValueObject;

import java.io.Serializable;

import static org.fuin.ddd4j.core.DuplicateEncryptionKeyIdException.ELEMENT_NAME;

/**
 * Stores the data from a {@link DuplicateEncryptionKeyIdException} for marshalling and allows recreating it after unmarshalling.
 * The idea is to transport an exception from the server to the client (without stack trace) and recreate it to be thrown on the client.
 */
public class DuplicateEncryptionKeyIdExceptionData implements ExceptionData<DuplicateEncryptionKeyIdException> {

    private static final long serialVersionUID = 1000L;

    @JsonbProperty("msg")
    private String message;

    @JsonbProperty("sid")
    private String sid;

    @JsonbProperty("key-id")
    private String keyId;

    /**
     * Constructor only for marshalling/unmarshalling.
     */
    protected DuplicateEncryptionKeyIdExceptionData() {
        super();
    }

    /**
     * Constructor with all data.
     *
     * @param ex Exception to copy data from.
     */
    public DuplicateEncryptionKeyIdExceptionData(@NotNull final DuplicateEncryptionKeyIdException ex) {
        super();
        this.message = ex.getMessage();
        this.sid = ex.getShortId();
        this.keyId = ex.getKeyId();
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
    public String getMessage() {
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
        result = prime * result + ((keyId == null) ? 0 : keyId.hashCode());
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
        DuplicateEncryptionKeyIdExceptionData other = (DuplicateEncryptionKeyIdExceptionData) obj;
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
        if (keyId == null) {
            if (other.keyId != null)
                return false;
        } else if (!keyId.equals(other.keyId))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "DuplicateEncryptionKeyIdExceptionData [message=" + message + ", sid=" + sid + ", keyId=" + keyId + "]";
    }

    @Override
    public DuplicateEncryptionKeyIdException toException() {
        return new DuplicateEncryptionKeyIdException(keyId);
    }

}