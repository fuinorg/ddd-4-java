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
import org.fuin.ddd4j.jacksontest.AId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EntityIdJacksonDeserializerTest {

    @Test
    public void testUnmarshal() throws JsonProcessingException {

        final ObjectMapper objectMapper = TestUtils.objectMapper();
        final AId entityId = objectMapper.readValue("\"A 1\"", AId.class);
        assertThat(entityId).isInstanceOf(AId.class);
        assertThat(entityId.getType().asString()).isEqualTo("A");
        assertThat(entityId.asString()).isEqualTo("" + 1L);

    }

}

