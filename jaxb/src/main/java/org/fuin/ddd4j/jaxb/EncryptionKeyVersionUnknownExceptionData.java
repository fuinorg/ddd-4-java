package org.fuin.ddd4j.jaxb;

import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.fuin.ddd4j.core.EncryptionKeyVersionUnknownException;
import org.fuin.ddd4j.core.ExceptionData;

import static org.fuin.ddd4j.core.EncryptionKeyVersionUnknownException.ELEMENT_NAME;

/**
 * Stores the data from a {@link EncryptionKeyVersionUnknownException} for marshalling and allows recreating it after unmarshalling.
 * The idea is to transport an exception from the server to the client (without stack trace) and recreate it to be thrown on the client.
 */
@XmlRootElement(name = ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
public class EncryptionKeyVersionUnknownExceptionData implements ExceptionData<EncryptionKeyVersionUnknownException> {

    private static final long serialVersionUID = 1000L;

    @XmlElement(name = "msg")
    private String message;

    @XmlElement(name = "sid")
    private String sid;

    @XmlElement(name = "key-version")
    private String keyVersion;

    /**
     * Constructor only for marshalling/unmarshalling.
     */
    protected EncryptionKeyVersionUnknownExceptionData() {
        super();
    }

    /**
     * Constructor with all data.
     *
     * @param ex Exception to copy data from.
     */
    public EncryptionKeyVersionUnknownExceptionData(@NotNull final EncryptionKeyVersionUnknownException ex) {
        super();
        this.message = ex.getMessage();
        this.sid = ex.getShortId();
        this.keyVersion = ex.getKeyVersion();
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

    /**
     * Returns the key version that caused the problem.
     *
     * @return Key version.
     */
    public String getKeyVersion() {
        return keyVersion;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((message == null) ? 0 : message.hashCode());
        result = prime * result + ((sid == null) ? 0 : sid.hashCode());
        result = prime * result + ((keyVersion == null) ? 0 : keyVersion.hashCode());
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
        EncryptionKeyVersionUnknownExceptionData other = (EncryptionKeyVersionUnknownExceptionData) obj;
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
        if (keyVersion == null) {
            if (other.keyVersion != null)
                return false;
        } else if (!keyVersion.equals(other.keyVersion))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "EncryptionKeyVersionUnknownExceptionData [message=" + message + ", sid=" + sid + ", keyVersion=" + keyVersion + "]";
    }

    @Override
    public EncryptionKeyVersionUnknownException toException() {
        return new EncryptionKeyVersionUnknownException(keyVersion);
    }

}
