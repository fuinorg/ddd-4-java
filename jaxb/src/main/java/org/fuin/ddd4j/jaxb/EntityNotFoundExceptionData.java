package org.fuin.ddd4j.jaxb;

import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.fuin.ddd4j.core.EntityNotFoundException;
import org.fuin.ddd4j.core.ExceptionData;

import static org.fuin.ddd4j.core.EntityNotFoundException.ELEMENT_NAME;

/**
 * Stores the data from a {@link EntityNotFoundException} for marshalling and allows recreating it after unmarshalling.
 * The idea is to transport an exception from the server to the client (without stack trace) and recreate it to be thrown on the client.
 */
@XmlRootElement(name = ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
public class EntityNotFoundExceptionData implements ExceptionData<EntityNotFoundException> {

    private static final long serialVersionUID = 1000L;

    @XmlElement(name = "msg")
    private String message;

    @XmlElement(name = "sid")
    private String sid;

    @XmlElement(name = "parent-id-path")
    private String parentIdPath;

    @XmlElement(name = "entity-id")
    private String entityId;

    /**
     * Constructor only for marshalling/unmarshalling.
     */
    protected EntityNotFoundExceptionData() {
        super();
    }

    /**
     * Constructor with all data.
     *
     * @param ex Exception to copy data from.
     */
    public EntityNotFoundExceptionData(@NotNull final EntityNotFoundException ex) {
        super();
        this.message = ex.getMessage();
        this.sid = ex.getShortId();
        this.parentIdPath = ex.getParentIdPath();
        this.entityId = ex.getEntityId();
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
     * Returns the path of parent (without entity ID itself).
     *
     * @return Path.
     */
    public String getParentIdPath() {
        return parentIdPath;
    }

    /**
     * Returns the unique identifier of the entity.
     *
     * @return Entity ID.
     */
    public String getEntityId() {
        return entityId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((entityId == null) ? 0 : entityId.hashCode());
        result = prime * result + ((message == null) ? 0 : message.hashCode());
        result = prime * result + ((parentIdPath == null) ? 0 : parentIdPath.hashCode());
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
        EntityNotFoundExceptionData other = (EntityNotFoundExceptionData) obj;
        if (entityId == null) {
            if (other.entityId != null)
                return false;
        } else if (!entityId.equals(other.entityId))
            return false;
        if (message == null) {
            if (other.message != null)
                return false;
        } else if (!message.equals(other.message))
            return false;
        if (parentIdPath == null) {
            if (other.parentIdPath != null)
                return false;
        } else if (!parentIdPath.equals(other.parentIdPath))
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
        return "EntityNotFoundExceptionData [message=" + message + ", sid=" + sid + ", parentIdPath=" + parentIdPath + ", entityId=" + entityId + "]";
    }

    @Override
    public EntityNotFoundException toException() {
        return new EntityNotFoundException(parentIdPath, entityId);
    }

}
