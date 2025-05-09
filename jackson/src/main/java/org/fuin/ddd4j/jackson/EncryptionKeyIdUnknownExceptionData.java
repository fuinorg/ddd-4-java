package org.fuin.ddd4j.jackson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import org.fuin.ddd4j.core.EncryptionKeyIdUnknownException;
import org.fuin.ddd4j.core.ExceptionData;

import java.io.Serial;

import static org.fuin.ddd4j.core.EncryptionKeyIdUnknownException.ELEMENT_NAME;

/**
 * Stores the data from a {@link EncryptionKeyIdUnknownException} for marshalling and allows recreating it after unmarshalling.
 * The idea is to transport an exception from the server to the client (without stack trace) and recreate it to be thrown on the client.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EncryptionKeyIdUnknownExceptionData implements ExceptionData<EncryptionKeyIdUnknownException> {

    @Serial
    private static final long serialVersionUID = 1000L;

    @JsonProperty("msg")
    private String message;

    @JsonProperty("sid")
    private String sid;

    @JsonProperty("key-id")
    private String keyId;

    /**
     * Constructor only for marshalling/unmarshalling.
     */
    protected EncryptionKeyIdUnknownExceptionData() {
        super();
    }

    /**
     * Constructor with all data.
     *
     * @param ex Exception to copy data from.
     */
    public EncryptionKeyIdUnknownExceptionData(@NotNull final EncryptionKeyIdUnknownException ex) {
        super();
        this.message = ex.getMessage();
        this.sid = ex.getShortId();
        this.keyId = ex.getKeyId();
    }

    @Override
    @JsonIgnore
    public String getDataElement() {
        return ELEMENT_NAME;
    }

    /**
     * Returns the exception message.
     *
     * @return Message.
     */
    @JsonIgnore
    public final String getMessage() {
        return message;
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
        EncryptionKeyIdUnknownExceptionData other = (EncryptionKeyIdUnknownExceptionData) obj;
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
        return "EncryptionKeyIdUnknownExceptionData [message=" + message + ", sid=" + sid + ", keyId=" + keyId + "]";
    }

    @Override
    public EncryptionKeyIdUnknownException toException() {
        return new EncryptionKeyIdUnknownException(keyId);
    }

}
