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
	 * 
	 * @param <T>
	 *            Type the data is converted into.
	 */
	@NeverNull
	public <T> byte[] marshal(@NotNull T obj);

}
