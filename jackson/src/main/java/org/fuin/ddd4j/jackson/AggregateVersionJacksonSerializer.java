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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.fuin.ddd4j.core.AggregateVersion;

import javax.annotation.concurrent.ThreadSafe;
import java.io.IOException;

/**
 * Converts an aggregate version into a string (Jackson).
 */
@ThreadSafe
public final class AggregateVersionJacksonSerializer extends StdSerializer<AggregateVersion> {

    /**
     * Default constructor.
     */
    public AggregateVersionJacksonSerializer() {
        super(AggregateVersion.class);
    }

    @Override
    public void serialize(AggregateVersion value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value == null) {
            gen.writeNull();
        } else {
            gen.writeNumber(value.asBaseType());
        }
    }

}
