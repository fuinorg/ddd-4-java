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
package org.fuin.ddd4j.jaxb;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import org.fuin.ddd4j.core.ObjectSerDeserializer;
import org.fuin.objects4j.common.Contract;
import org.fuin.utils4j.jaxb.JaxbUtils;

import javax.annotation.concurrent.ThreadSafe;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Serializes/deserializes an object using JAXB. The {@link JAXBContext} has to
 * be created with all classes that should be (de-)serialized bound to it. A new
 * {@code Marshaller}/{@code Unmarshaller} is created internally for every call,
 * which keeps this class thread-safe (in contrast to a {@code Marshaller} or
 * {@code Unmarshaller} that must not be shared between threads).
 */
@ThreadSafe
public final class JaxbObjectSerDeserializer implements ObjectSerDeserializer {

    private final JAXBContext jaxbContext;

    private final Charset charset;

    private final XmlAdapter<?, ?>[] adapters;

    /**
     * Constructor with {@link StandardCharsets#UTF_8} as encoding and no adapters.
     *
     * @param jaxbContext Context with all bound classes used for serializing/deserializing.
     */
    public JaxbObjectSerDeserializer(final JAXBContext jaxbContext) {
        this(jaxbContext, StandardCharsets.UTF_8);
    }

    /**
     * Constructor with all data.
     *
     * @param jaxbContext Context with all bound classes used for serializing/deserializing.
     * @param charset     Character set used to convert the XML to/from a byte array.
     * @param adapters    Adapters to use (must be thread-safe / stateless as they are shared between calls).
     */
    public JaxbObjectSerDeserializer(final JAXBContext jaxbContext, final Charset charset, final XmlAdapter<?, ?>... adapters) {
        Contract.requireArgNotNull("jaxbContext", jaxbContext);
        Contract.requireArgNotNull("charset", charset);
        Contract.requireArgNotNull("adapters", adapters);
        this.jaxbContext = jaxbContext;
        this.charset = charset;
        this.adapters = adapters.clone();
    }

    @Override
    public byte[] serialize(final Object value) {
        Contract.requireArgNotNull("value", value);
        final String xml = Objects.requireNonNull(JaxbUtils.marshal(jaxbContext, value, adapters), "marshal result");
        return xml.getBytes(charset);
    }

    @Override
    public <T> T deserialize(final byte[] content, final Class<T> valueType) {
        Contract.requireArgNotNull("content", content);
        Contract.requireArgNotNull("valueType", valueType);
        final Object obj = Objects.requireNonNull(JaxbUtils.unmarshal(jaxbContext, new String(content, charset), adapters),
                "unmarshal result");
        return valueType.cast(obj);
    }

}
