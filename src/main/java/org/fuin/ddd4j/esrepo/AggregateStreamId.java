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
package org.fuin.ddd4j.esrepo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.fuin.ddd4j.ddd.AggregateRootId;
import org.fuin.ddd4j.ddd.EntityType;
import org.fuin.ddd4j.eventstore.intf.StreamId;
import org.fuin.objects4j.common.Immutable;
import org.fuin.objects4j.vo.KeyValue;

/**
 * Unique name of an aggregate stream.
 * 
 * @param <ID> Type of the aggregate root identifier.
 */
@Immutable
public final class AggregateStreamId<ID extends AggregateRootId> implements
		StreamId {

	private static final long serialVersionUID = 1000L;

	private EntityType type;

	private String paramName;

	private ID paramValue;

	private transient List<KeyValue> params;

	/**
	 * Constructor with type and id.
	 * 
	 * @param type
	 *            Aggregate type.
	 * @param paramName
	 *            Parameter name.
	 * @param paramValue
	 *            Aggregate id.
	 */
	public AggregateStreamId(final EntityType type, final String paramName,
			final ID paramValue) {
		super();
		this.type = type;
		this.paramName = paramName;
		this.paramValue = paramValue;
	}

	@Override
	public final String getName() {
		return type.asString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public final ID getSingleParamValue() {
		return paramValue;
	}

	@Override
	public final List<KeyValue> getParameters() {
		if (params == null) {
			final List<KeyValue> list = new ArrayList<KeyValue>();
			list.add(new KeyValue(paramName, paramValue));
			params = Collections.unmodifiableList(list);
		}
		return params;
	}

	@Override
	public final String asString() {
		return type + "-" + paramValue.asString();
	}

	@Override
	public final String toString() {
		return asString();
	}

}
