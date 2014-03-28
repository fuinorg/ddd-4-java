package org.fuin.ddd4j.ddd;

import javax.validation.constraints.NotNull;

import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.common.NeverNull;

/**
 * Signals that an aggregate of a given type and identifier was deleted from the
 * repository.
 */
public final class AggregateDeletedException extends Exception {

	private static final long serialVersionUID = 1L;

	private final EntityType aggregateType;

	private final AggregateRootId aggregateId;

	/**
	 * Constructor with all data.
	 * 
	 * @param aggregateType
	 *            Type of the aggregate.
	 * @param aggregateId
	 *            Unique identifier of the aggregate.
	 */
	public AggregateDeletedException(@NotNull final EntityType aggregateType,
			@NotNull final AggregateRootId aggregateId) {
		super("Aggregate of type '" + aggregateType + "' with id "
				+ aggregateId + " already deleted");

		Contract.requireArgNotNull("aggregateType", aggregateType);
		Contract.requireArgNotNull("aggregateId", aggregateId);

		this.aggregateType = aggregateType;
		this.aggregateId = aggregateId;
	}

	/**
	 * Returns the type of the aggregate.
	 * 
	 * @return Type.
	 */
	@NeverNull
	public final EntityType getAggregateType() {
		return aggregateType;
	}

	/**
	 * Returns the unique identifier of the aggregate.
	 * 
	 * @return Stream with version conflict.
	 */
	@NeverNull
	public final AggregateRootId getAggregateId() {
		return aggregateId;
	}

}
