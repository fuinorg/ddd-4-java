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
package org.fuin.ddd4j.ddd;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.vo.AbstractIntegerValueObject;

/**
 * Version of an aggregate root.
 */
public final class AggregateVersion extends AbstractIntegerValueObject {

    private static final long serialVersionUID = 1000L;

    private Integer value;

    /**
     * Constructor with value.
     * 
     * @param version
     *            Version.
     */
    public AggregateVersion(@NotNull @Min(0) final Integer version) {
        super();
        Contract.requireArgNotNull("version", version);
        Contract.requireArgMin("version", version, 0);
        this.value = version;
    }

    @Override
    public final Integer asBaseType() {
        return value;
    }

    /**
     * Returns the information if a given string is a valid version.
     * 
     * @param value
     *            Value to check. A <code>null</code> value returns
     *            <code>true</code>.
     * 
     * @return TRUE if it's a valid version, else FALSE.
     */
    public static boolean isValid(final Integer value) {
        if (value == null) {
            return true;
        }
        return value >= 0;
    }

    /**
     * Parses a version identifier.
     * 
     * @param value
     *            Value to convert. A <code>null</code> value returns
     *            <code>null</code>.
     * 
     * @return Converted value.
     */
    public static AggregateVersion valueOf(final Integer value) {
        if (value == null) {
            return null;
        }
        return new AggregateVersion(value);
    }

}
