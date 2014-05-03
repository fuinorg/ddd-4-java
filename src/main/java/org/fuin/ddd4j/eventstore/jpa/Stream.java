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
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Base class for all concrete streams.
 */
@Table(name = "STREAMS")
@Entity
public class Stream {

	@Id
	@Column(name = "NAME", nullable = false, updatable = false, length = 255)
	private String name;
	
	@Column(name = "DELETED", nullable = false)
	private boolean deleted = false;

	@Column(name = "VERSION", nullable = false)
	private int version = 0;

	/**
	 * Protected default constructor for JPA.
	 */
	protected Stream() {
		super();
	}
	
	/**
	 * Constructor with name.
	 * 
	 * @param name Unique stream name.
	 */
	public Stream(@NotNull final String name) {
		super();
		this.name = name;
	}
	
	/**
	 * Returns the information if the stream was deleted.
	 * 
	 * @return TRUE if the stream was deleted.
	 */
	public final boolean isDeleted() {
		return deleted;
	}

	/**
	 * Marks the stream as deleted.
	 */
	public final void delete() {
		this.deleted = true;
	}

	/**
	 * Returns the current version of the stream.
	 * 
	 * @return Version.
	 */
	public final int getVersion() {
		return version;
	}

	/**
	 * Increments the version of the stream by one.
	 */
	protected final void incVersion() {
		this.version++;
	}

	/**
	 * Creates a container that stores the given event entry.
	 * 
	 * @param eventEntry
	 *            Event entry to convert into a JPA variaant.
	 * 
	 * @return JPA entity.
	 */
	public final StreamEvent createEvent(@NotNull final EventEntry eventEntry) {
		incVersion();
		return new StreamEvent(name, version, eventEntry);
	}

}
