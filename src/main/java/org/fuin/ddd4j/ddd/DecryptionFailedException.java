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

import javax.json.bind.annotation.JsonbProperty;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.fuin.objects4j.common.AbstractJaxbMarshallableException;
import org.fuin.objects4j.common.ExceptionShortIdentifable;

/**
 * Signals that decrypting the data failed.
 */
@XmlRootElement(name = "decryption-failed-exception")
public final class DecryptionFailedException extends AbstractJaxbMarshallableException implements ExceptionShortIdentifable {

    private static final long serialVersionUID = 1L;

    @JsonbProperty("sid")
    @XmlElement(name = "sid")
    private String sid;

    /**
     * Default constructor.
     */
    public DecryptionFailedException() {
        super("Decryption failed");
        this.sid = SHORT_ID_PREFIX + "-DECRYPTION_FAILED";
    }

    @Override
    public final String getShortId() {
        return sid;
    }

}
