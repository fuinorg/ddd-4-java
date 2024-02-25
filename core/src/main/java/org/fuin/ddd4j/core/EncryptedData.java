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
import org.fuin.objects4j.common.ValueObject;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;

/**
 * Container for encrypted data. In addition to the data itself, the container has information about the key used to encrypt the data and
 * the format of the data. The receiving system must have a notion of what the key identifier, versions and data type means. Equals and hash
 * code is based on all data (value object)
 */
@Immutable
public interface EncryptedData extends ValueObject, Serializable {

    /**
     * Returns the unique identifier of the private key used.
     *
     * @return Private key name.
     */
    @NotEmpty
    String getKeyId();

    /**
     * Returns the version of the private key used.
     *
     * @return Version.
     */
    @NotEmpty
    String getKeyVersion();

    /**
     * Returns the unique type of the data like "UserPersonalData".
     *
     * @return Unique type name.
     */
    @NotEmpty
    String getDataType();

    /**
     * Returns the content type like "application/json; encoding=UTF-8; version=1".
     *
     * @return Mime type.
     */
    @NotEmpty
    String getContentType();

    /**
     * Returns the encrypted data.
     *
     * @return Data.
     */
    @NotEmpty
    byte[] getEncryptedData();

}
