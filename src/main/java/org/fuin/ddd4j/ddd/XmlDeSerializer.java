/**
 * Copyright (C) 2013 Future Invent Informationsmanagement GmbH. All rights
 * reserved. <http://www.fuin.org/>
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
 * along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fuin.ddd4j.ddd;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;

/**
 * Serializes and deserializes a class of a given type that only has one version
 * and is always "application/xml" + "utf-8" encoded.
 */
public final class XmlDeSerializer implements Serializer, Deserializer {

    private final String type;

    private final int version;

    private final String mimeType;

    private final Charset charset;

    private final Marshaller marshaller;

    private final Unmarshaller unmarshaller;

    /**
     * Constructor with type and JAXB context classes.
     * 
     * @param type
     *            Type that can be serialized/deserialized.
     * @param classesToBeBound
     *            Classes to use for the JAXB context.
     */
    public XmlDeSerializer(final String type,
	    final Class<?>... classesToBeBound) {
	super();
	this.type = type;
	this.version = 1;
	this.mimeType = "application/xml";
	this.charset = Charset.forName("utf-8");
	try {
	    final JAXBContext ctx = JAXBContext.newInstance(classesToBeBound);
	    marshaller = ctx.createMarshaller();
	    unmarshaller = ctx.createUnmarshaller();
	    unmarshaller.setEventHandler(new ValidationEventHandler() {
		@Override
		public boolean handleEvent(final ValidationEvent event) {
		    if (event.getSeverity() > 0) {
			if (event.getLinkedException() == null) {
			    throw new RuntimeException(
				    "Error unmarshalling the data: "
					    + event.getMessage());
			}
			throw new RuntimeException(
				"Error unmarshalling the data", event
					.getLinkedException());
		    }
		    return true;
		}
	    });
	} catch (final JAXBException ex) {
	    throw new RuntimeException(
		    "Error initializing JAXB helper classes", ex);
	}
    }

    @Override
    public final String getType() {
	return type;
    }

    @Override
    public final int getVersion() {
	return version;
    }

    @Override
    public final String getMimeType() {
	return mimeType;
    }

    @Override
    public final Charset getEncoding() {
	return charset;
    }

    @Override
    public final byte[] marshal(final Object obj) {
	try {
	    final ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
	    marshaller.marshal(obj, bos);
	    return bos.toByteArray();
	} catch (final JAXBException ex) {
	    throw new RuntimeException("Error serializing data", ex);
	}
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <T> T unmarshal(final byte[] data) {
	try {
	    final ByteArrayInputStream bais = new ByteArrayInputStream(data);
	    return (T) unmarshaller.unmarshal(bais);
	} catch (final JAXBException ex) {
	    throw new RuntimeException("Error de-serializing data", ex);
	}
    }

}
