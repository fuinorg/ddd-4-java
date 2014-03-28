package org.fuin.ddd4j.ddd;

/**
 * Factory to create entity identifier.
 */
public interface EntityIdFactory {

	/**
	 * Verifies if the given type string is a valid one.
	 * 
	 * @param type
	 *            Type to be verified.
	 * 
	 * @return TRUE if the factory can create identifiers for the given type.
	 */
	public boolean containsType(String type);

	/**
	 * Creates an entity id by type and string identifier.
	 * 
	 * @param type
	 *            Type of the identifier.
	 * @param id
	 *            Identifier.
	 * 
	 * @return Entity identifier.
	 */
	public EntityId createEntityId(String type, String id);

}
