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
import org.fuin.ddd4j.jaxbtest.AId;
import org.fuin.ddd4j.jaxbtest.JaxbTestEntityIdFactory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EntityIdXmlAdapterTest {

    @Test
    public void testUnmarshal() {

        // PREPARE
        final EntityIdXmlAdapter testee = new EntityIdXmlAdapter(new JaxbTestEntityIdFactory());
        final EntityId entityId = testee.unmarshal("A 1");

        // TEST & VERIFY
        assertThat(entityId).isInstanceOf(AId.class);
        assertThat(entityId.getType().asString()).isEqualTo("A");
        assertThat(entityId.asString()).isEqualTo("" + 1L);

    }

    @Test
    public void testMarshal() {

        // PREPARE
        final EntityIdXmlAdapter testee = new EntityIdXmlAdapter(new JaxbTestEntityIdFactory());
        final EntityId entityId = new AId(1);

        // TEST & VERIFY
        assertThat(testee.marshal(entityId)).isEqualTo("A 1");

    }

}
