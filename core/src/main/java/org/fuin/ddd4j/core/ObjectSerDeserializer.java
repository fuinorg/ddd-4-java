/**
 * Copyright (C) 2015 Michael Schnell. All rights reserved.
 * http://www.fuin.org/
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see http://www.gnu.org/licenses/.
 */
package org.fuin.ddd4j.core;

/**
 * Allows serializing/deserializing an object. Basically
 * a wrapper for the different serialization frameworks.
 */
public interface ObjectSerDeserializer {

    /**
     * Returns the content type that is used to serialize/deserialize.
     *
     * @return Content type like "application/json; charset=utf-8".
     */
    String getContentType();

    /**
     * Serializes a given object to a byte array.
     *
     * @param value Instance to serialize.
     * @return Object as byte array.
     */
    byte[] serialize(Object value);

    /**
     * Deserializes a byte array into an object.
     *
     * @param content   Byte array with object.
     * @param valueType Expected type of object.
     * @param <T>       Type of the object.
     * @return Deserialized new instance.
     */
    <T> T deserialize(byte[] content, Class<T> valueType);

}
