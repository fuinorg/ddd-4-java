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

import static org.fest.assertions.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.fuin.ddd4j.test.AId;
import org.fuin.ddd4j.test.BId;
import org.fuin.ddd4j.test.CId;
import org.junit.Test;

// CHECKSTYLE:OFF
public class AbstractAggregateRootTest {

	@Test
	public void testFindDeclaredAnnotatedMethodAndInvoke() {

		// PREPARE
		final AId aid = new AId(1);
		final ARoot a = new ARoot(aid);

		final BId bid = new BId(2);

		a.addB(bid);

		final Method method = AbstractAggregateRoot
				.findDeclaredAnnotatedMethod(a, ChildEntityLocator.class,
						BId.class);

		// TEST
		Entity<?> found = AbstractAggregateRoot.invoke(method, a, bid);

		// VERIFY
		assertThat(found.getId()).isSameAs(bid);

	}

	@Test
	public void testCallAnnotatedEventHandlerMethod() {

		// PREPARE
		final AId aid = new AId(1);
		final ARoot a = new ARoot();
		final ACreatedEvent event = new ACreatedEvent(aid);

		// TEST
		AbstractAggregateRoot.callAnnotatedEventHandlerMethod(a, event);

		// VERIFY
		assertThat(a.getLastEvent()).isSameAs(event);

	}

	@Test
	public void testCallAnnotatedEventHandlerMethodOnAggregateRootOrChild() {

		// PREPARE
		final AId aid = new AId(1);
		final ARoot a = new ARoot(aid);
		final BId bid = new BId(2);
		a.addB(bid);
		a.markChangesAsCommitted();
		final CId cid = new CId(3);

		final CAddedEvent event = new CAddedEvent(aid, bid, cid);

		// TEST
		AbstractAggregateRoot
				.callAnnotatedEventHandlerMethodOnAggregateRootOrChild(a, event);

		// VERIFY
		assertThat(a.getFirstChild().getLastEvent()).isSameAs(event);

	}

	@Test
	public void testApplyRoot() {

		// PREPARE
		final AId aid = new AId(1);
		final ARoot a = new ARoot();
		final ACreatedEvent event = new ACreatedEvent(aid);

		// TEST
		a.apply(event);

		// VERIFY
		assertThat(a.getUncommittedChanges()).containsExactly(event);
		assertThat(a.getVersion()).isEqualTo(0);
		assertThat(a.getNextVersion()).isEqualTo(1);

	}

	@Test
	public void testApplyNewChildEvent() {

		// PREPARE
		final AId aid = new AId(1);
		final ARoot a = new ARoot(aid);
		final BId bid = new BId(2);
		a.addB(bid);
		final CId cid = new CId(3);
		a.markChangesAsCommitted();

		final CAddedEvent event = new CAddedEvent(aid, bid, cid);

		// TEST
		a.applyNewChildEvent(a.getFirstChild(), event);

		// VERIFY
		assertThat(a.getUncommittedChanges()).containsExactly(event);
		assertThat(a.getVersion()).isEqualTo(2);
		assertThat(a.getNextVersion()).isEqualTo(3);
		assertThat(a.getFirstChild().getLastEvent()).isSameAs(event);

	}

	@Test
	public void testGetNextVersion() {

		// PREPARE
		final AId aid = new AId(1);
		final ARoot a = new ARoot();
		assertThat(a.getVersion()).isEqualTo(0);
		assertThat(a.getNextVersion()).isEqualTo(0);
		final ACreatedEvent event = new ACreatedEvent(aid);

		// TEST
		a.apply(event);

		// VERIFY
		assertThat(a.getVersion()).isEqualTo(0);
		assertThat(a.getNextVersion()).isEqualTo(1);

	}

	@Test
	public void testHasUncommitedChanges() {

		// PREPARE
		final AId aid = new AId(1);
		final ARoot a = new ARoot();
		assertThat(a.hasUncommitedChanges()).isFalse();
		final ACreatedEvent event = new ACreatedEvent(aid);
		a.apply(event);

		// TEST & VERIFY
		assertThat(a.hasUncommitedChanges()).isTrue();

	}

