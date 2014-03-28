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
package org.fuin.ddd4j.common;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.groups.Default;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;

/**
 * Base class for unit tests.
 */
// CHECKSTYLE:OFF
public abstract class BaseTest {

	/**
	 * Marshals the given data.
	 * 
	 * @param data
	 *            Data to serialize.
	 * @param classesToBeBound
	 *            List of java classes to be recognized by the
	 *            {@link JAXBContext}.
	 * 
	 * @return XML data.
	 */
	protected final <T> String marshal(final T data,
			final Class<?>... classesToBeBound) {
		try {
			final JAXBContext ctx = JAXBContext.newInstance(classesToBeBound);
			final Marshaller marshaller = ctx.createMarshaller();
			final StringWriter writer = new StringWriter();
			marshaller.marshal(data, writer);
			return writer.toString();
		} catch (final JAXBException ex) {
			throw new RuntimeException("Error serializing test data", ex);
		}
	}

	/**
	 * Unmarshals the given data.
	 * 
	 * @param xmlData
	 *            XML data.
	 * @param classesToBeBound
	 *            List of java classes to be recognized by the
	 *            {@link JAXBContext}.
	 * 
	 * @return Data.
	 */
	@SuppressWarnings("unchecked")
	protected final <T> T unmarshal(final String xmlData,
			final Class<?>... classesToBeBound) {
		try {
			final JAXBContext ctx = JAXBContext.newInstance(classesToBeBound);
			final Unmarshaller unmarshaller = ctx.createUnmarshaller();
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
			return (T) unmarshaller.unmarshal(new StringReader(xmlData));
		} catch (final JAXBException ex) {
			throw new RuntimeException("Error de-serializing test data", ex);
		}
	}

	/**
	 * Validates the given object.
	 * 
	 * @param obj
	 *            Object to validate.
	 * 
	 * @return Constraint violations.
	 */
	protected final Set<ConstraintViolation<Object>> validate(final Object obj) {
		final ValidatorFactory factory = Validation
				.buildDefaultValidatorFactory();
		final Validator validator = factory.getValidator();
		return validator.validate(obj, Default.class);
	}

}
// CHECKSTYLE:ON
