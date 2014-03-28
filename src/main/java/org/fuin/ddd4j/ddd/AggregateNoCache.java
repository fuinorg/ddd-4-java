package org.fuin.ddd4j.ddd;

/**
 * Never caches anything.
 * 
 * @param <T>
 *            Type of the aggregate.
 */
public final class AggregateNoCache<AGGREGATE> implements
		AggregateCache<AGGREGATE> {

	@Override
	public final AGGREGATE get(final AggregateRootId aggregateId) {
		// Always return null
		return null;
	}

	@Override
	public void put(AggregateRootId aggregateId, AGGREGATE aggregate) {
		// Do nothing
	}

	@Override
	public final void remove(final AggregateRootId aggregateId) {
		// Do nothing
	}

}
