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

import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import cucumber.api.Transformer;
import cucumber.runtime.ParameterInfo;

/**
 * Transforms a Joda date/time for cucumber.
 */
public final class JodaTransformer extends Transformer<DateTime> {

    private ParameterInfo parameterInfo;

    private Locale locale;

    @Override
    public final DateTime transform(final String value) {
	final String format = parameterInfo.getFormat();
	final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(
		format).withLocale(locale);
	return dateTimeFormatter.parseDateTime(value);
    }

    @Override
    public final void setParameterInfoAndLocale(
	    final ParameterInfo parameterInfo, final Locale locale) {
	this.parameterInfo = parameterInfo;
	this.locale = locale;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public final boolean canConvert(final Class type) {
	return type.equals(DateTime.class);
    }

}
