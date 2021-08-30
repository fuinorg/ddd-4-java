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
 * Tests for {@link AggregateVersionNotFoundException}.
 */
// CHECKSTYLE:OFF Disabled for test
public class AggregateVersionNotFoundExceptionTest {

    @Test
    public void testSerializeDeserialize() {

        // PREPARE
        final VendorId vendorId = new VendorId(UUID.fromString("4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119"));
        final AggregateVersionNotFoundException original = new AggregateVersionNotFoundException(VendorId.ENTITY_TYPE, vendorId, 47);

        // TEST
        final byte[] data = serialize(original);
        final AggregateVersionNotFoundException copy = deserialize(data);

        // VERIFY
        assertThat(copy.getShortId()).isEqualTo(original.getShortId());
        assertThat(copy.getAggregateType()).isEqualTo(original.getAggregateType());
        assertThat(copy.getAggregateId()).isEqualTo(original.getAggregateId());
        assertThat(copy.getMessage()).isEqualTo(original.getMessage());
        assertThat(copy.getVersion()).isEqualTo(original.getVersion());

    }

    @Test
    public void testHashCodeEquals() {
        EqualsVerifier.forClass(AggregateVersionNotFoundException.Data.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }
    
    @Test
    public final void testMarshalUnmarshalXML() throws Exception {

        // PREPARE
        final AggregateVersionNotFoundException original = new AggregateVersionNotFoundException(VendorId.ENTITY_TYPE,
                new VendorId(UUID.fromString("4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119")), 47);

        // TEST
        final String xml = marshal(original.getData(), AggregateVersionNotFoundException.Data.class);

        // VERIFY
        final Diff documentDiff = DiffBuilder
                .compare("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + "<aggregate-version-not-found-exception>"
                        + "<msg>Requested version 47 for Vendor (4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119) does not exist</msg>"
                        + "<sid>DDD4J-AGGREGATE_VERSION_NOT_FOUND</sid>" + "<aggregate-type>Vendor</aggregate-type>"
                        + "<aggregate-id>4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119</aggregate-id>" + "<version>47</version>"
                        + "</aggregate-version-not-found-exception>")
                .withTest(xml).ignoreWhitespace().build();

        assertThat(documentDiff.hasDifferences()).describedAs(documentDiff.toString()).isFalse();

        // TEST
        final AggregateVersionNotFoundException.Data data = unmarshal(xml, AggregateVersionNotFoundException.Data.class);
        final AggregateVersionNotFoundException copy = data.toException();

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
        final AggregateVersionNotFoundException original = new AggregateVersionNotFoundException(VendorId.ENTITY_TYPE,
                new VendorId(UUID.fromString("4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119")), 47);

        // TEST
        final String json = jsonb().toJson(original.getData());
        final AggregateVersionNotFoundException.Data data = jsonb().fromJson(json, AggregateVersionNotFoundException.Data.class);
        final AggregateVersionNotFoundException copy = data.toException();

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
