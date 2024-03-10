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
package org.fuin.ddd4j.core;

import jakarta.validation.constraints.NotEmpty;
import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.common.ExceptionShortIdentifable;

import java.io.Serial;

import static org.fuin.ddd4j.core.Ddd4JUtils.SHORT_ID_PREFIX;

/**
 * Signals that the encryption key identifier is unknown.
 */
public final class EncryptionKeyIdUnknownException extends Exception implements ExceptionShortIdentifable {

    /**
     * Unique name of the element to use for XML and JSON marshalling/unmarshalling.
     */
    public static final String ELEMENT_NAME = "encryption-key-id-unknown-exception";

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Unique short identifier of this exception.
     */
    public static final String SHORT_ID = SHORT_ID_PREFIX + "-ENCRYPTION_KEY_ID_UNKNOWN";

    private final String keyId;

    /**
     * Constructor with all data.
     *
     * @param keyId The key identifier that caused the problem.
     */
    public EncryptionKeyIdUnknownException(@NotEmpty final String keyId) {
        super("Unknown keyId: " + keyId);
        Contract.requireArgNotEmpty("keyId", keyId);
        this.keyId = keyId;
    }

    @Override
    public final String getShortId() {
        return SHORT_ID;
    }

    /**
     * Returns the IV version that caused the problem.
     *
     * @return IV version.
     */
    @NotEmpty
    public final String getKeyId() {
        return keyId;
    }

}
