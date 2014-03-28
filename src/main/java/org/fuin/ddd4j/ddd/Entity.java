package org.fuin.ddd4j.ddd;

/**
 * An object that is not defined by its attributes, but rather by a thread of
 * continuity and its identity.
 * 
 * @param <ID>
 *            Entity ID type.
 */
public interface Entity<ID extends EntityId> {

	/**
	 * Returns the unique type.
	 * 
	 * @return Type of the entity that is unique in the context.
	 */
	public EntityType getType();

	/**
	 * Returns the unique entity identifier.
	 * 
	 * @return Identifier.
	 */
	public ID getId();

}
