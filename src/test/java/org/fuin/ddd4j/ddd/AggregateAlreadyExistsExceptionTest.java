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

import java.util.UUID;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;

import org.eclipse.yasson.FieldAccessStrategy;
import org.fuin.ddd4j.test.VendorId;
import org.junit.Test;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Diff;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

/**
 * Tests for {@link AggregateAlreadyExistsException}.
 */
// CHECKSTYLE:OFF Disabled for test
public class AggregateAlreadyExistsExceptionTest {

    @Test
    public void testSerializeDeserialize() {

        // PREPARE
        final VendorId vendorId = new VendorId(UUID.fromString("4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119"));
        final AggregateAlreadyExistsException original = new AggregateAlreadyExistsException(VendorId.ENTITY_TYPE, vendorId, 102);

        // TEST
        final byte[] data = serialize(original);
        final AggregateAlreadyExistsException copy = deserialize(data);

        // VERIFY
        assertThat(copy.getShortId()).isEqualTo(original.getShortId());
        assertThat(copy.getAggregateType()).isEqualTo(original.getAggregateType());
        assertThat(copy.getAggregateId()).isEqualTo(original.getAggregateId());
        assertThat(copy.getMessage()).isEqualTo(original.getMessage());
        assertThat(copy.getVersion()).isEqualTo(original.getVersion());

    }

    @Test
    public void testHashCodeEquals() {
        EqualsVerifier.forClass(AggregateAlreadyExistsException.Data.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public final void testMarshalUnmarshalXML() throws Exception {

        // PREPARE
        final AggregateAlreadyExistsException original = new AggregateAlreadyExistsException(VendorId.ENTITY_TYPE,
                new VendorId(UUID.fromString("4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119")), 102);

        // TEST
        final String xml = marshal(original.getData(), AggregateAlreadyExistsException.Data.class);

        // VERIFY
        final Diff documentDiff = DiffBuilder
                .compare("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + "<aggregate-already-exists-exception>"
                        + "<msg>Vendor 4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119 already exists (version=102)</msg>"
                        + "<sid>DDD4J-AGGREGATE_ALREADY_EXISTS</sid>" + "<aggregate-type>Vendor</aggregate-type>"
                        + "<aggregate-id>4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119</aggregate-id>" + "<version>102</version>"
                        + "</aggregate-already-exists-exception>")
                .withTest(xml).ignoreWhitespace().build();

        assertThat(documentDiff.hasDifferences()).describedAs(documentDiff.toString()).isFalse();

        // TEST
        final AggregateAlreadyExistsException.Data data = unmarshal(xml, AggregateAlreadyExistsException.Data.class);
        final AggregateAlreadyExistsException copy = data.toException();

        // VERIFY
        assertThat(copy.getShortId()).isEqualTo(original.getShortId());
        assertThat(copy.getAggregateType()).isEqualTo(original.getAggregateType());
        assertThat(copy.getAggregateId()).isEqualTo(original.getAggregateId());
        assertThat(copy.getMessage()).isEqualTo(original.getMessage());
        assertThat(copy.getVersion()).isEqualTo(original.getVersion());

    }

    @Test
    public final void testMarshalUnmarshalJSON() throws Exception {

        // PREPARE
        final AggregateAlreadyExistsException original = new AggregateAlreadyExistsException(VendorId.ENTITY_TYPE,
                new VendorId(UUID.fromString("4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119")), 102);

        // TEST
        final String json = jsonb().toJson(original.getData());
        final AggregateAlreadyExistsException.Data data = jsonb().fromJson(json, AggregateAlreadyExistsException.Data.class);
        final AggregateAlreadyExistsException copy = data.toException();

        // VERIFY
        assertThat(copy.getShortId()).isEqualTo(original.getShortId());
        assertThat(copy.getAggregateType()).isEqualTo(original.getAggregateType());
        assertThat(copy.getAggregateId()).isEqualTo(original.getAggregateId());
        assertThat(copy.getMessage()).isEqualTo(original.getMessage());
        assertThat(copy.getVersion()).isEqualTo(original.getVersion());

    }

    private static Jsonb jsonb() {
        final JsonbConfig config = new JsonbConfig().withPropertyVisibilityStrategy(new FieldAccessStrategy());
        return JsonbBuilder.create(config);
    }

}
// CHECKSTYLE:ON
