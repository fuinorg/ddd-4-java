/**
 * Copyright (C) 2015 Michael Schnell. All rights reserved.
 * http://www.fuin.org/
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see http://www.gnu.org/licenses/.
 */
package org.fuin.ddd4j.jaxbtest;

import org.fuin.objects4j.jaxb.ValueObjectStringXmlAdapter;

import javax.annotation.concurrent.ThreadSafe;

/**
 * Converts a vendor identifier into a string and back (JAXB).
 */
@ThreadSafe
public final class VendorIdConverter extends ValueObjectStringXmlAdapter<VendorId> {

    public VendorIdConverter() {
        super(VendorId::valueOf);
    }

}
