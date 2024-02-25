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
package org.fuin.ddd4j.jsonbtestmodel;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbTypeAdapter;
import jakarta.validation.constraints.NotNull;
import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.common.ValueObject;

import java.io.Serial;
import java.io.Serializable;

/**
 * References a vendor.
 */
public final class VendorRef implements ValueObject, Serializable {

    @Serial
    private static final long serialVersionUID = 1000L;

    @JsonbProperty("id")
    @JsonbTypeAdapter(VendorIdJsonbAdapter.class)
    private VendorId vendorId;

    @JsonbProperty("key")
    @JsonbTypeAdapter(VendorKeyJsonbAdapter.class)
    private VendorKey vendorKey;

    @JsonbProperty("name")
    @JsonbTypeAdapter(VendorNameJsonbAdapter.class)
    private VendorName vendorName;

    /**
     * Default constructor only for de-serialization.
     */
    VendorRef() {
        super();
    }

    /**
     * Constructor with all data.
     *
     * @param vendorId   Technical vendor identifier.
     * @param vendorKey  Vendor business key.
     * @param vendorName Vendor name.
     */
    public VendorRef(@NotNull final VendorId vendorId, @NotNull final VendorKey vendorKey, @NotNull final VendorName vendorName) {
        super();

        Contract.requireArgNotNull("vendorId", vendorId);
        Contract.requireArgNotNull("vendorKey", vendorKey);
        Contract.requireArgNotNull("vendorName", vendorName);

        this.vendorId = vendorId;
        this.vendorKey = vendorKey;
        this.vendorName = vendorName;
    }

    /**
     * Returns the technical vendor identifier.
     *
     * @return Vendor ID.
     */
    @NotNull
    public VendorId getId() {
        return vendorId;
    }

    /**
     * Returns the vendor's business key.
     *
     * @return Vendor key.
     */
    @NotNull
    public VendorKey getKey() {
        return vendorKey;
    }

    /**
     * Returns the name of the vendor.
     *
     * @return Vendor name.
     */
    @NotNull
    public VendorName getName() {
        return vendorName;
    }

    @Override
    public final String toString() {
        return VendorId.TYPE + " " + vendorKey;
    }

}
