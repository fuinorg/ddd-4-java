package org.fuin.ddd4j.ddd;

import java.nio.charset.Charset;

import javax.validation.constraints.NotNull;

import org.fuin.objects4j.common.NeverNull;

/**
 * Serializes an object.
 */
public interface Serializer {

	/**
	 * Returns the unique identifier for the type of data.
	 * 
	 * @return Unique and never changing type name.
	 */
	@NeverNull
	public String getType();

	/**
	 * Returns the version of the type.
	 * 
	 * @return Version.
	 */
	public int getVersion();

	/**
	 * Returns the mime type the serializer supports.
	 * 
	 * @return Mime type.
	 */
	@NeverNull
	public String getMimeType();

	/**
	 * Returns the used encoding.
	 * 
	 * @return Encoding.
	 */
	@NeverNull
	public Charset getEncoding();

	/**
	 * Converts the given object into a byte representation.
	 * 
	 * @param obj
	 *            Object to serialize.
	 * 
	 * @return Serialized object.
	 */
	@NeverNull
	public <T> byte[] marshal(@NotNull T obj);

}
