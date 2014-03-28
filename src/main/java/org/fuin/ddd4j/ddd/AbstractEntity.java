package org.fuin.ddd4j.ddd;

import javax.validation.constraints.NotNull;

import org.fuin.ddd4j.example.Agreement;
import org.fuin.objects4j.common.Contract;

/**
 * Base class for entities.
 * 
 * @param <ID>
 *            Entity identifier.
 */
public abstract class AbstractEntity<ID extends EntityId> implements Entity<ID> {

	private AbstractAggregateRoot<?> root;

	/**
	 * Constructor with root aggregate.
	 * 
	 * @param root
	 *            Root aggregate.
	 */
	public AbstractEntity(@NotNull final AbstractAggregateRoot<?> root) {
		super();
		Contract.requireArgNotNull("root", root);
		this.root = root;
	}

	/**
	 * Applies the given new event. CAUTION: Don't use this method for applying
	 * historic events!
	 * 
	 * @param event
	 *            Event to dispatch to the appropriate event handler method.
	 */
	protected final void apply(@NotNull final DomainEvent<?> event) {
		root.applyNewChildEvent(this, event);
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

}
