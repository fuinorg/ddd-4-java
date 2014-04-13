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

import java.util.ArrayList;
import java.util.List;

import org.fuin.ddd4j.ddd.AbstractDomainEvent;
import org.fuin.ddd4j.ddd.AbstractEntity;
import org.fuin.ddd4j.ddd.ChildEntityLocator;
import org.fuin.ddd4j.ddd.EntityType;
import org.fuin.ddd4j.ddd.EventHandler;

// CHECKSTYLE:OFF
public class BEntity extends AbstractEntity<AId, ARoot, BId> {

	private ARoot root;

	private BId id;

	private List<CEntity> childs;

	private AbstractDomainEvent<?> lastEvent;

	public BEntity(ARoot root, BId id) {
		super(root);
		this.root = root;
		this.id = id;
		this.childs = new ArrayList<CEntity>();
	}

	@Override
	public BId getId() {
		return id;
	}

	@Override
	public EntityType getType() {
		return BId.TYPE;
	}

	@ChildEntityLocator
	private CEntity find(CId bid) {
		for (CEntity child : childs) {
			if (child.getId().equals(bid)) {
				return child;
			}
		}
		return null;
	}

	public void add(CId cid) {
		apply(new CAddedEvent(root.getId(), id, cid));
	}

	public void doIt(CId cid) {
		final CEntity found = find(cid);
		found.doIt();
	}
	
	@EventHandler
	public void handle(CAddedEvent event) {
		childs.add(new CEntity(root, id, event.getCId()));
		lastEvent = event;
	}

	public AbstractDomainEvent<?> getLastEvent() {
		return lastEvent;
	}

	public CEntity getFirstChild() {
		return childs.get(0);
	}
	
}
// CHECKSTYLE:ON
