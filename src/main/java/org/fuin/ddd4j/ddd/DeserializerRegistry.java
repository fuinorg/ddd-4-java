package org.fuin.ddd4j.ddd;

import java.nio.charset.Charset;

import javax.validation.constraints.NotNull;

public interface DeserializerRegistry {

	/**
	 * Tries to find a deserializer for the given combination.
	 * 
	 * @param type
	 *            Unique identifier for the type of data.
	 * @param version
	 *            Version of the data.
	 * @param mimeType
	 *            Mime type.
	 * @param encoding
	 *            Encoding.
	 * 
	 * @return Deserializer instance configured with the arguments.
	 */
	public Deserializer getDeserializer(@NotNull String type, int version,
			@NotNull String mimeType, @NotNull Charset encoding);

}
