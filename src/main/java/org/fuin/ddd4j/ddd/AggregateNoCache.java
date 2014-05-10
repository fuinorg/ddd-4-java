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
package org.fuin.ddd4j.ddd;

/**
 * Never caches anything.
 * 
 * @param <AGGREGATE>
 *            Type of the aggregate.
 */
public final class AggregateNoCache<AGGREGATE> implements
	AggregateCache<AGGREGATE> {

    @Override
    public final AGGREGATE get(final AggregateRootId aggregateId) {
	// Always return null
	return null;
    }

    @Override
    public void put(final AggregateRootId aggregateId, final AGGREGATE aggregate) {
	// Do nothing
    }

    @Override
    public final void remove(final AggregateRootId aggregateId) {
	// Do nothing
    }

}
