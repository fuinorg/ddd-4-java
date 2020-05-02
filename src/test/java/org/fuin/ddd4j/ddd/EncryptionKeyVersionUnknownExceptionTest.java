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
import org.junit.Test;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Diff;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

/**
 * Tests for {@link EncryptionKeyVersionUnknownException}.
 */
// CHECKSTYLE:OFF Disabled for test
public class EncryptionKeyVersionUnknownExceptionTest {

    @Test
    public void testSerializeDeserialize() {

        // PREPARE
        final String keyVersion = "v2";
        final EncryptionKeyVersionUnknownException original = new EncryptionKeyVersionUnknownException(keyVersion);

        // TEST
        final byte[] data = serialize(original);
        final EncryptionKeyVersionUnknownException copy = deserialize(data);

        // VERIFY
        assertThat(copy.getShortId()).isEqualTo(original.getShortId());
        assertThat(copy.getKeyVersion()).isEqualTo(original.getKeyVersion());
        assertThat(copy.getMessage()).isEqualTo(original.getMessage());

    }

    @Test
    public void testHashCodeEquals() {
        EqualsVerifier.forClass(EncryptionKeyVersionUnknownException.Data.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }
    
    @Test
    public final void testMarshalUnmarshalXML() throws Exception {

        // PREPARE
        final String keyVersion = "v3";
        final EncryptionKeyVersionUnknownException original = new EncryptionKeyVersionUnknownException(keyVersion);

        // TEST
        final String xml = marshal(original.getData(), EncryptionKeyVersionUnknownException.Data.class);

        // VERIFY
        final Diff documentDiff = DiffBuilder
                .compare("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + "<encryption-key-version-unknown-exception>"
                        + "<msg>Unknown keyVersion: v3</msg>" + "<sid>DDD4J-ENCRYPTION_KEY_VERSION_UNKNOWN</sid>"
                        + "<key-version>v3</key-version>" + "</encryption-key-version-unknown-exception>")
                .withTest(xml).ignoreWhitespace().build();

        assertThat(documentDiff.hasDifferences()).describedAs(documentDiff.toString()).isFalse();

        // TEST
        final EncryptionKeyVersionUnknownException.Data data = unmarshal(xml, EncryptionKeyVersionUnknownException.Data.class);
        final EncryptionKeyVersionUnknownException copy = data.toException();

        // VERIFY
        assertThat(copy.getShortId()).isEqualTo(original.getShortId());
        assertThat(copy.getKeyVersion()).isEqualTo(original.getKeyVersion());
        assertThat(copy.getMessage()).isEqualTo(original.getMessage());

    }

    @Test
    public final void testMarshalUnmarshalJSON() throws Exception {

        // PREPARE
        final String keyVersion = "v3";
        final EncryptionKeyVersionUnknownException original = new EncryptionKeyVersionUnknownException(keyVersion);

        // TEST
        final String json = jsonb().toJson(original.getData());
        final EncryptionKeyVersionUnknownException.Data data = jsonb().fromJson(json, EncryptionKeyVersionUnknownException.Data.class);
        final EncryptionKeyVersionUnknownException copy = data.toException();

        // VERIFY
        assertThat(copy.getShortId()).isEqualTo(original.getShortId());
        assertThat(copy.getKeyVersion()).isEqualTo(original.getKeyVersion());
        assertThat(copy.getMessage()).isEqualTo(original.getMessage());

    }
    
    private static Jsonb jsonb() {
        final JsonbConfig config = new JsonbConfig().withPropertyVisibilityStrategy(new FieldAccessStrategy());
        return JsonbBuilder.create(config);
    }

}
// CHECKSTYLE:ON
