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
package org.fuin.ddd4j.eventstore.intf;

import java.io.Serializable;
import java.util.List;

import org.fuin.objects4j.common.NeverNull;
import org.fuin.objects4j.vo.KeyValue;

/**
 * Name of a stream that is unique within the event store.
 */
public interface StreamId extends Serializable {

	/**
	 * Returns the name of the stream.
	 * 
	 * @return Unique name.
	 */
	@NeverNull
	public String getName();

	/**
	 * Returns the parameters used in addition to the pure stream name to
	 * identify the stream.
	 * 
	 * @return Ordered unmodifiable list of parameters - May be empty if no
	 *         parameters exist.
	 */
	@NeverNull
	public List<KeyValue> getParameters();

	/**
	 * Constructs a unique string from name and parameters.
	 * 
	 * @return String representation of the identifier.
	 */
	@NeverNull
	public String asString();

}
