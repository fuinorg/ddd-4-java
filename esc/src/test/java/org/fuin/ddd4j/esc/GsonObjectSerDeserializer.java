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
package org.fuin.ddd4j.esc;

import com.google.gson.Gson;
import org.fuin.ddd4j.core.ObjectSerDeserializer;
import org.fuin.utils4j.TestOmitted;

import java.nio.charset.StandardCharsets;

/**
 * Simple Gson (JSON) based {@link ObjectSerDeserializer} for tests.
 */
@TestOmitted("Only a test class")
public final class GsonObjectSerDeserializer implements ObjectSerDeserializer {

    private final Gson gson = new Gson();

    @Override
    public String getContentType() {
        return "application/json; charset=utf-8";
    }

    @Override
    public byte[] serialize(final Object value) {
        return gson.toJson(value).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public <T> T deserialize(final byte[] content, final Class<T> valueType) {
        return gson.fromJson(new String(content, StandardCharsets.UTF_8), valueType);
    }

}
