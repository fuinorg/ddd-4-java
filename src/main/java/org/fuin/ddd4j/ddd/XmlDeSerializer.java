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

public class XmlDeSerializer implements Serializer, Deserializer {

	private final String type;

	private final int version;

	private final String mimeType;

	private final Charset charset;

	private final Marshaller marshaller;

	private final Unmarshaller unmarshaller;

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
	public String getType() {
		return type;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public String getMimeType() {
		return mimeType;
	}

	@Override
	public Charset getEncoding() {
		return charset;
	}

	@Override
	public byte[] marshal(final Object obj) {
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
	public <T> T unmarshal(byte[] data) {
		try {
			final ByteArrayInputStream bais = new ByteArrayInputStream(data);
			return (T) unmarshaller.unmarshal(bais);
		} catch (final JAXBException ex) {
			throw new RuntimeException("Error de-serializing data", ex);
		}
	}

}
