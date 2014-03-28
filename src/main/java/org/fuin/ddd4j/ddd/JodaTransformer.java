package org.fuin.ddd4j.ddd;

import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import cucumber.api.Transformer;
import cucumber.runtime.ParameterInfo;

public final class JodaTransformer extends Transformer<DateTime> {

	private ParameterInfo parameterInfo;

	private Locale locale;

	public final DateTime transform(final String value) {
		final String format = parameterInfo.getFormat();
		final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(
				format).withLocale(locale);
		return dateTimeFormatter.parseDateTime(value);
	}

	public final void setParameterInfoAndLocale(
			final ParameterInfo parameterInfo, final Locale locale) {
		this.parameterInfo = parameterInfo;
		this.locale = locale;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public final boolean canConvert(Class type) {
		return type.equals(DateTime.class);
	}

}
