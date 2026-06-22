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
package org.fuin.ddd4j.jsonb;

import jakarta.json.bind.Jsonb;
import org.fuin.ddd4j.core.ObjectSerDeserializer;
import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.common.ThreadSafe;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Serializes/deserializes an object using JSON-B ({@link Jsonb}).
 */
@ThreadSafe
public final class JsonbObjectSerDeserializer implements ObjectSerDeserializer {

    private final Jsonb jsonb;

    private final Charset charset;

    /**
     * Constructor with {@link StandardCharsets#UTF_8} as encoding.
     *
     * @param jsonb JSON-B instance to use for serializing/deserializing.
     */
    public JsonbObjectSerDeserializer(final Jsonb jsonb) {
        this(jsonb, StandardCharsets.UTF_8);
    }

    /**
     * Constructor with all mandatory data.
     *
     * @param jsonb   JSON-B instance to use for serializing/deserializing.
     * @param charset Character set used to convert the JSON to/from a byte array.
     */
    public JsonbObjectSerDeserializer(final Jsonb jsonb, final Charset charset) {
        Contract.requireArgNotNull("jsonb", jsonb);
        Contract.requireArgNotNull("charset", charset);
        this.jsonb = jsonb;
        this.charset = charset;
    }

    @Override
    public String getContentType() {
        return "application/json; charset=" + charset.name();
    }

    @Override
    public byte[] serialize(final Object value) {
        Contract.requireArgNotNull("value", value);
        return jsonb.toJson(value).getBytes(charset);
    }

    @Override
    public <T> T deserialize(final byte[] content, final Class<T> valueType) {
        Contract.requireArgNotNull("content", content);
        Contract.requireArgNotNull("valueType", valueType);
        return jsonb.fromJson(new String(content, charset), valueType);
    }

}
