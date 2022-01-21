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
import javax.validation.constraints.NotEmpty;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.fuin.objects4j.common.ExceptionShortIdentifable;
import org.fuin.objects4j.common.MarshalInformation;
import org.fuin.objects4j.common.ToExceptionCapable;
import org.fuin.objects4j.vo.ValueObject;

/**
 * Signals that the encryption key identifier already exists and cannot be created a second time.
 */
public final class DuplicateEncryptionKeyIdException extends Exception
        implements ExceptionShortIdentifable, MarshalInformation<DuplicateEncryptionKeyIdException.Data> {

    private static final long serialVersionUID = 1L;

    /** Unique short identifier of this exception. */
    public static final String SHORT_ID = SHORT_ID_PREFIX + "-DUPLICATE-ENCRYPTION_KEY_ID";

    /** Unique name of the element to use for XML and JSON marshalling/unmarshalling. */
    public static final String ELEMENT_NAME = "duplicate-encryption-key-id-exception";

    private final Data data;

    /**
     * Constructor with all data.
     * 
     * @param keyId
     *            The key identifier that caused the problem.
     */
    public DuplicateEncryptionKeyIdException(@NotEmpty final String keyId) {
        super("Duplicate keyId: " + keyId);
        this.data = new Data(getMessage(), SHORT_ID, keyId);
    }

    /**
     * Constructor used by the {@link Data} class.
     * 
     * @param data
     *            Data to use for reconstructing the exception.
     */
    private DuplicateEncryptionKeyIdException(final Data data) {
        super(data.message);
        this.data = data;
    }

    @Override
    public final String getShortId() {
        return data.sid;
    }

    /**
     * Returns the IV version that caused the problem.
     * 
     * @return IV version.
     */
    @NotEmpty
    public final String getKeyId() {
        return data.keyId;
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
    public static final class Data implements Serializable, ValueObject, ToExceptionCapable<DuplicateEncryptionKeyIdException> {

        private static final long serialVersionUID = 1000L;

        @JsonbProperty("msg")
        @XmlElement(name = "msg")
        private String message;

        @JsonbProperty("sid")
        @XmlElement(name = "sid")
        private String sid;

        @JsonbProperty("key-id")
        @XmlElement(name = "key-id")
        private String keyId;

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
         * @param keyId
         *            The key identifier that caused the problem.
         */
        private Data(final String message, final String sid, final String keyId) {
            super();
            this.message = message;
            this.sid = sid;
            this.keyId = keyId;
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
            Data other = (Data) obj;
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
            return "Data [message=" + message + ", sid=" + sid + ", keyId=" + keyId + "]";
        }

        public DuplicateEncryptionKeyIdException toException() {
            return new DuplicateEncryptionKeyIdException(this);
        }

    }

}
