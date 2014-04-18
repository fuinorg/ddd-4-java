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
package org.fuin.ddd4j.eventstore.jpa;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.fuin.ddd4j.test.VendorId;
import org.fuin.ddd4j.test.VendorIdConverter;

/**
 * Event related to a vendor.
 */
@Table(name = "VENDOR_EVENTS", indexes = { @Index(columnList = "VENDOR_ID,EVENT_NO") })
@Entity
public class VendorEvent extends StreamEvent {

	@Id
	@NotNull
	@Column(name = "ID", length = 100, nullable = false, columnDefinition = "VARCHAR(100)")
	private String id;

	@NotNull
	@Convert(converter = VendorIdConverter.class)
	@Column(name = "VENDOR_ID", length = 100, nullable = false)
	private VendorId aggregateId;

	/**
	 * Protected default constructor only required for JPA.
	 */
	protected VendorEvent() {
		super();
	}

	/**
	 * Constructor with all data.
	 * 
	 * @param aggregateId Unique identifer of the aggregate.
	 * @param eventNumber Number of the event.
	 * @param eventEntry Event data.
	 */
	public VendorEvent(final VendorId aggregateId, final int eventNumber,
			final EventEntry eventEntry) {
		super(eventNumber, eventEntry);
		this.id = aggregateId.asString() + "#" + eventNumber;
		this.aggregateId = aggregateId;
	}

	/**
	 * Returns the unique identifer of the aggregate.
	 * 
	 * @return Aggregate ID.
	 */
	public final VendorId getAggregateId() {
		return aggregateId;
	}

}
