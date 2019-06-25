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

import org.junit.Test;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Diff;

/**
 * Tests for {@link EncryptionKeyIdUnknownException}.
 */
// CHECKSTYLE:OFF Disabled for test
public class EncryptionKeyIdUnknownExceptionTest {

    @Test
    public void testSerializeDeserialize() {

        // PREPARE
        final String keyId = "CUSTOMER " + UUID.fromString("4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119");
        final EncryptionKeyIdUnknownException original = new EncryptionKeyIdUnknownException(keyId);

        // TEST
        final byte[] data = serialize(original);
        final EncryptionKeyIdUnknownException copy = deserialize(data);

        // VERIFY
        assertThat(copy.getShortId()).isEqualTo(original.getShortId());
        assertThat(copy.getKeyId()).isEqualTo(original.getKeyId());
        assertThat(copy.getMessage()).isEqualTo(original.getMessage());

    }

    @Test
    public final void testMarshalUnmarshalXML() throws Exception {

        // PREPARE
        final String keyId = "CUSTOMER " + UUID.fromString("4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119");
        final EncryptionKeyIdUnknownException original = new EncryptionKeyIdUnknownException(keyId);

        // TEST
        final String xml = marshal(original, EncryptionKeyIdUnknownException.class);

        // VERIFY
        final Diff documentDiff = DiffBuilder.compare("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<encryption-key-id-unknown-exception>" + "<msg>Unknown keyId: CUSTOMER 4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119</msg>"
                + "<sid>DDD4J-ENCRYPTION_KEY_ID_UNKNOWN</sid>" + "<key-id>CUSTOMER 4dcf4c2c-10e1-4db9-ba9e-d1e644e9d119</key-id>"
                + "</encryption-key-id-unknown-exception>").withTest(xml).ignoreWhitespace().build();

        assertThat(documentDiff.hasDifferences()).describedAs(documentDiff.toString()).isFalse();

        // TEST
        final EncryptionKeyIdUnknownException copy = unmarshal(xml, EncryptionKeyIdUnknownException.class);

        // VERIFY
        assertThat(copy.getShortId()).isEqualTo(original.getShortId());
        assertThat(copy.getKeyId()).isEqualTo(original.getKeyId());

    }

}
// CHECKSTYLE:ON
