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

import org.fuin.objects4j.common.ThreadSafe;
import org.fuin.objects4j.vo.AbstractValueObjectConverter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Converts Joda {@link DateTime} objects.
 */
@ThreadSafe
@ApplicationScoped
public final class DateTimeConverter extends
		AbstractValueObjectConverter<String, DateTime> implements
		AttributeConverter<DateTime, String> {

	private final DateTimeFormatter dateTimeFormatter = DateTimeFormat
			.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");

	@Override
	public Class<String> getBaseTypeClass() {
		return String.class;
	}

	@Override
	public final Class<DateTime> getValueObjectClass() {
		return DateTime.class;
	}

	@Override
	public final boolean isValid(final String value) {
		try {
			// TODO Should be replaced by something without throwing
			// exception...
			dateTimeFormatter.parseDateTime(value);
			return true;
		} catch (final RuntimeException ex) {
			return false;
		}
	}

	@Override
	public final DateTime toVO(final String value) {
		return dateTimeFormatter.parseDateTime(value);
	}

	@Override
	public final String fromVO(final DateTime value) {
		if (value == null) {
			return null;
		}
		return value.toString();
	}

}
