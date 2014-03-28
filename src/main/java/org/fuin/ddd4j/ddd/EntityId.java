package org.fuin.ddd4j.ddd;

/**
 * Identifies an entity within all entities of the same type.
 */
public interface EntityId extends TechnicalId {

	/**
	 * Returns the type represented by this identifier.
	 * 
	 * @return Type of entity.
	 */
	public EntityType getType();

	/**
	 * Returns the entity identifier as string.
	 * 
	 * @return Entity identifier.
	 */
	public String asString();

	/**
	 * Returns the entity identifier as string with type and identifier.
	 * 
	 * @return Type and identifier.
	 */
	public String asTypedString();

}
