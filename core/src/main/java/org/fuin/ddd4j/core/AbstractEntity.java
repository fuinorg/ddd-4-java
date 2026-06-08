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
package org.fuin.ddd4j.core;

import org.fuin.objects4j.common.Contract;
import org.jspecify.annotations.Nullable;

/**
 * Base class for entities.
 *
 * @param <ROOT>
 *            Type of the aggregate root.
 * @param <ROOT_ID>
 *            Type of the aggregate identifier.
 * @param <ID>
 *            Type of the entity identifier.
 */
public abstract class AbstractEntity<ROOT_ID extends AggregateRootId, ROOT extends AbstractAggregateRoot<ROOT_ID>, ID extends EntityId>
        implements Entity<ID> {

    private final ROOT root;

    /**
     * Constructor with root aggregate.
     *
     * @param root
     *            Root aggregate.
     */
    public AbstractEntity(final ROOT root) {
        super();
        Contract.requireArgNotNull("root", root);
        this.root = root;
    }

    /**
     * Applies the given new event. CAUTION: Don't use this method for applying historic events!
     *
     * @param event
     *            Event to dispatch to the appropriate event handler method.
     */
    protected final void apply(final DomainEvent<?> event) {
        root.applyNewChildEvent(this, event);
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + getId().hashCode();
        return result;
    }

    @Override
    public final boolean equals(@Nullable final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AbstractEntity<?, ?, ?> other = (AbstractEntity<?, ?, ?>) obj;
        return getId().equals(other.getId());
    }

    /**
     * Returns the aggregate root the entity belongs to.
     *
     * @return Aggregate root this is a child of.
     */
    protected final ROOT getRoot() {
        return root;
    }

    /**
     * Returns the identifier of the aggregate root the entity belongs to.
     *
     * @return Unique aggregate root identifier.
     */
    protected final ROOT_ID getRootId() {
        return root.getId();
    }

    /**
     * Base class for entity builders. As an entity always requires its aggregate root and identifier at construction
     * time (the {@code root} field is final), the builder collects this mandatory data plus the entity specific
     * attributes and creates the instance via the real constructor within the concrete {@code build()} method.
     *
     * @param <ROOT_ID> Type of the aggregate root identifier.
     * @param <ROOT>    Type of the aggregate root.
     * @param <ID>      Type of the entity identifier.
     * @param <TYPE>    Type of the entity.
     * @param <BUILDER> Type of the builder.
     */
    protected abstract static class Builder<ROOT_ID extends AggregateRootId, ROOT extends AbstractAggregateRoot<ROOT_ID>, ID extends EntityId, TYPE extends AbstractEntity<ROOT_ID, ROOT, ID>, BUILDER extends Builder<ROOT_ID, ROOT, ID, TYPE, BUILDER>> {

        @Nullable
        private ROOT rootAggregate;

        @Nullable
        private ID id;

        /**
         * Default constructor.
         */
        protected Builder() {
            super();
        }

        /**
         * Sets the root aggregate the entity belongs to.
         *
         * @param rootAggregate Root aggregate of this entity.
         * @return This builder.
         */
        @SuppressWarnings("unchecked")
        public final BUILDER rootAggregate(final ROOT rootAggregate) {
            Contract.requireArgNotNull("rootAggregate", rootAggregate);
            this.rootAggregate = rootAggregate;
            return (BUILDER) this;
        }

        /**
         * Sets the unique entity identifier.
         *
         * @param id Unique entity identifier.
         * @return This builder.
         */
        @SuppressWarnings("unchecked")
        public final BUILDER id(final ID id) {
            Contract.requireArgNotNull("id", id);
            this.id = id;
            return (BUILDER) this;
        }

        /**
         * Returns the root aggregate to use for constructing the entity.
         *
         * @return Root aggregate.
         */
        @Nullable
        protected final ROOT getRootAggregate() {
            return rootAggregate;
        }

        /**
         * Returns the entity identifier to use for constructing the entity.
         *
         * @return Unique entity identifier.
         */
        @Nullable
        protected final ID getEntityId() {
            return id;
        }

        /**
         * Ensures that the mandatory data is set up for building the object or throws a runtime exception otherwise.
         */
        protected final void ensureBuildableAbstractEntity() {
            ensureNotNull("rootAggregate", rootAggregate);
            ensureNotNull("id", id);
        }

        /**
         * Clears the common entity data. This must be called within the build method.
         */
        protected final void resetAbstractEntity() {
            this.rootAggregate = null;
            this.id = null;
        }

        /**
         * Ensures that a field is set or throws a runtime exception otherwise.
         *
         * @param name  Name of the field.
         * @param value Value to test for {@literal null}.
         */
        protected final void ensureNotNull(final String name, @Nullable final Object value) {
            if (value == null) {
                throw new RuntimeException("The value of '" + name + "' has not been set");
            }
        }

        /**
         * Creates a new entity instance from the builder's data.
         *
         * @return New instance.
         */
        public abstract TYPE build();

    }

}
