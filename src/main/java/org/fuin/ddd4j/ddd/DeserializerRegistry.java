/**
 * Copyright (C) 2013 Future Invent Informationsmanagement GmbH. All rights
 * reserved. <http://www.fuin.org/>
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fuin.ddd4j.ddd;

import java.nio.charset.Charset;

import javax.validation.constraints.NotNull;

/**
 * Locates a deserializer for a given type, version and encoding combination.
 */
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
	 * @return Deserializer instance configured with the arguments or NULL if no
	 *         deserializer was found for the type.
	 */
	public Deserializer getDeserializer(@NotNull String type, int version,
			@NotNull String mimeType, @NotNull Charset encoding);

}
