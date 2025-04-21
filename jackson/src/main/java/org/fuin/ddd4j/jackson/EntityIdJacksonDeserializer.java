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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.fuin.ddd4j.core.EntityId;
import org.fuin.ddd4j.core.EntityIdFactory;
import org.fuin.objects4j.common.ValueOfCapable;

import javax.annotation.concurrent.ThreadSafe;
import java.io.IOException;

/**
 * Jackson deserializer for an entity identifier.
 */
@ThreadSafe
public final class EntityIdJacksonDeserializer<T extends EntityId> extends StdDeserializer<T> {

    private final ValueOfCapable<T> vop;

    /**
     * Constructor with target type and factory.
     *
     * @param clasz Entity ID class.
     * @param factory Factory to use.
     */
    @SuppressWarnings("unchecked")
    public EntityIdJacksonDeserializer(final Class<T> clasz, final EntityIdFactory factory) {
        super(clasz);
        vop = str -> (T) EntityId.valueOf(factory, str);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonToken currentToken = parser.getCurrentToken();
        if (currentToken.equals(JsonToken.VALUE_STRING)) {
            String value = parser.getText().trim();
            return this.vop.valueOf(value);
        } else {
            return (T) (currentToken.equals(JsonToken.VALUE_NULL) ? null : context.handleUnexpectedToken(this.handledType(), parser));
        }
    }

}
