package org.fuin.ddd4j.ddd;

/**
 * Identifies an aggregate within all aggregates of the same type.
 */
public interface AggregateRootId extends EntityId {

	/**
	 * Returns the aggregate identifier as string.
	 * 
	 * @return Aggregate identifier.
	 */
	public String asString();

}
