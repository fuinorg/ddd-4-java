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
package org.fuin.ddd4j.jsonbtest;

import jakarta.validation.constraints.NotNull;
import org.fuin.ddd4j.core.BusinessKey;
import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.core.AbstractStringValueObject;
import org.fuin.objects4j.ui.Label;
import org.fuin.objects4j.ui.ShortLabel;
import org.fuin.objects4j.ui.Tooltip;

import javax.annotation.concurrent.Immutable;
import java.io.Serial;

/**
 * A vendor's human-readable business key.
 */
@Label(value = "Vendor number")
@ShortLabel(value = "VN")
@Tooltip("A human readable unique identifier for a vendor. " + "Used for example in mails or contracts as a reference.")
@Immutable
public final class VendorKey extends AbstractStringValueObject implements BusinessKey {

    @Serial
    private static final long serialVersionUID = 1000L;

    @NotNull
    @VendorKeyStr
    private final String key;

    /**
     * Constructor with string.
     *
     * @param key
     *            Key.
     */
    public VendorKey(@NotNull @VendorKeyStr final String key) {
        super();
        Contract.requireArgNotEmpty("key", key);
        VendorKeyStrValidator.requireArgValid("key", key);
        this.key = key;
    }

    @Override
    public final String asBaseType() {
        return key;
    }

    @Override
    public final String toString() {
        return key;
    }

    /**
     * Returns the information if a given string is a valid key.
     *
     * @param value
     *            Value to check. A <code>null</code> value returns <code>true</code>.
     *
     * @return TRUE if it's a valid key, else FALSE.
     */
    public static boolean isValid(final String value) {
        return VendorKeyStrValidator.isValid(value);
    }

    /**
     * Parses a key.
     *
     * @param value
     *            Value to convert. A <code>null</code> value returns <code>null</code>.
     *
     * @return Converted value.
     */
    public static VendorKey valueOf(final String value) {
        if (value == null) {
            return null;
        }
        return new VendorKey(value);
    }

}
