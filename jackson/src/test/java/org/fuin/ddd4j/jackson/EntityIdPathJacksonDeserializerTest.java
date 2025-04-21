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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.fuin.ddd4j.core.EntityId;
import org.fuin.ddd4j.core.EntityIdPath;
import org.fuin.ddd4j.jacksontest.AId;
import org.fuin.ddd4j.jacksontest.BId;
import org.fuin.ddd4j.jacksontest.CId;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;

public class EntityIdPathJacksonDeserializerTest {

    @Test
    public void testUnmarshal() throws Exception {

        // PREPARE
        EntityIdPath path;
        Iterator<EntityId> it;
        EntityId first;
        EntityId last;

        // TEST & VERIFY
        final ObjectMapper objectMapper = TestUtils.objectMapper();

        // One
        path = objectMapper.readValue("\"A 1\"", EntityIdPath.class);
        first = path.first();
        last = path.last();
        assertValues(first, AId.class, "A", 1L);
        assertValues(last, AId.class, "A", 1L);
        assertThat(first).isSameAs(last);
        it = path.iterator();
        assertValues(it.next(), AId.class, "A", 1L);
        assertThat(it.hasNext()).isFalse();

        // Two
        path = objectMapper.readValue("\"A 1/B 2\"", EntityIdPath.class);
        first = path.first();
        last = path.last();
        assertValues(first, AId.class, "A", 1L);
        assertValues(last, BId.class, "B", 2L);
        it = path.iterator();
        assertValues(it.next(), AId.class, "A", 1L);
        assertValues(it.next(), BId.class, "B", 2L);
        assertThat(it.hasNext()).isFalse();

        // Three
        path = objectMapper.readValue("\"A 1/B 2/C 3\"", EntityIdPath.class);
        first = path.first();
        last = path.last();
        assertValues(first, AId.class, "A", 1L);
        assertValues(last, CId.class, "C", 3L);
        it = path.iterator();
        assertValues(it.next(), AId.class, "A", 1L);
        assertValues(it.next(), BId.class, "B", 2L);
        assertValues(it.next(), CId.class, "C", 3L);
        assertThat(it.hasNext()).isFalse();
    }

    private void assertValues(EntityId entityId, Class<?> typeClass, String type, long id) {
        assertThat(entityId).isInstanceOf(typeClass);
        assertThat(entityId.getType().asString()).isEqualTo(type);
        assertThat(entityId.asString()).isEqualTo("" + id);
    }

}

