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
package org.fuin.ddd4j.common;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Java zoned date time JAXB adapter that uses {@link DateTimeFormatter#ISO_OFFSET_DATE_TIME} as fixed format.
 */
public final class ZonedDateTimeAdapter extends XmlAdapter<String, ZonedDateTime> {

    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId
            .systemDefault());

    @Override
    public final ZonedDateTime unmarshal(final String str) {
        if (str == null) {
            return null;
        }
        return parse(str);
    }

    @Override
    public final String marshal(final ZonedDateTime value) {
        if (value == null) {
            return null;
        }
        return format(value);
    }

    /**
     * Parses an ISO date time string.
     * 
     * @param str
     *            String to parse.
     * 
     * @return Zoned date time.
     */
    public static ZonedDateTime parse(final String str) {
        return ZonedDateTime.parse(str, FORMAT);
    }

    /**
     * Converts a zoned date time into a String.
     * 
     * @param dateTime
     *            Date time to convert.
     * 
     * @return Formatted String.
     */
    public static String format(final ZonedDateTime dateTime) {
        return FORMAT.format(dateTime);
    }

}
