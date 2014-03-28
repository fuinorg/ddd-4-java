package org.fuin.ddd4j.ddd;

import javax.validation.constraints.NotNull;

/**
 * Cache for aggregates of the same type.
 * 
 * @param <T>
 *            Type of the aggregate.
 */
public interface AggregateCache<AGGREGATE> {

	/**
	 * Tries to read the aggregate with the given identifier from the cache.
	 * 
	 * @param aggregateId
	 *            Aggregate to load.
	 * 
	 * @return Cached aggregate or <code>null</code> if it was not found in the
	 *         cache.
	 */
	public AGGREGATE get(@NotNull AggregateRootId aggregateId);

	/**
	 * Puts an aggregate with the given identifier in the cache.
	 * 
	 * @param aggregateId
	 *            Aggregate to load.
	 * @param aggregate
	 *            Aggregate to cache.
	 */
	public void put(@NotNull AggregateRootId aggregateId,
			@NotNull AGGREGATE aggregate);

	/**
	 * Removes the aggregate with the given identifier from the cache.
	 * 
	 * @param aggregateId
	 *            Aggregate to remove from cache.
	 */
	public void remove(@NotNull AggregateRootId aggregateId);

}
