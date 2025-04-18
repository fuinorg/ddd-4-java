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

import org.fuin.objects4j.common.Contract;

import java.io.Serial;

/**
 * The person identifier is unknown for the vendor.
 */
public final class PersonNotFoundException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    private final VendorRef vendorRef;

    private final PersonId personId;

    /**
     * Constructor with mandatory data.
     *
     * @param vendorRef
     *            Vendor reference.
     * @param personId
     *            Person identifier.
     */
    public PersonNotFoundException(final VendorRef vendorRef, final PersonId personId) {
        super("No person with # " + personId + " found for: " + vendorRef);
        Contract.requireArgNotNull("vendorRef", vendorRef);
        Contract.requireArgNotNull("personId", personId);
        this.vendorRef = vendorRef;
        this.personId = personId;
    }

    /**
     * Returns the vendor reference.
     *
     * @return Reference.
     */
    public final VendorRef getVendorRef() {
        return vendorRef;
    }

    /**
     * Returns the ID that caused the problem.
     *
     * @return Person ID that was not found.
     */
    public final PersonId getPersonId() {
        return personId;
    }

}
