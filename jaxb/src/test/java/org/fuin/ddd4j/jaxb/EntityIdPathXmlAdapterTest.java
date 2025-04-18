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
package org.fuin.ddd4j.jaxb;

import org.fuin.ddd4j.core.EntityId;
import org.fuin.ddd4j.core.EntityIdPath;
import org.fuin.ddd4j.jaxbtest.AId;
import org.fuin.ddd4j.jaxbtest.BId;
import org.fuin.ddd4j.jaxbtest.CId;
import org.fuin.ddd4j.jaxbtest.JaxbTestEntityIdFactory;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;

public class EntityIdPathXmlAdapterTest {

    @Test
    public void testMarshal() {

        // PREPARE
        final EntityIdPathXmlAdapter testee = new EntityIdPathXmlAdapter(new JaxbTestEntityIdFactory());

        // TEST & VERIFY
        assertThat(testee.marshal(null)).isNull();
        assertThat(testee.marshal(new EntityIdPath(new AId(1)))).isEqualTo("A 1");
        assertThat(testee.marshal(new EntityIdPath(new AId(1), new BId(2)))).isEqualTo("A 1/B 2");
        assertThat(testee.marshal(new EntityIdPath(new AId(1), new BId(2), new CId(3)))).isEqualTo("A 1/B 2/C 3");

    }

    @Test
    public void testUnmarshal() {

        // PREPARE
        final EntityIdPathXmlAdapter testee = new EntityIdPathXmlAdapter(new JaxbTestEntityIdFactory());
        EntityIdPath path;
        Iterator<EntityId> it;
        EntityId first;
        EntityId last;

        // TEST & VERIFY

        // null
        assertThat(testee.unmarshal(null)).isNull();

        // One
        path = testee.unmarshal("A 1");
        first = path.first();
        last = path.last();
        assertValues(first, AId.class, "A", 1L);
        assertValues(last, AId.class, "A", 1L);
        assertThat(first).isSameAs(last);
        it = path.iterator();
        assertValues(it.next(), AId.class, "A", 1L);
        assertThat(it.hasNext()).isFalse();

        // Two
        path = testee.unmarshal("A 1/B 2");
        first = path.first();
        last = path.last();
        assertValues(first, AId.class, "A", 1L);
        assertValues(last, BId.class, "B", 2L);
        it = path.iterator();
        assertValues(it.next(), AId.class, "A", 1L);
        assertValues(it.next(), BId.class, "B", 2L);
        assertThat(it.hasNext()).isFalse();

        // Three
        path = testee.unmarshal("A 1/B 2/C 3");
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

