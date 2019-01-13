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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.fuin.objects4j.common.AbstractJaxbMarshallableException;
import org.fuin.objects4j.common.ExceptionShortIdentifable;

/**
 * Signals that an entity was not found.
 */
@XmlRootElement(name = "entity-not-found-exception")
@XmlAccessorType(XmlAccessType.NONE)
public final class EntityNotFoundException extends AbstractJaxbMarshallableException implements ExceptionShortIdentifable {

    private static final long serialVersionUID = 1L;

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
     * JAX-B constructor.
     */
    protected EntityNotFoundException() {
        super();
    }

    /**
     * Constructor with all data.
     * 
     * @param parentIdPath
     *            Path from root to parent.
     * @param entityId
     *            Unique identifier of the entity that was not found.
     */
    public EntityNotFoundException(@NotNull final EntityIdPath parentIdPath, @NotNull final EntityId entityId) {
        super(entityId.asTypedString() + " not found in " + parentIdPath.asString());

        this.sid = SHORT_ID_PREFIX + "-ENTITY_NOT_FOUND";
        this.parentIdPath = parentIdPath.asString();
        this.entityId = entityId.asString();
    }

    @Override
    public final String getShortId() {
        return sid;
    }

    /**
     * Returns the path from root to parent.
     * 
     * @return Path.
     */
    @NotNull
    public final String getParentIdPath() {
        return parentIdPath;
    }

    /**
     * Returns the unique identifier of the entity.
     * 
     * @return Unknown entity identifier.
     */
    @NotNull
    public final String getEntityId() {
        return entityId;
    }

}
