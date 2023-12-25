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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.fuin.utils4j.jaxb.JaxbUtils.XML_PREFIX;
import static org.fuin.utils4j.jaxb.JaxbUtils.marshal;
import static org.fuin.utils4j.jaxb.JaxbUtils.unmarshal;

import org.fuin.objects4j.common.ConstraintViolationException;
import org.fuin.objects4j.vo.ValueObjectConverter;
import org.fuin.utils4j.jaxb.UnmarshallerBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.xml.bind.JAXBException;

// CHECKSTYLE:OFF Test code
public class AggregateVersionConverterTest {

    private static final String XML = XML_PREFIX + "<data version=\"123\"/>";

    @Test
    public final void testtoVO() {
        assertThat(new AggregateVersionConverter().toVO(123)).isEqualTo(new AggregateVersion(123));
    }

    @Test
    public final void testIsValid() {
        assertThat(new AggregateVersionConverter().isValid(null)).isTrue();
        assertThat(new AggregateVersionConverter().isValid(0)).isTrue();
        assertThat(new AggregateVersionConverter().isValid(-1)).isFalse();
    }

    @Test
    public final void testGetSimpleValueObjectClass() {
        assertThat(new AggregateVersionConverter().getValueObjectClass()).isSameAs(AggregateVersion.class);
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
        assertThatThrownBy(() -> {
            unmarshal(new UnmarshallerBuilder().addClassesToBeBound(Data.class).withHandler(event -> false).build(), invalidEmailInXmlData);
        }).hasRootCauseInstanceOf(ConstraintViolationException.class)
          .hasRootCauseMessage("Min value of argument 'version' is 0, but was: -1");

    }

}
// CHECKSTYLE:ON
