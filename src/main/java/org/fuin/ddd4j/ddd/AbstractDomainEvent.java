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

import javax.annotation.Nullable;
import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTypeAdapter;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Base class for domain events.
 * 
 * @param <ID>
 *            Type of the entity identifier.
 */
public abstract class AbstractDomainEvent<ID extends EntityId> extends AbstractEvent implements DomainEvent<ID> {

    private static final long serialVersionUID = 1000L;

    @NotNull
    @JsonbTypeAdapter(EntityIdPathConverter.class)
    @JsonbProperty("entity-id-path")
    @XmlJavaTypeAdapter(EntityIdPathConverter.class)
    @XmlElement(name = "entity-id-path")
    private EntityIdPath entityIdPath;

    /**
     * Protected default constructor for deserialization.
     */
    protected AbstractDomainEvent() { // NOSONAR Default constructor
        super();
    }

    /**
     * Constructor with entity identifier path.
     * 
     * @param entityIdPath
     *            Identifier path from aggregate root to the entity that emitted the event.
     */
    public AbstractDomainEvent(@NotNull final EntityIdPath entityIdPath) {
        super();
        this.entityIdPath = entityIdPath;
    }

    /**
     * Constructor with entity identifier path and event this one responds to. Convenience method to set the correlation and causation
     * identifiers correctly.
     * 
     * @param entityIdPath
     *            Identifier path from aggregate root to the entity that emitted the event.
     * @param respondTo
     *            Causing event.
     */
    public AbstractDomainEvent(@NotNull final EntityIdPath entityIdPath, @NotNull final Event respondTo) {
        super(respondTo);
        this.entityIdPath = entityIdPath;
    }

    /**
     * Constructor with entity identifier path, correlation and causation identifiers.
     * 
     * @param entityIdPath
     *            Identifier path from aggregate root to the entity that emitted the event.
     * @param correlationId
     *            Correlation ID.
     * @param causationId
     *            ID of the event that caused this one.
     */
    public AbstractDomainEvent(@NotNull final EntityIdPath entityIdPath, @Nullable final EventId correlationId,
            @Nullable final EventId causationId) {
        super(correlationId, causationId);
        this.entityIdPath = entityIdPath;
    }

    @Override
    public final EntityIdPath getEntityIdPath() {
        return entityIdPath;
    }

    @Override
    public final ID getEntityId() {
        return entityIdPath.last();
    }

}
