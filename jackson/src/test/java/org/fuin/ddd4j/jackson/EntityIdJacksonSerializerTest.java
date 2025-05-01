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
import org.fuin.ddd4j.core.EntityId;
import org.fuin.ddd4j.jacksontest.AId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EntityIdJacksonSerializerTest {

    @Test
    public void testMarshal() throws JsonProcessingException {

        final EntityId entityId = new AId(1);
        assertThat(TestUtils.objectMapper()
                .writeValueAsString(entityId)).isEqualTo("\"A 1\"");

    }

}

