/**
 * Copyright (C) 2015 Michael Schnell. All rights reserved.
 * http://www.fuin.org/
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see http://www.gnu.org/licenses/.
 */
package org.fuin.ddd4j.esc;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.fuin.utils4j.TestOmitted;

/**
 * Version 1 of the "vendor created" event: a flat structure with the id, key and name as plain string
 * attributes. It is a standalone JAXB value carrier (not a {@code DomainEvent}) used only to write a stored
 * {@code …v1} representation into the event store; on read it is up-cast to the current
 * {@link org.fuin.ddd4j.jsonbtestmodel.VendorCreatedEvent} (v2) by
 * {@link VendorCreatedEventV1ToV2Converter}.
 */
@TestOmitted("Only a test fixture")
@XmlRootElement(name = "vendor-created-event")
@XmlAccessorType(XmlAccessType.FIELD)
public final class VendorCreatedEventV1 {

    @XmlAttribute(name = "id")
    private String id;

    @XmlAttribute(name = "key")
    private String key;

    @XmlAttribute(name = "name")
    private String name;

    /**
     * Default constructor only for deserialization.
     */
    protected VendorCreatedEventV1() {
    }

    /**
     * Constructor with all data.
     *
     * @param id   Technical vendor identifier (UUID string).
     * @param key  Vendor business key.
     * @param name Vendor name.
     */
    public VendorCreatedEventV1(final String id, final String key, final String name) {
        this.id = id;
        this.key = key;
        this.name = name;
    }

    /**
     * Returns the technical vendor identifier.
     *
     * @return UUID string.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the vendor business key.
     *
     * @return Key.
     */
    public String getKey() {
        return key;
    }

    /**
     * Returns the vendor name.
     *
     * @return Name.
     */
    public String getName() {
        return name;
    }

}
