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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import jakarta.validation.constraints.NotNull;

import org.fuin.objects4j.common.Contract;

/**
 * Base class for aggregate roots.
 * 
 * @param <ID>
 *            Aggregate identifier.
 */
public abstract class AbstractAggregateRoot<ID extends AggregateRootId> implements AggregateRoot<ID> {

    private static final MethodExecutor METHOD_EXECUTOR = new MethodExecutor();

    private int version = -1;

    private final List<DomainEvent<?>> uncommitedChanges;

    /**
     * Default constructor.
     */
    public AbstractAggregateRoot() {
        super();
        this.uncommitedChanges = new ArrayList<>();
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
        final AbstractAggregateRoot<?> other = (AbstractAggregateRoot<?>) obj;
        if (!getId().equals(other.getId())) {
            return false;
        }
        return true;
    }

    @Override
    public final List<DomainEvent<?>> getUncommittedChanges() {
        return Collections.unmodifiableList(uncommitedChanges);
    }

    @Override
    public final boolean hasUncommitedChanges() {
        return uncommitedChanges.size() > 0;
    }

    @Override
    public final void markChangesAsCommitted() {
        version = getNextVersion();
        uncommitedChanges.clear();
    }

    @Override
    public final int getVersion() {
        return version;
    }

    @Override
    public final int getNextVersion() {
        return version + uncommitedChanges.size();
    }

    @Override
    public final AggregateVersion getNextApplyVersion() {
        return AggregateVersion.valueOf(getNextVersion() + 1);
    }

    @Override
    public final void loadFromHistory(final DomainEvent<?>... history) {
        if (history == null) {
            return;
        }
        loadFromHistory(Arrays.asList(history));
    }

    @Override
    public final void loadFromHistory(final List<DomainEvent<?>> history) {
        if (history == null) {
            return;
        }
        for (final DomainEvent<?> event : history) {
            if (!getIgnoredEvents().contains(event.getClass())) {
                final boolean applied = callAnnotatedEventHandlerMethodOnAggregateRootOrChild(this, event);
                if (applied) {
                    version++;
                } else {
                    throw new IllegalStateException("Wasn't able to apply historic event '" + event + "' to: " + this.getClass().getName());
                }
            }
        }
    }

    /**
     * Tries to find recursive an annotated method on this object or one of it's child entities to apply the given event to.
     * 
     * @param aggregateRoot
     *            Aggregate root with the event handler method.
     * @param event
     *            Event to apply.
     * 
     * @return TRUE if the event was successfully applied, else FALSE.
     */
    static boolean callAnnotatedEventHandlerMethodOnAggregateRootOrChild(final AggregateRoot<?> aggregateRoot, final DomainEvent<?> event) {

        final EntityIdPath path = event.getEntityIdPath();
        final Iterator<EntityId> idIt = path.iterator();
        final EntityId entityId = idIt.next();
        if (!(entityId instanceof AggregateRootId)) {
            throw new IllegalStateException(
                    "The first ID in the entity identifier path was not an " + AggregateRootId.class.getSimpleName() + ": " + path);
        }

        if (!idIt.hasNext()) {
            // Direct event from aggregate root
            return callAnnotatedEventHandlerMethod(aggregateRoot, event);
        }

        // Continue with child(s)
        Entity<?> entity = aggregateRoot;
        while (idIt.hasNext()) {
            final EntityId id = idIt.next();
            final Method foundChildEntityMethod = METHOD_EXECUTOR.findDeclaredAnnotatedMethod(entity, ChildEntityLocator.class,
                    id.getClass());
            if (foundChildEntityMethod == null) {
                return false;
            }
            entity = METHOD_EXECUTOR.invoke(foundChildEntityMethod, entity, id);
        }

        // Call event handler on the child entity
        return callAnnotatedEventHandlerMethod(entity, event);
    }

    /**
     * Returns a list of old / ignored events. Sub classes can overwrite this method to ignore historic events that are not needed any more.
     * 
     * @return Events that can be safely ignored.
     */
    protected final List<Class<? extends DomainEvent<?>>> getIgnoredEvents() {
        return Collections.emptyList();
    }

    /**
     * Applies the given new event. CAUTION: Don't use this method for applying historic events!
     * 
     * @param event
     *            Event to dispatch to the appropriate event handler method.
     */
    protected final void apply(@NotNull final DomainEvent<?> event) {
        if (callAnnotatedEventHandlerMethod(this, event)) {
            uncommitedChanges.add(event);
        } else {
            throw new IllegalStateException("Couldn't find an event handler for: " + event.getClass().getName());
        }
    }

    /**
     * Applies the given new event. CAUTION: Don't use this method for applying historic events!
     * 
     * @param entity
     *            Child entity that requested the apply operation.
     * @param event
     *            Event to dispatch to the appropriate event handler method.
     */
    final void applyNewChildEvent(@NotNull final AbstractEntity<?, ?, ?> entity, @NotNull final DomainEvent<?> event) {

        if (callAnnotatedEventHandlerMethod(entity, event)) {
            uncommitedChanges.add(event);
        } else {
            throw new IllegalStateException("Couldn't find an event handler in '" + entity.getClass().getName() + "' for: " + event);
        }

    }

    /**
     * Applies the event to the method in the entity that is annotated with {@link ApplyEvent} and has exactly one parameter with the same
     * type as the domain event.
     * 
     * @param entity
     *            Entity to apply the event to.
     * @param event
     *            Event to apply.
     * 
     * @return TRUE if an appropriate event handler method was found and the event was applied, else FALSE.
     */
    static boolean callAnnotatedEventHandlerMethod(final Entity<?> entity, final DomainEvent<?> event) {

        Contract.requireArgNotNull("entity", entity);
        Contract.requireArgNotNull("event", event);

        final Method method = METHOD_EXECUTOR.findDeclaredAnnotatedMethod(entity, ApplyEvent.class, event.getClass());
        if (method == null) {
            return false;
        }
        METHOD_EXECUTOR.invoke(method, entity, event);
        return true;

    }

}
