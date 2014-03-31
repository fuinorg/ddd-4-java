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

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.AttributeConverter;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.fuin.objects4j.common.ThreadSafe;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Converts Joda {@link DateTime} objects.
 */
@ThreadSafe
@ApplicationScoped
public final class DateTimeConverter extends XmlAdapter<String, DateTime>
		implements AttributeConverter<DateTime, String> {

	private final DateTimeFormatter dateTimeFormatter = DateTimeFormat
			.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");

	private DateTime toVO(final String value) {
		return dateTimeFormatter.parseDateTime(value);
	}

	private String fromVO(final DateTime value) {
		if (value == null) {
			return null;
		}
		return dateTimeFormatter.print(value);
	}

	@Override
	public final String marshal(final DateTime value) throws Exception {
		return fromVO(value);
	}

	@Override
	public final DateTime unmarshal(final String value) throws Exception {
		return toVO(value);
	}

	@Override
	public final String convertToDatabaseColumn(final DateTime value) {
		return fromVO(value);
	}

	@Override
	public final DateTime convertToEntityAttribute(final String value) {
		return toVO(value);
	}

}
