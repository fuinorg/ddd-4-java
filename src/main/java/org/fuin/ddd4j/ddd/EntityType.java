package org.fuin.ddd4j.ddd;

/**
 * Identifies a type of entity within all entity types of the context. As the
 * simple name of an entity class should be defined by the Ubiquitous Language,
 * the simple class name is a good choice for the type.
 */
public interface EntityType {

	/**
	 * Returns the entity type name as string.
	 * 
	 * @return Unique entity type name.
	 */
	public String asString();

}
