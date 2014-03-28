package org.fuin.ddd4j.ddd;

import javax.validation.constraints.NotNull;

import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.common.NeverNull;

/**
 * Signals a conflict between an expected and an actual version for an
 * aggregate.
 */
public final class AggregateVersionConflictException extends Exception {

	private static final long serialVersionUID = 1L;

	private final EntityType aggregateType;

	private final AggregateRootId aggregateId;

	private final int expected;

	private final int actual;

	/**
	 * Constructor with all data.
	 * 
	 * @param
	 * @param aggregateId
	 *            Unique identifier of the aggregate.
	 * @param expected
	 *            Expected version.
	 * @param actual
	 *            Actual version.
	 */
	public AggregateVersionConflictException(
			@NotNull final EntityType aggregateType,
			@NotNull final AggregateRootId aggregateId, final int expected,
			final int actual) {
		super("Expected version " + expected + " for aggregate '"
				+ aggregateType + "' (" + aggregateId + "), but was " + actual);

		Contract.requireArgNotNull("aggregateType", aggregateType);
		Contract.requireArgNotNull("aggregateId", aggregateId);

		this.aggregateType = aggregateType;
		this.aggregateId = aggregateId;
		this.expected = expected;
		this.actual = actual;
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

	/**
	 * Returns the expected version.
	 * 
	 * @return Expected version.
	 */
	public final int getExpected() {
		return expected;
	}

	/**
	 * Returns the actual version.
	 * 
	 * @return Actual version.
	 */
	public final int getActual() {
		return actual;
	}

}
