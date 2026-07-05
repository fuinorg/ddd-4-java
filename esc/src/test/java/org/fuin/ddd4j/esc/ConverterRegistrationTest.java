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
package org.fuin.ddd4j.esc;

import org.fuin.esc.api.Converter;
import org.fuin.esc.api.ConverterRegistry;
import org.fuin.esc.api.SerializedDataType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the {@link ConverterRegistration} class.
 */
public class ConverterRegistrationTest {

    private static final SerializedDataType TYPE = new SerializedDataType("MyEvent");

    @Test
    public void testAccessors() {

        // PREPARE
        final Converter<String, String> converter = append("|v2");

        // TEST
        final ConverterRegistration testee = new ConverterRegistration(TYPE, "1", "2", converter);

        // VERIFY
        assertThat(testee.type()).isEqualTo(TYPE);
        assertThat(testee.fromVersion()).isEqualTo("1");
        assertThat(testee.toVersion()).isEqualTo("2");
        assertThat(testee.converter()).isSameAs(converter);

    }

    @Test
    public void testToRegistryUpcasts() {

        // PREPARE: v1 -> v2 -> v3
        final List<ConverterRegistration> registrations = List.of(
                new ConverterRegistration(TYPE, "1", "2", append("|v2")),
                new ConverterRegistration(TYPE, "2", "3", append("|v3")));

        // TEST
        final ConverterRegistry registry = ConverterRegistration.toRegistry(registrations);

        // VERIFY
        assertThat((Object) registry.upcastToLatest(TYPE, "1", "data")).isEqualTo("data|v2|v3");
        assertThat((Object) registry.upcastToLatest(TYPE, "2", "data")).isEqualTo("data|v3");
        assertThat((Object) registry.upcastToLatest(TYPE, "3", "data")).isEqualTo("data");

    }

    @Test
    public void testEmptyRegistryIsPassThrough() {

        // TEST
        final ConverterRegistry registry = ConverterRegistration.toRegistry(List.of());

        // VERIFY
        assertThat((Object) registry.upcastToLatest(TYPE, "1", "data")).isEqualTo("data");

    }

    private static Converter<String, String> append(final String suffix) {
        return new Converter<>() {
            @Override
            public Class<String> getSourceType() {
                return String.class;
            }

            @Override
            public Class<String> getTargetType() {
                return String.class;
            }

            @Override
            public String convert(final String source) {
                return source + suffix;
            }
        };
    }

}
