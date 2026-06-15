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
package org.fuin.ddd4j.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.fuin.ddd4j.core.ObjectSerDeserializer;
import org.fuin.objects4j.common.Contract;

import javax.annotation.concurrent.ThreadSafe;
import java.io.IOException;

/**
 * Serializes/deserializes an object using a Jackson {@link ObjectMapper}.
 */
@ThreadSafe
public final class JacksonObjectSerDeserializer implements ObjectSerDeserializer {

    private final String contentType;

    private final ObjectMapper objectMapper;

    /**
     * Constructor with all mandatory data.
     *
     * @param contentType Content type used like "application/json; charset=utf-8".
     * @param objectMapper Mapper to use for serializing/deserializing.
     */
    public JacksonObjectSerDeserializer(
            final String contentType,
            final ObjectMapper objectMapper) {
        Contract.requireArgNotNull("contentType", contentType);
        Contract.requireArgNotNull("objectMapper", objectMapper);
        this.contentType = contentType;
        this.objectMapper = objectMapper;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public byte[] serialize(final Object value) {
        Contract.requireArgNotNull("value", value);
        try {
            return objectMapper.writeValueAsBytes(value);
        } catch (final JsonProcessingException ex) {
            throw new RuntimeException("Error serializing object of type: " + value.getClass().getName(), ex);
        }
    }

    @Override
    public <T> T deserialize(final byte[] content, final Class<T> valueType) {
        Contract.requireArgNotNull("content", content);
        Contract.requireArgNotNull("valueType", valueType);
        try {
            return objectMapper.readValue(content, valueType);
        } catch (final IOException ex) {
            throw new RuntimeException("Error deserializing object of type: " + valueType.getName(), ex);
        }
    }

}
