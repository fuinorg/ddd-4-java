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

import org.fuin.ddd4j.ddd.AbstractDomainEvent;
import org.fuin.ddd4j.ddd.EntityIdPath;
import org.fuin.ddd4j.ddd.EventType;

// CHECKSTYLE:OFF
public class CEvent extends AbstractDomainEvent<CId> {

    private static final long serialVersionUID = 1L;

    private static final EventType EVENT_TYPE = new EventType("CEvent");

    public CEvent(final AId aid, final BId bid, final CId cid) {
	super(new EntityIdPath(aid, bid, cid));
    }

    @Override
    public EventType getEventType() {
	return EVENT_TYPE;
    }

}
// CHECKSTYLE:ON