	@Test
	public void testMarkChangesAsCommitted() {

		// PREPARE
		final AId aid = new AId(1);
		final ARoot a = new ARoot();
		final ACreatedEvent event = new ACreatedEvent(aid);
		a.apply(event);
		assertThat(a.hasUncommitedChanges()).isTrue();

		// TEST
		a.markChangesAsCommitted();

		// VERIFY
		assertThat(a.hasUncommitedChanges()).isFalse();

	}

	@Test
	public void testLoadFromHistory() {

		// PREPARE
		final AId aid = new AId(1);
		final ARoot a = new ARoot();
		final ACreatedEvent event = new ACreatedEvent(aid);
		assertThat(a.getVersion()).isEqualTo(0);

		// TEST
		a.loadFromHistory(event);

		// VERIFY
		assertThat(a.getVersion()).isEqualTo(1);
		assertThat(a.getUncommittedChanges()).isEmpty();

	}

	@Test
	public void testAggregateRootEvent() {

		// PREPARE
		final AId aid = new AId(1);

		// TEST
		final ARoot a = new ARoot(aid);

		// VERIFY
		assertThat(a.getVersion()).isEqualTo(0);
		assertThat(a.getNextVersion()).isEqualTo(1);
		assertThat(a.getUncommittedChanges()).hasSize(1);
		final DomainEvent<?> ev = a.getUncommittedChanges().get(0);
		assertThat(ev).isSameAs(a.getLastEvent());

	}

	@Test
	public void testChildEvent() {

		// PREPARE
		final AId aid = new AId(1);
		final ARoot a = new ARoot(aid);
		assertThat(a.getVersion()).isEqualTo(0);
		final BId bid = new BId(2);
		a.addB(bid);
		a.markChangesAsCommitted();
		final CId cid = new CId(3);

		// TEST
		a.addC(bid, cid);

		// VERIFY
		assertThat(a.getVersion()).isEqualTo(2);
		assertThat(a.getUncommittedChanges()).hasSize(1);
		assertThat(a.getNextVersion()).isEqualTo(3);
		final DomainEvent<?> ev = a.getUncommittedChanges().get(0);
		assertThat(ev).isSameAs(a.getFirstChild().getLastEvent());

	}

	@Test
	public void testSubChildEvent() {

		// PREPARE
		final AId aid = new AId(1);
		final ARoot a = new ARoot(aid);		
		final BId bid = new BId(2);
		a.addB(bid);
		final CId cid = new CId(3);
		a.addC(bid, cid);
		a.markChangesAsCommitted();

		final CEntity c = a.getFirstChild().getFirstChild();
		
		// TEST
		c.doIt();

		// VERIFY
		assertThat(a.getVersion()).isEqualTo(3);
		assertThat(a.getUncommittedChanges()).hasSize(1);
		assertThat(a.getNextVersion()).isEqualTo(4);
		final DomainEvent<?> ev = a.getUncommittedChanges().get(0);
		assertThat(ev).isSameAs(c.getLastEvent());

	}

	@Test
	public void testMultipleEvent() {

		// PREPARE
		final AId aid = new AId(1);
		final ARoot a = new ARoot(aid);
		final BId bid = new BId(2);
		a.addB(bid);
		a.markChangesAsCommitted();

		// TEST
		final CId cid = new CId(3);
		a.addC(bid, cid);
		a.doItC(bid, cid);

		// VERIFY
		assertThat(a.getVersion()).isEqualTo(2);
		assertThat(a.getUncommittedChanges()).hasSize(2);
		assertThat(a.getNextVersion()).isEqualTo(4);
		final DomainEvent<?> evB = a.getUncommittedChanges().get(0);
		assertThat(evB).isSameAs(a.getFirstChild().getLastEvent());
		final DomainEvent<?> evC = a.getUncommittedChanges().get(1);
		assertThat(evC).isSameAs(a.getFirstChild().getFirstChild().getLastEvent());

	}

	// ----------------- TEST CLASSES -----------------

	private static class ARoot extends AbstractAggregateRoot<AId> {

		private AId id;

		private List<BEntity> childs;

