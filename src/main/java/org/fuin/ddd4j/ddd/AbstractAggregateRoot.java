package org.fuin.ddd4j.ddd;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.fuin.ddd4j.example.Agreement;
import org.fuin.objects4j.common.Contract;

/**
 * Base class for aggregate roots.
 * 
 * @param <ID>
 *            Aggregate identifier.
 */
public abstract class AbstractAggregateRoot<ID extends AggregateRootId>
		implements AggregateRoot<ID> {

	private int version = 0;

	private List<DomainEvent<?>> uncommitedChanges;

	/**
	 * Default constructor.
	 */
	public AbstractAggregateRoot() {
		super();
		this.uncommitedChanges = new ArrayList<DomainEvent<?>>();
	}

	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + getId().hashCode();
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
		final Agreement other = (Agreement) obj;
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
	public boolean hasUncommitedChanges() {
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
				final boolean applied = callAnnotatedEventHandlerMethodOnAggregateRootOrChild(
						this, event);
				if (applied) {
					version++;
				} else {
					throw new IllegalStateException(
							"Wasn't able to apply historic event '" + event
									+ "' to: " + this.getClass().getName());
				}
			}
		}
	}

	/**
	 * Tries to find recursive an annotated method on this object or one of it's
	 * child entities to apply the given event to.
	 * 
	 * @param event
	 *            Event to apply.
	 * 
	 * @return TRUE if the event was successfully applied, else FALSE.
	 */
	static boolean callAnnotatedEventHandlerMethodOnAggregateRootOrChild(
			final AggregateRoot<?> aggregateRoot, final DomainEvent<?> event) {

		final EntityIdPath path = event.getEntityIdPath();
		final Iterator<EntityId> idIt = path.iterator();
		EntityId entityId = idIt.next();
		if (!(entityId instanceof AggregateRootId)) {
			throw new IllegalStateException(
					"The first ID in the entity identifier path was not an "
							+ AggregateRootId.class.getSimpleName() + ": "
							+ path);
		}

		if (!idIt.hasNext()) {
			// Direct event from aggregate root
			return callAnnotatedEventHandlerMethod(aggregateRoot, event);
		}

		// Continue with child(s)
		Entity<?> entity = aggregateRoot;
		while (idIt.hasNext()) {
			final EntityId id = idIt.next();
			final Method foundChildEntityMethod = findDeclaredAnnotatedMethod(
					entity, ChildEntityLocator.class, id.getClass());
			if (foundChildEntityMethod == null) {
				return false;
			}
			entity = invoke(foundChildEntityMethod, entity, id);
		}

		// Call event handler on the child entity
		return callAnnotatedEventHandlerMethod(entity, event);
	}

	@SuppressWarnings("unchecked")
	static <T extends Entity<?>> T invoke(final Method method,
			final Entity<?> entity, final EntityId id) {
		try {
			if (!method.isAccessible()) {
				method.setAccessible(true);
			}
			return (T) method.invoke(entity, id);
		} catch (final IllegalAccessException | IllegalArgumentException
				| InvocationTargetException ex) {
			throw new RuntimeException("Error calling method on '"
					+ entity.getClass().getSimpleName() + "' with argument '"
					+ id.getClass().getSimpleName() + "': " + method, ex);
		}
	}

	/**
	 * Returns a list of old / ignored events. Sub classes can overwrite this
	 * method to ignore historic events that are not needed any more.
	 * 
	 * @return Events that can be safely ignored.
	 */
	protected List<Class<? extends DomainEvent<?>>> getIgnoredEvents() {
		return Collections.emptyList();
	}

	/**
	 * Applies the given new event. CAUTION: Don't use this method for applying
	 * historic events!
	 * 
	 * @param event
	 *            Event to dispatch to the appropriate event handler method.
	 */
	protected final void apply(@NotNull final DomainEvent<?> event) {
		if (callAnnotatedEventHandlerMethod(this, event)) {
			uncommitedChanges.add(event);
		} else {
			throw new IllegalStateException(
					"Couldn't find an event handler for: "
							+ event.getClass().getName());
		}
	}

	/**
	 * Applies the given new event. CAUTION: Don't use this method for applying
	 * historic events!
	 * 
	 * @param entity
	 *            Child entity that requested the apply operation.
	 * @param event
	 *            Event to dispatch to the appropriate event handler method.
	 */
	void applyNewChildEvent(@NotNull AbstractEntity<?> entity,
			@NotNull final DomainEvent<?> event) {

		if (callAnnotatedEventHandlerMethod(entity, event)) {
			uncommitedChanges.add(event);
		} else {
			throw new IllegalStateException(
					"Couldn't find an event handler in '"
							+ entity.getClass().getName() + "' for: " + event);
		}

	}

	/**
	 * Applies the event to the method in the entity that is annotated with
	 * {@link EventHandler} and has exactly one parameter with the same type as
	 * the domain event.
	 * 
	 * @param entity
	 *            Entity to apply the event to.
	 * @param event
	 *            Event to apply.
	 * 
	 * @return TRUE if an appropriate event handler method was found and the
	 *         event was applied, else FALSE.
	 */
	static boolean callAnnotatedEventHandlerMethod(final Entity<?> entity,
			final DomainEvent<?> event) {

		Contract.requireArgNotNull("entity", entity);
		Contract.requireArgNotNull("event", event);

		final Method method = findDeclaredAnnotatedMethod(entity,
				EventHandler.class, event.getClass());
		if (method == null) {
			return false;
		}

		try {
			if (!method.isAccessible()) {
				method.setAccessible(true);
			}
			method.invoke(entity, event);
			return true;
		} catch (final IllegalAccessException | IllegalArgumentException
				| InvocationTargetException ex) {
			throw new RuntimeException("Error 'apply("
					+ event.getClass().getSimpleName() + ")'", ex);
		}

	}

	/**
	 * Finds a declared method that has a given annotation and single parameter.
	 * 
	 * @param obj
	 *            Object to inspect.
	 * @param annotationType
	 *            Expected annotation type.
	 * @param argumentType
	 *            Expected single argument type.
	 * 
	 * @return Method or <code>null</code> if any of the expected parameters
	 *         does not match.
	 */
	static Method findDeclaredAnnotatedMethod(final Object obj,
			final Class<? extends Annotation> annotationType,
			final Class<?> argumentType) {

		final Method[] methods = obj.getClass().getDeclaredMethods();
		for (final Method method : methods) {
			if (method.getAnnotation(annotationType) != null) {
				final Class<?>[] types = method.getParameterTypes();
				if ((types.length == 1) && (types[0] == argumentType)) {
					return method;
				}
			}
		}
		return null;

	}

}
