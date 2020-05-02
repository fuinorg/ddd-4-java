/**
 * Copyright (C) 2015 Michael Schnell. All rights reserved. 
 * http://www.fuin.org/
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
 * along with this library. If not, see http://www.gnu.org/licenses/.
 */
package org.fuin.ddd4j.ddd;

import static org.fuin.ddd4j.ddd.Ddd4JUtils.SHORT_ID_PREFIX;

import java.io.Serializable;

import javax.json.bind.annotation.JsonbProperty;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.fuin.objects4j.common.ExceptionShortIdentifable;
import org.fuin.objects4j.common.MarshalUnmarshalInformation;
import org.fuin.objects4j.vo.ValueObject;

/**
 * Signals that an entity already existed in it's parent.
 */
public final class DuplicateEntityException extends Exception
        implements ExceptionShortIdentifable, MarshalUnmarshalInformation<DuplicateEntityException.Data> {

    private static final long serialVersionUID = 1L;

    /** Unique short identifier of this exception. */
    public static final String SHORT_ID = SHORT_ID_PREFIX + "-DUPLICATE_ENTITY";

    /** Unique name of the element to use for XML and JSON marshalling/unmarshalling. */
    public static final String ELEMENT_NAME = "duplicate-entity-exception";

    private final Data data;

    /**
     * Constructor with all data.
     * 
     * @param parentIdPath
     *            Path from root to parent.
     * @param entityId
     *            Unique identifier of the entity that already existed.
     */
    public DuplicateEntityException(@NotNull final EntityIdPath parentIdPath, @NotNull final EntityId entityId) {
        super(entityId.asTypedString() + " already exists in " + parentIdPath.asString());
        final String pidp = parentIdPath == null ? null : parentIdPath.asString();
        this.data = new Data(getMessage(), SHORT_ID, pidp, entityId.asString());
    }

    /**
     * Constructor with entity identifier path.
     * 
     * @param entityIdPath
     *            Entity identifier path (from root to entity).
     */
    public DuplicateEntityException(@NotNull final EntityIdPath entityIdPath) {
        this(entityIdPath.parent(), entityIdPath.last());
    }

    /**
     * Constructor used by the {@link Data} class.
     * 
     * @param data
     *            Data to use for reconstructing the exception.
     */
    private DuplicateEntityException(final Data data) {
        super(data.message);
        this.data = data;
    }

    @Override
    public final String getShortId() {
        return data.sid;
    }

    /**
     * Returns the path from root to parent.
     * 
     * @return Path.
     */
    @NotNull
    public final String getParentIdPath() {
        return data.parentIdPath;
    }

    /**
     * Returns the unique identifier of the entity.
     * 
     * @return Unknown entity identifier.
     */
    @NotNull
    public final String getEntityId() {
        return data.entityId;
    }

    /**
     * Returns the exception specific data.
     * 
     * @return Data structure that can be marshalled/unmarshalled.
     */
    public final Data getData() {
        return data;
    }

    @Override
    public Class<Data> getDataClass() {
        return Data.class;
    }

    @Override
    public String getDataElement() {
        return ELEMENT_NAME;
    }

    /**
     * Specific exception data.
     */
    @XmlRootElement(name = ELEMENT_NAME)
    @XmlAccessorType(XmlAccessType.NONE)
    public static final class Data implements Serializable, ValueObject {

        private static final long serialVersionUID = 1000L;

        @JsonbProperty("msg")
        @XmlElement(name = "msg")
        private String message;

        @JsonbProperty("sid")
        @XmlElement(name = "sid")
        private String sid;

        @JsonbProperty("parent-id-path")
        @XmlElement(name = "parent-id-path")
        private String parentIdPath;

        @JsonbProperty("entity-id")
        @XmlElement(name = "entity-id")
        private String entityId;

        /**
         * Constructor only for marshalling/unmarshalling.
         */
        protected Data() {
            super();
        }

        /**
         * Constructor with all data.
         * 
         * @param message
         *            Exception message.
         * @param sid
         *            Unique short identifier of this exception.
         * @param aggregateType
         *            Type of the aggregate.
         * @param aggregateId
         *            Unique identifier of the aggregate.
         */
        private Data(final String message, final String sid, final String parentIdPath, final String entityId) {
            super();
            this.message = message;
            this.sid = sid;
            this.parentIdPath = parentIdPath;
            this.entityId = entityId;
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
            Data other = (Data) obj;
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
            return "Data [message=" + message + ", sid=" + sid + ", parentIdPath=" + parentIdPath + ", entityId=" + entityId + "]";
        }

        public DuplicateEntityException toException() {
            return new DuplicateEntityException(this);
        }

    }

}
