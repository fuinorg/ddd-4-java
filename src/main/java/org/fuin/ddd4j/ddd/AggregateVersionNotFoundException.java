package org.fuin.ddd4j.ddd;

import javax.validation.constraints.NotNull;

import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.common.NeverNull;

/**
 * Signals that the requested version for an aggregate does not exist.
 */
public final class AggregateVersionNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	private final EntityType aggregateType;

	private final AggregateRootId aggregateId;

	private final int version;

	/**
	 * Constructor with all data.
	 * 
	 * @param aggregateType
	 *            Type of the aggregate.
	 * @param aggregateId
	 *            Unique identifier of the aggregate.
	 * @param version
	 *            Requested version.
	 */
	public AggregateVersionNotFoundException(
			@NotNull final EntityType aggregateType,
			@NotNull final AggregateRootId aggregateId, final int version) {
		super("Requested version " + version + " for aggregate '"
				+ aggregateType + "' (" + aggregateId + ") does not exist");

		Contract.requireArgNotNull("aggregateType", aggregateType);
		Contract.requireArgNotNull("aggregateId", aggregateId);

		this.aggregateType = aggregateType;
		this.aggregateId = aggregateId;
		this.version = version;
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
	 * Returns the requested version.
	 * 
	 * @return Version.
	 */
	public final int getVersion() {
		return version;
	}

}
