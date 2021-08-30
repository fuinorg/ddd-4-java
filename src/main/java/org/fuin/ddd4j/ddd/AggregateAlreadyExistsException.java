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

import jakarta.json.bind.annotation.JsonbProperty;
import javax.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.common.ExceptionShortIdentifable;
import org.fuin.objects4j.common.MarshalInformation;
import org.fuin.objects4j.common.ToExceptionCapable;
import org.fuin.objects4j.vo.ValueObject;

/**
 * An aggregate already exists when trying to create it.
 */
public final class AggregateAlreadyExistsException extends Exception
        implements ExceptionShortIdentifable, MarshalInformation<AggregateAlreadyExistsException.Data> {

    private static final long serialVersionUID = 1L;

    /** Unique short identifier of this exception. */
    public static final String SHORT_ID = SHORT_ID_PREFIX + "-AGGREGATE_ALREADY_EXISTS";

    /** Unique name of the element to use for XML and JSON marshalling/unmarshalling. */
    public static final String ELEMENT_NAME = "aggregate-already-exists-exception";

    private final Data data;

    /**
     * Constructor with all data.
     * 
     * @param aggregateType
     *            Type of the aggregate.
     * @param aggregateId
     *            Unique identifier of the aggregate.
     * @param version
     *            Actual version.
     */
    public AggregateAlreadyExistsException(@NotNull final EntityType aggregateType, @NotNull final AggregateRootId aggregateId,
            final int version) {
        super(aggregateType.asString() + " " + aggregateId.asString() + " already exists (version=" + version + ")");
        Contract.requireArgNotNull("aggregateType", aggregateType);
        Contract.requireArgNotNull("aggregateId", aggregateId);
        this.data = new Data(this.getMessage(), SHORT_ID, aggregateType.asString(), aggregateId.asString(), version);
    }

    /**
     * Constructor used by the {@link Data} class.
     * 
     * @param data
     *            Data to use for reconstructing the exception.
     */
    private AggregateAlreadyExistsException(final Data data) {
        super(data.message);
        this.data = data;
    }

    @Override
    public final String getShortId() {
        return data.sid;
    }

    /**
     * Returns the type of the aggregate.
     * 
     * @return Type.
     */
    @NotNull
    public final String getAggregateType() {
        return data.aggregateType;
    }

    /**
     * Returns the unique identifier of the aggregate.
     * 
     * @return Stream with version conflict.
     */
    @NotNull
    public final String getAggregateId() {
        return data.aggregateId;
    }

    /**
     * Returns the actual version.
     * 
     * @return Actual version.
     */
    public final int getVersion() {
        return data.version;
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
    public static final class Data implements Serializable, ValueObject, ToExceptionCapable<AggregateAlreadyExistsException> {

        private static final long serialVersionUID = 1000L;

        @JsonbProperty("msg")
        @XmlElement(name = "msg")
        private String message;

        @JsonbProperty("sid")
        @XmlElement(name = "sid")
        private String sid;

        @JsonbProperty("aggregate-type")
        @XmlElement(name = "aggregate-type")
        private String aggregateType;

        @JsonbProperty("aggregate-id")
        @XmlElement(name = "aggregate-id")
        private String aggregateId;

        @JsonbProperty("version")
        @XmlElement(name = "version")
        private int version;

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
         * @param version
         *            Actual version.
         */
        private Data(final String message, final String sid, final String aggregateType, final String aggregateId, final int version) {
            super();
            this.message = message;
            this.sid = sid;
            this.aggregateType = aggregateType;
            this.aggregateId = aggregateId;
            this.version = version;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((aggregateId == null) ? 0 : aggregateId.hashCode());
            result = prime * result + ((aggregateType == null) ? 0 : aggregateType.hashCode());
            result = prime * result + ((message == null) ? 0 : message.hashCode());
            result = prime * result + ((sid == null) ? 0 : sid.hashCode());
            result = prime * result + version;
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
            if (aggregateId == null) {
                if (other.aggregateId != null)
                    return false;
            } else if (!aggregateId.equals(other.aggregateId))
                return false;
            if (aggregateType == null) {
                if (other.aggregateType != null)
                    return false;
            } else if (!aggregateType.equals(other.aggregateType))
                return false;
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
            if (version != other.version)
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "Data [message=" + message + ", sid=" + sid + ", aggregateType=" + aggregateType + ", aggregateId=" + aggregateId
                    + ", version=" + version + "]";
        }

        public AggregateAlreadyExistsException toException() {
            return new AggregateAlreadyExistsException(this);
        }

    }

}
