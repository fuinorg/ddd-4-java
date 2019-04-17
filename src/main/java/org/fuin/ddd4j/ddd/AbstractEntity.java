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

import javax.validation.constraints.NotNull;

import org.fuin.objects4j.common.Contract;

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
// CHECKSTYLE:OFF:LineLength
public abstract class AbstractEntity<ROOT_ID extends AggregateRootId, ROOT extends AggregateRoot<ROOT_ID>, ID extends EntityId>
        implements Entity<ID> {
    // CHECKSTYLE:ON:LineLength

    private final ROOT root;

    private final AggregateRootImpl impl;

    /**
     * Constructor with root aggregate.
     * 
     * @param root
     *            Root aggregate.
     * @param impl
     *            Additional functionality the root aggregate implementation has to provide.
     */
    public AbstractEntity(@NotNull final ROOT root, final AggregateRootImpl impl) {
        super();
        Contract.requireArgNotNull("root", root);
        Contract.requireArgNotNull("impl", impl);
        this.root = root;
        this.impl = impl;
    }

    /**
     * Applies the given new event. CAUTION: Don't use this method for applying historic events!
     * 
     * @param event
     *            Event to dispatch to the appropriate event handler method.
     */
    protected final void apply(@NotNull final DomainEvent<?> event) {
        impl.applyNewChildEvent(this, event);
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + getId().hashCode();
        return result;
    }

    @Override
    public final boolean equals(final Object obj) {
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
        if (!getId().equals(other.getId())) {
            return false;
        }
        return true;
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
     * Returns the aggregate root implementation.
     * 
     * @return Implementation reference.
     */
    protected final AggregateRootImpl getAggregateRootImpl() {
        return impl;
    }

    /**
     * Aggregate root functionality that is not part of the public API but is required by this class.
     */
    public static interface AggregateRootImpl {

        /**
         * Applies the given new event. CAUTION: Don't use this method for applying historic events!
         * 
         * @param entity
         *            Child entity that requested the apply operation.
         * @param event
         *            Event to dispatch to the appropriate event handler method.
         */
        public void applyNewChildEvent(@NotNull final AbstractEntity<?, ?, ?> entity, @NotNull final DomainEvent<?> event);

    }

}
