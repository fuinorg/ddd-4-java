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

import org.fuin.ddd4j.test.VendorId;
import org.junit.Test;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Diff;

/**
 * Tests for {@link AggregateVersionConflictException}.
 */
// CHECKSTYLE:OFF Disabled for test
public class AggregateVersionConflictExceptionTest {

    @Test
    public void testSerializeDeserialize() {

        // PREPARE
        final VendorId vendorId = new VendorId(UUID.fromString("4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119"));
        final AggregateVersionConflictException original = new AggregateVersionConflictException(VendorId.ENTITY_TYPE, vendorId, 47, 102);

        // TEST
        final byte[] data = serialize(original);
        final AggregateVersionConflictException copy = deserialize(data);

        // VERIFY
        assertThat(copy.getShortId()).isEqualTo(original.getShortId());
        assertThat(copy.getAggregateType()).isEqualTo(original.getAggregateType());
        assertThat(copy.getAggregateId()).isEqualTo(original.getAggregateId());
        assertThat(copy.getMessage()).isEqualTo(original.getMessage());
        assertThat(copy.getExpected()).isEqualTo(original.getExpected());
        assertThat(copy.getActual()).isEqualTo(original.getActual());

    }

    @Test
    public final void testMarshalUnmarshalXML() throws Exception {

        // PREPARE
        final AggregateVersionConflictException original = new AggregateVersionConflictException(VendorId.ENTITY_TYPE,
                new VendorId(UUID.fromString("4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119")), 47, 102);

        // TEST
        final String xml = marshal(original, AggregateVersionConflictException.class);

        // VERIFY
        final Diff documentDiff = DiffBuilder
                .compare("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + "<aggregate-version-conflict-exception>"
                        + "<msg>Expected version 47 for Vendor (4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119), but was 102</msg>"
                        + "<sid>DDD4J-AGGREGATE_VERSION_CONFLICT</sid>" + "<aggregate-type>Vendor</aggregate-type>"
                        + "<aggregate-id>4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119</aggregate-id>" + "<expected>47</expected>"
                        + "<actual>102</actual>" + "</aggregate-version-conflict-exception>")
                .withTest(xml).ignoreWhitespace().build();

        assertThat(documentDiff.hasDifferences()).describedAs(documentDiff.toString()).isFalse();

        // TEST
        final AggregateVersionConflictException copy = unmarshal(xml, AggregateVersionConflictException.class);

        // VERIFY
        assertThat(copy.getShortId()).isEqualTo(original.getShortId());
        assertThat(copy.getAggregateType()).isEqualTo(original.getAggregateType());
        assertThat(copy.getAggregateId()).isEqualTo(original.getAggregateId());
        assertThat(copy.getMessage()).isEqualTo(original.getMessage());
        assertThat(copy.getExpected()).isEqualTo(original.getExpected());
        assertThat(copy.getActual()).isEqualTo(original.getActual());

    }

}
// CHECKSTYLE:ON