		private AbstractDomainEvent<?> lastEvent;

		public ARoot() {
			super();
		}
		
		public ARoot(AId id) {
			super();
			apply(new ACreatedEvent(id));
		}

		@Override
		public AId getId() {
			return id;
		}

		@Override
		public EntityType getType() {
			return AId.TYPE;
		}

		@ChildEntityLocator
		private BEntity find(BId bid) {
			for (BEntity child : childs) {
				if (child.getId().equals(bid)) {
					return child;
				}
			}
			return null;
		}

		public void addB(BId bid) {
			apply(new BAddedEvent(id, bid));
		}

		public void addC(BId bid, CId cid) {
			final BEntity found = find(bid);
			found.add(cid);
		}
		
		public void doItC(BId bid, CId cid) {
			final BEntity found = find(bid);
			found.doIt(cid);
		}
		
		@EventHandler
		public void handle(ACreatedEvent event) {
			this.id = event.getId();
			this.childs = new ArrayList<BEntity>();
			lastEvent = event;
		}
		
		@EventHandler
		public void handle(BAddedEvent event) {
			childs.add(new BEntity(this, event.getBId()));
			lastEvent = event;
		}

		public AbstractDomainEvent<?> getLastEvent() {
			return lastEvent;
		}

		private BEntity getFirstChild() {
			return childs.get(0);
		}
		
	}

	private static class BEntity extends AbstractEntity<BId> {

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

		private CEntity getFirstChild() {
			return childs.get(0);
		}
		
	}

	private static class CEntity extends AbstractEntity<CId> {

		private ARoot root;

		private BId parentId;

		private CId id;

		private CEvent lastEvent;

		public CEntity(ARoot root, BId parentId, CId id) {
			super(root);
			this.root = root;
			this.parentId = parentId;
			this.id = id;
		}

		@Override
		public CId getId() {
			return id;
		}

		@Override
		public EntityType getType() {
			return CId.TYPE;
		}

		public void doIt() {
			apply(new CEvent(root.getId(), parentId, id));
		}

		@EventHandler
		public void handle(CEvent event) {
			lastEvent = event;
		}

		public CEvent getLastEvent() {
			return lastEvent;
		}

	}

	private static class ACreatedEvent extends AbstractDomainEvent<AId> {

		private static final long serialVersionUID = 1L;

		private static final EventType EVENT_TYPE = new EventType("ACreatedEvent");
		
		private AId id;
		
		public ACreatedEvent(AId id) {
			super(new EntityIdPath(id));
		}
		
		public AId getId() {
			return id;
		}
		
		@Override
		public EventType getEventType() {
			return EVENT_TYPE;
		}
		
	}
	
	private static class BAddedEvent extends AbstractDomainEvent<AId> {

		private static final long serialVersionUID = 1L;

		private static final EventType EVENT_TYPE = new EventType("BAddedEvent");

		private BId bid;
		
		public BAddedEvent(AId aid, BId bid) {
			super(new EntityIdPath(aid));
			this.bid = bid;
		}

		@Override
		public EventType getEventType() {
			return EVENT_TYPE;
		}
		
		public BId getBId() {
			return bid;
		}

	}

	private static class CAddedEvent extends AbstractDomainEvent<BId> {

		private static final long serialVersionUID = 1L;

		private static final EventType EVENT_TYPE = new EventType("CAddedEvent");

		private CId cid;
		
		public CAddedEvent(AId aid, BId bid, CId cid) {
			super(new EntityIdPath(aid, bid));
			this.cid = cid;
		}

		@Override
		public EventType getEventType() {
			return EVENT_TYPE;
		}

		public CId getCId() {
			return cid;
		}
		
	}

	private static class CEvent extends AbstractDomainEvent<CId> {

		private static final long serialVersionUID = 1L;

		private static final EventType EVENT_TYPE = new EventType("CEvent");

		public CEvent(AId aid, BId bid, CId cid) {
			super(new EntityIdPath(aid, bid, cid));
		}

		@Override
		public EventType getEventType() {
			return EVENT_TYPE;
		}

	}

}
//CHECKSTYLE:ON
