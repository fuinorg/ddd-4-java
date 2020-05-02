/**
 * Copyright (C) 2015 Michael Schnell. All rights reserved. 
 * http://www.fuin.org/
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see http://www.gnu.org/licenses/.
 */
package org.fuin.ddd4j.ddd;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fuin.utils4j.JaxbUtils.marshal;
import static org.fuin.utils4j.JaxbUtils.unmarshal;
import static org.fuin.utils4j.Utils4J.deserialize;
import static org.fuin.utils4j.Utils4J.serialize;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;

import org.eclipse.yasson.FieldAccessStrategy;
import org.fuin.ddd4j.test.AId;
import org.fuin.ddd4j.test.BId;
import org.junit.Test;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Diff;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

/**
 * Tests for {@link EntityNotFoundException}.
 */
// CHECKSTYLE:OFF Disabled for test
public class EntityNotFoundExceptionTest {

    @Test
    public void testSerializeDeserialize() {

        // PREPARE
        final EntityNotFoundException original = new EntityNotFoundException(new EntityIdPath(new AId(1L)), new BId(1));

        // TEST
        final byte[] data = serialize(original);
        final EntityNotFoundException copy = deserialize(data);

        // VERIFY
        assertThat(copy.getShortId()).isEqualTo(original.getShortId());
        assertThat(copy.getParentIdPath()).isEqualTo(original.getParentIdPath());
        assertThat(copy.getEntityId()).isEqualTo(original.getEntityId());
        assertThat(copy.getMessage()).isEqualTo(original.getMessage());

    }

    @Test
    public void testHashCodeEquals() {
        EqualsVerifier.forClass(EntityNotFoundException.Data.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }
    
    @Test
    public final void testMarshalUnmarshalXML() throws Exception {

        // PREPARE
        final EntityNotFoundException original = new EntityNotFoundException(new EntityIdPath(new AId(1L)), new BId(2));

        // TEST
        final String xml = marshal(original.getData(), EntityNotFoundException.Data.class);

        // VERIFY
        final Diff documentDiff = DiffBuilder.compare("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<entity-not-found-exception>" + "<msg>B 2 not found in A 1</msg>" + "<sid>DDD4J-ENTITY_NOT_FOUND</sid>"
                + "<parent-id-path>A 1</parent-id-path>" + "<entity-id>2</entity-id>" + "</entity-not-found-exception>").withTest(xml)
                .ignoreWhitespace().build();

        assertThat(documentDiff.hasDifferences()).describedAs(documentDiff.toString()).isFalse();

        // TEST
        final EntityNotFoundException.Data data = unmarshal(xml, EntityNotFoundException.Data.class);
        final EntityNotFoundException copy = data.toException();

        // VERIFY
        assertThat(copy.getShortId()).isEqualTo(original.getShortId());
        assertThat(copy.getParentIdPath()).isEqualTo(original.getParentIdPath());
        assertThat(copy.getEntityId()).isEqualTo(original.getEntityId());
        assertThat(copy.getMessage()).isEqualTo(original.getMessage());

    }

    @Test
    public final void testMarshalUnmarshalJSON() throws Exception {

        // PREPARE
        final EntityNotFoundException original = new EntityNotFoundException(new EntityIdPath(new AId(1L)), new BId(2));

        // TEST
        final String json = jsonb().toJson(original.getData());
        final EntityNotFoundException.Data data = jsonb().fromJson(json, EntityNotFoundException.Data.class);
        final EntityNotFoundException copy = data.toException();

        // VERIFY
        assertThat(copy.getShortId()).isEqualTo(original.getShortId());
        assertThat(copy.getParentIdPath()).isEqualTo(original.getParentIdPath());
        assertThat(copy.getEntityId()).isEqualTo(original.getEntityId());
        assertThat(copy.getMessage()).isEqualTo(original.getMessage());

    }
    
    private static Jsonb jsonb() {
        final JsonbConfig config = new JsonbConfig().withPropertyVisibilityStrategy(new FieldAccessStrategy());
        return JsonbBuilder.create(config);
    }

}
// CHECKSTYLE:ON
