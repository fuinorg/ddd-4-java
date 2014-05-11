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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.fuin.ddd4j.eventstore.jpa.StreamEvent;

/**
 * Vendor event.
 */
@Table(name = "PRJ_VENDOR_TABLE_EVENTS", indexes = { @Index(name = "IDX_EVENTS_ID", unique = true, columnList = "EVENTS_ID") })
@Entity
public class ProjectionVendorTableEvent extends StreamEvent {

    // We don't really need this column, but JPA does not allow us to use the
    // EVENTS_ID field from StreamEvent as @Id
    @Id
    @Column(name = "ID")
    private Long id;

}
