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
import static org.fuin.units4j.Units4JUtils.assertCauseCauseMessage;
import static org.fuin.utils4j.JaxbUtils.XML_PREFIX;
import static org.fuin.utils4j.JaxbUtils.marshal;
import static org.fuin.utils4j.JaxbUtils.unmarshal;
import static org.junit.Assert.fail;

import jakarta.xml.bind.JAXBException;

import org.fuin.objects4j.vo.ValueObjectConverter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

// CHECKSTYLE:OFF Test code
public class AggregateVersionConverterTest {

    private static final String XML = XML_PREFIX + "<data version=\"123\"/>";

    private ValueObjectConverter<Integer, AggregateVersion> testee;

    @Before
    public final void setup() {
        testee = new AggregateVersionConverter();
    }

    @After
    public final void teardown() {
        testee = null;
    }

    @Test
    public final void testFactoryInjectable() {
        assertThat(testee).isNotNull();
    }

    @Test
    public final void testtoVO() {
        assertThat(testee.toVO(123)).isEqualTo(new AggregateVersion(123));
    }

    @Test
    public final void testIsValid() {
        assertThat(testee.isValid(null)).isTrue();
        assertThat(testee.isValid(0)).isTrue();
        assertThat(testee.isValid(-1)).isFalse();
    }

    @Test
    public final void testGetSimpleValueObjectClass() {
        assertThat(testee.getValueObjectClass()).isSameAs(AggregateVersion.class);
    }

    @Test
    public final void testMarshal() throws JAXBException {

        final Data data = new Data();
        data.version = new AggregateVersion(123);
        assertThat(marshal(data, Data.class)).isEqualTo(XML);

    }

    @Test
    public final void testMarshalUnmarshal() throws JAXBException {

        final Data data = unmarshal(XML, Data.class);
        assertThat(data.version).isEqualTo(new AggregateVersion(123));

    }

    @Test
    public final void testUnmarshalError() {

        final String invalidEmailInXmlData = XML_PREFIX + "<data version=\"-1\"/>";
        try {
            unmarshal(invalidEmailInXmlData, Data.class);
            fail("Expected an exception");
        } catch (final RuntimeException ex) {
            assertCauseCauseMessage(ex, "Min value of argument 'version' is 0, but was: -1");
        }

    }

}
// CHECKSTYLE:ON
