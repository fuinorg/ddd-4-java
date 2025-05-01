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

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.fuin.ddd4j.core.AggregateVersion;

import javax.annotation.concurrent.ThreadSafe;
import java.io.IOException;

/**
 * Converts a string into an aggregate version (Jackson).
 */
@ThreadSafe
public final class AggregateVersionJacksonDeserializer extends StdDeserializer<AggregateVersion> {

    /**
     * Default constructor.
     */
    public AggregateVersionJacksonDeserializer() {
        super(AggregateVersion.class);
    }

    @Override
    public AggregateVersion deserialize(JsonParser parser, DeserializationContext context) throws IOException, JacksonException {
        final JsonToken currentToken = parser.getCurrentToken();
        if (currentToken.equals(JsonToken.VALUE_NUMBER_INT)) {
            final int value = parser.getIntValue();
            return AggregateVersion.valueOf(value);
        }
        if (currentToken.equals(JsonToken.VALUE_NULL)) {
            return null;
        }
        return (AggregateVersion) context.handleUnexpectedToken(handledType(), parser);

    }

}
