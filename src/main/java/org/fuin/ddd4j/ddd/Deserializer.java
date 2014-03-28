package org.fuin.ddd4j.ddd;

/**
 * Serializes an object.
 */
public interface Deserializer {

	/**
	 * Converts the given data into an object.
	 * 
	 * @param data
	 *            Serialized object.
	 * 
	 * @return Deserialized object.
	 */
	public <T> T unmarshal(byte[] data);

}
