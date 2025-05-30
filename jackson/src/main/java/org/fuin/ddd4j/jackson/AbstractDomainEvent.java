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
package org.fuin.ddd4j.jackson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.fuin.ddd4j.core.AggregateRootId;
import org.fuin.ddd4j.core.AggregateVersion;
import org.fuin.ddd4j.core.DomainEvent;
import org.fuin.ddd4j.core.EntityId;
import org.fuin.ddd4j.core.EntityIdPath;
import org.fuin.ddd4j.core.Event;
import org.fuin.ddd4j.core.EventId;
import org.fuin.objects4j.common.Contract;

import java.io.Serial;

/**
 * Base class for domain events.
 *
 * @param <ID> Type of the entity identifier.
 */
public abstract class AbstractDomainEvent<ID extends EntityId> extends AbstractEvent implements DomainEvent<ID> {

    @Serial
    private static final long serialVersionUID = 1000L;

    @NotNull
    @JsonProperty("entity-id-path")
    private EntityIdPath entityIdPath;

    @Nullable
    @JsonProperty("aggregate-version")
    private AggregateVersion aggregateVersion;

    /**
     * Protected default constructor for deserialization.
     */
    protected AbstractDomainEvent() { // NOSONAR Default constructor
        super();
    }

    /**
     * Constructor with entity identifier path.
     *
     * @param entityIdPath Identifier path from aggregate root to the entity that emitted the event.
     */
    public AbstractDomainEvent(@NotNull final EntityIdPath entityIdPath) {
        super();
        this.entityIdPath = entityIdPath;
    }

    /**
     * Constructor with entity identifier path and event this one responds to. Convenience method to set the correlation and causation
     * identifiers correctly.
     *
     * @param entityIdPath Identifier path from aggregate root to the entity that emitted the event.
     * @param respondTo    Causing event.
     */
    public AbstractDomainEvent(@NotNull final EntityIdPath entityIdPath, @NotNull final Event respondTo) {
        super(respondTo);
        this.entityIdPath = entityIdPath;
    }

    /**
     * Constructor with entity identifier path, correlation and causation identifiers.
     *
     * @param entityIdPath  Identifier path from aggregate root to the entity that emitted the event.
     * @param correlationId Correlation ID.
     * @param causationId   ID of the event that caused this one.
     */
    public AbstractDomainEvent(@NotNull final EntityIdPath entityIdPath, @Nullable final EventId correlationId,
                               @Nullable final EventId causationId) {
        super(correlationId, causationId);
        this.entityIdPath = entityIdPath;
    }

    @Override
    @JsonIgnore
    public final EntityIdPath getEntityIdPath() {
        return entityIdPath;
    }

    @Override
    @JsonIgnore
    public final ID getEntityId() {
        return entityIdPath.last();
    }

    @Override
    @Nullable
    @JsonIgnore
    public final AggregateVersion getAggregateVersion() {
        return aggregateVersion;
    }

    @Override
    @Nullable
    @JsonIgnore
    public final Integer getAggregateVersionInteger() {
        if (aggregateVersion == null) {
            return null;
        }
        return aggregateVersion.asBaseType();
    }

    /**
     * Base class for event builders.
     *
     * @param <ID>      Type of the entity identifier.
     * @param <TYPE>    Type of the event.
     * @param <BUILDER> Type of the builder.
     */
    protected abstract static class Builder<ID extends EntityId, TYPE extends AbstractDomainEvent<ID>, BUILDER extends Builder<ID, TYPE, BUILDER>>
            extends AbstractEvent.Builder<TYPE, BUILDER> {

        private AbstractDomainEvent<ID> delegate;

        /**
         * Constructor with event.
         *
         * @param delegate Event to populate with data.
         */
        public Builder(final TYPE delegate) {
            super(delegate);
            this.delegate = delegate;
        }

        /**
         * Sets the identifier path from aggregate root to the entity that emitted the event.
         *
         * @param entityIdPath Path of entity identifiers.
         * @return This builder.
         */
        @SuppressWarnings("unchecked")
        public final BUILDER entityIdPath(@NotNull final EntityIdPath entityIdPath) {
            Contract.requireArgNotNull("entityIdPath", entityIdPath);
            delegate.entityIdPath = entityIdPath;
            return (BUILDER) this;
        }

        /**
         * Convenience method to set the entity identifier path if the path has only the aggregate root identifer.
         *
         * @param id Aggregate root identifier that will be used to create the entity id path.
         * @return This builder.
         */
        @SuppressWarnings("unchecked")
        public final BUILDER entityIdPath(@NotNull AggregateRootId id) {
            Contract.requireArgNotNull("id", id);
            delegate.entityIdPath = new EntityIdPath(id);
            return (BUILDER) this;
        }

        /**
         * Sets the aggregate version.
         *
         * @param aggregateVersion Aggregate version.
         * @return This builder.
         */
        @SuppressWarnings("unchecked")
        public final BUILDER aggregateVersion(final AggregateVersion aggregateVersion) {
            delegate.aggregateVersion = aggregateVersion;
            return (BUILDER) this;
        }

        /**
         * Ensures that everything is set up for building the object or throws a runtime exception otherwise.
         */
        protected final void ensureBuildableAbstractDomainEvent() {
            ensureBuildableAbstractEvent();
            ensureNotNull("entityIdPath", delegate.entityIdPath);
        }

        /**
         * Sets the internal instance to a new one. This must be called within the build method.
         *
         * @param delegate Delegate to use.
         */
        protected final void resetAbstractDomainEvent(final TYPE delegate) {
            resetAbstractEvent(delegate);
            this.delegate = delegate;
        }

    }

}
