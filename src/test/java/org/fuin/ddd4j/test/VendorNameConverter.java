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
package org.fuin.ddd4j.test;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import javax.annotation.concurrent.ThreadSafe;
import org.fuin.objects4j.vo.AbstractValueObjectConverter;

/**
 * Converts a vendor name into a string and back (JAXB and JPA).
 */
@ThreadSafe
@Converter(autoApply = true)
public final class VendorNameConverter extends AbstractValueObjectConverter<String, VendorName> implements
        AttributeConverter<VendorName, String> {

    @Override
    public Class<String> getBaseTypeClass() {
        return String.class;
    }

    @Override
    public final Class<VendorName> getValueObjectClass() {
        return VendorName.class;
    }

    @Override
    public final boolean isValid(final String value) {
        return VendorName.isValid(value);
    }

    @Override
    public final VendorName toVO(final String value) {
        return VendorName.valueOf(value);
    }

    @Override
    public final String fromVO(final VendorName value) {
        if (value == null) {
            return null;
        }
        return value.toString();
    }

}
