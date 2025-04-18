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

import java.io.Serial;

/**
 * It was tried to add a vendor key that already exists.
 */
public final class DuplicateVendorKeyException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    private final VendorKey key;

    /**
     * Constructor with key.
     *
     * @param key
     *            Key.
     */
    public DuplicateVendorKeyException(final VendorKey key) {
        super("The vendor key already exists: " + key);
        this.key = key;
    }

    /**
     * The key that caused the problem.
     *
     * @return Duplicate key.
     */
    public final VendorKey getKey() {
        return key;
    }

}
