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
import org.fuin.ddd4j.core.EntityIdFactory;
import org.fuin.ddd4j.jacksontest.AId;
import org.fuin.ddd4j.jacksontest.JacksonTestEntityIdFactory;
import org.fuin.ddd4j.jacksontest.VendorId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies that the module registers a (de)serializer for every identifier the factory knows, so that an
 * application no longer has to list its identifiers one by one.
 */
class Ddd4JacksonModuleTest {

    private static final UUID UUID_VALUE = UUID.fromString("f918869c-2e0b-4ec7-a6d1-4c1a08b6c1a2");

    /**
     * A mapper with nothing but the module under test - no identifier is registered by hand, so anything
     * that works here was discovered through {@link EntityIdFactory#getIdClasses()}.
     *
     * @param entityIdFactory Factory the module gets its identifier classes from.
     *
     * @return New mapper.
     */
    private static ObjectMapper mapper(final EntityIdFactory entityIdFactory) {
        return new ObjectMapper().registerModule(new Ddd4JacksonModule(entityIdFactory));
    }

    @Test
    void testWriteAndReadADiscoveredIdentifier() throws Exception {

        final ObjectMapper mapper = mapper(new JacksonTestEntityIdFactory());

        final String json = mapper.writeValueAsString(new AId(1));
        assertThat(json).isEqualTo("""
                "A 1\"""");

        assertThat(mapper.readValue(json, AId.class).asTypedString()).isEqualTo("A 1");

    }

    @Test
    void testWriteAndReadADiscoveredUuidBasedIdentifier() throws Exception {

        final ObjectMapper mapper = mapper(new JacksonTestEntityIdFactory());

        final String json = mapper.writeValueAsString(new VendorId(UUID_VALUE));
        assertThat(json).isEqualTo("""
                "Vendor f918869c-2e0b-4ec7-a6d1-4c1a08b6c1a2\"""");

        assertThat(mapper.readValue(json, VendorId.class)).isEqualTo(new VendorId(UUID_VALUE));

    }

    @Test
    void testAFactoryWithoutKnownClassesRegistersNoIdentifier() throws Exception {

        // The default implementation of getIdClasses() returns an empty set, so an existing factory
        // written before that method existed keeps working - it just contributes no registration.
        final EntityIdFactory factory = new EntityIdFactory() {
            @Override
            public boolean containsType(final String type) {
                return false;
            }

            @Override
            public boolean isValid(final String type, final String id) {
                return false;
            }

            @Override
            public EntityId createEntityId(final String type, final String id) {
                throw new IllegalArgumentException("Unknown type: " + type);
            }
        };
        assertThat(factory.getIdClasses()).isEmpty();

        // Without a registration Jackson falls back to bean serialization: the identifier becomes an
        // object instead of the typed string above.
        assertThat(mapper(factory).writeValueAsString(new AId(1))).startsWith("{");

    }

}
