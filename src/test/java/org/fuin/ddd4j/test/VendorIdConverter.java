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
package org.fuin.ddd4j.test;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.fuin.objects4j.common.ThreadSafe;
import org.fuin.objects4j.vo.AbstractValueObjectConverter;

/**
 * Converts a vendor identifier into a string and back (JAXB and JPA).
 */
@ThreadSafe
@ApplicationScoped
@Converter(autoApply = true)
public final class VendorIdConverter extends
	AbstractValueObjectConverter<String, VendorId> implements
	AttributeConverter<VendorId, String> {

    @Override
    public Class<VendorId> getValueObjectClass() {
	return VendorId.class;
    }

    @Override
    public final VendorId toVO(final String value) {
	return VendorId.valueOf(value);
    }

    @Override
    public Class<String> getBaseTypeClass() {
	return String.class;
    }

    @Override
    public final boolean isValid(final String value) {
	return VendorId.isValid(value);
    }

    @Override
    public final String fromVO(final VendorId value) {
	if (value == null) {
	    return null;
	}
	return value.toString();
    }

}
