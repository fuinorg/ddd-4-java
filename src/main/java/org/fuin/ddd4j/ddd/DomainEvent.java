package org.fuin.ddd4j.ddd;

import org.fuin.objects4j.common.NeverNull;

/**
 * Domain event published by an entity.
 * 
 * @param <ID>
 *            Type of the identifier.
 */
public interface DomainEvent<ID extends EntityId> extends Event {

	/**
	 * Returns the path to the originator of the event.
	 * 
	 * @return List of unique identifiers from aggregate root to the entity that
	 *         emitted the event.
	 */
	@NeverNull
	public EntityIdPath getEntityIdPath();

	/**
	 * Returns the identifier of the entity that caused this event. This is the
	 * last ID in the path.
	 * 
	 * @return Entity identifier.
	 */
	@NeverNull
	public ID getEntityId();

}
