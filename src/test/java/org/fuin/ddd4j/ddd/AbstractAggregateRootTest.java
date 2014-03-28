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
		final AEntity a = new AEntity(aid);

		final BId bid = new BId(2);
		final BEntity b = new BEntity(a, bid);

		a.add(b);

		final Method method = AbstractAggregateRoot
				.findDeclaredAnnotatedMethod(a, ChildEntityLocator.class,
						BId.class);

		// TEST
		Entity<?> found = AbstractAggregateRoot.invoke(method, a, bid);

		// VERIFY
		assertThat(found).isSameAs(b);

	}

	@Test
	public void testCallAnnotatedEventHandlerMethod() {

		// PREPARE
		final AId aid = new AId(1);
		final AEntity a = new AEntity(aid);
		final AEvent event = new AEvent(aid);

		// TEST
		AbstractAggregateRoot.callAnnotatedEventHandlerMethod(a, event);

		// VERIFY
		assertThat(a.getLastEvent()).isSameAs(event);

	}

	@Test
	public void testCallAnnotatedEventHandlerMethodOnAggregateRootOrChild() {

		// PREPARE
		final AId aid = new AId(1);
		final AEntity a = new AEntity(aid);

		final BId bid = new BId(2);
		final BEntity b = new BEntity(a, bid);

		a.add(b);

		final BEvent event = new BEvent(aid, bid);

		// TEST
		AbstractAggregateRoot
				.callAnnotatedEventHandlerMethodOnAggregateRootOrChild(a, event);

		// VERIFY
		assertThat(a.getLastEvent()).isNull();
		assertThat(b.getLastEvent()).isSameAs(event);

	}

	@Test
	public void testApplyRoot() {

		// PREPARE
		final AId aid = new AId(1);
		final AEntity a = new AEntity(aid);
		final AEvent event = new AEvent(aid);

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
		final AEntity a = new AEntity(aid);

		final BId bid = new BId(2);
		final BEntity b = new BEntity(a, bid);

		a.add(b);

		final BEvent event = new BEvent(aid, bid);

		// TEST
		a.applyNewChildEvent(b, event);

		// VERIFY
		assertThat(a.getUncommittedChanges()).containsExactly(event);
		assertThat(a.getVersion()).isEqualTo(0);
		assertThat(a.getNextVersion()).isEqualTo(1);
		assertThat(b.getLastEvent()).isSameAs(event);

	}

	@Test
	public void testGetNextVersion() {

		// PREPARE
		final AId aid = new AId(1);
		final AEntity a = new AEntity(aid);
		assertThat(a.getVersion()).isEqualTo(0);
		assertThat(a.getNextVersion()).isEqualTo(0);
		final AEvent event = new AEvent(aid);

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
		final AEntity a = new AEntity(aid);
		assertThat(a.hasUncommitedChanges()).isFalse();
		final AEvent event = new AEvent(aid);
		a.apply(event);

		// TEST & VERIFY
		assertThat(a.hasUncommitedChanges()).isTrue();

	}

	@Test
	public void testMarkChangesAsCommitted() {

		// PREPARE
		final AId aid = new AId(1);
		final AEntity a = new AEntity(aid);
		final AEvent event = new AEvent(aid);
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
		final AEntity a = new AEntity(aid);
		final AEvent event = new AEvent(aid);
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
		final AEntity a = new AEntity(aid);
		assertThat(a.getVersion()).isEqualTo(0);

		// TEST
		a.doIt();

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
		final AEntity a = new AEntity(aid);
		assertThat(a.getVersion()).isEqualTo(0);

		final BId bid = new BId(2);
		final BEntity b = new BEntity(a, bid);

		a.add(b);

		// TEST
		b.doIt();

		// VERIFY
		assertThat(a.getVersion()).isEqualTo(0);
		assertThat(a.getUncommittedChanges()).hasSize(1);
		assertThat(a.getNextVersion()).isEqualTo(1);
		final DomainEvent<?> ev = a.getUncommittedChanges().get(0);
		assertThat(ev).isSameAs(b.getLastEvent());

	}

	@Test
	public void testSubChildEvent() {

		// PREPARE
		final AId aid = new AId(1);
		final AEntity a = new AEntity(aid);
		assertThat(a.getVersion()).isEqualTo(0);

		final BId bid = new BId(2);
		final BEntity b = new BEntity(a, bid);

		a.add(b);

		final CId cid = new CId(3);
		final CEntity c = new CEntity(a, bid, cid);

		b.add(c);

		// TEST
		c.doIt();

		// VERIFY
		assertThat(a.getVersion()).isEqualTo(0);
		assertThat(a.getUncommittedChanges()).hasSize(1);
		assertThat(a.getNextVersion()).isEqualTo(1);
		final DomainEvent<?> ev = a.getUncommittedChanges().get(0);
		assertThat(ev).isSameAs(c.getLastEvent());

	}

	@Test
	public void testMultipleEvent() {

		// PREPARE
		final AId aid = new AId(1);
		final AEntity a = new AEntity(aid);
		assertThat(a.getVersion()).isEqualTo(0);

		final BId bid = new BId(2);
		final BEntity b = new BEntity(a, bid);

		a.add(b);

		final CId cid = new CId(3);
		final CEntity c = new CEntity(a, bid, cid);

		b.add(c);

		// TEST
		a.doIt();
		b.doIt();
		c.doIt();

		// VERIFY
		assertThat(a.getVersion()).isEqualTo(0);
		assertThat(a.getUncommittedChanges()).hasSize(3);
		assertThat(a.getNextVersion()).isEqualTo(3);
		final DomainEvent<?> evA = a.getUncommittedChanges().get(0);
		assertThat(evA).isSameAs(a.getLastEvent());
		final DomainEvent<?> evB = a.getUncommittedChanges().get(1);
		assertThat(evB).isSameAs(b.getLastEvent());
		final DomainEvent<?> evC = a.getUncommittedChanges().get(2);
		assertThat(evC).isSameAs(c.getLastEvent());

	}

	// ----------------- TEST CLASSES -----------------

	private static class AEntity extends AbstractAggregateRoot<AId> {

		private AId id;

		private List<BEntity> childs;

		private AEvent lastEvent;

		public AEntity(AId id) {
			this.id = id;
			this.childs = new ArrayList<BEntity>();
		}

		@Override
		public AId getId() {
			return id;
		}

		@Override
		public EntityType getType() {
			return new EntityType() {
				@Override
				public String asString() {
					return "A";
				}
			};
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

		public void add(BEntity b) {
			childs.add(b);
		}

		public void doIt() {
			apply(new AEvent(id));
		}

		@EventHandler
		public void handle(AEvent event) {
			lastEvent = event;
		}

		public AEvent getLastEvent() {
			return lastEvent;
		}

	}

	private static class BEntity extends AbstractEntity<BId> {

		private AId parentId;

		private BId id;

		private List<CEntity> childs;

		private BEvent lastEvent;

		public BEntity(AEntity parent, BId id) {
			super(parent);
			this.parentId = parent.getId();
			this.id = id;
			this.childs = new ArrayList<CEntity>();
		}

		@Override
		public BId getId() {
			return id;
		}

		@Override
		public EntityType getType() {
			return new EntityType() {
				@Override
				public String asString() {
					return "B";
				}
			};
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

		public void add(CEntity c) {
			childs.add(c);
		}

		public void doIt() {
			apply(new BEvent(parentId, id));
		}

		@EventHandler
		public void handle(BEvent event) {
			lastEvent = event;
		}

		public BEvent getLastEvent() {
			return lastEvent;
		}

	}

	private static class CEntity extends AbstractEntity<CId> {

		private AId parentParentId;

		private BId parentId;

		private CId id;

		private CEvent lastEvent;

		public CEntity(AEntity aggregateRoot, BId parentId, CId id) {
			super(aggregateRoot);
			this.parentParentId = aggregateRoot.getId();
			this.parentId = parentId;
			this.id = id;
		}

		@Override
		public CId getId() {
			return id;
		}

		@Override
		public EntityType getType() {
			return new EntityType() {
				@Override
				public String asString() {
					return "C";
				}
			};
		}

		public void doIt() {
			apply(new CEvent(parentParentId, parentId, id));
		}

		@EventHandler
		public void handle(CEvent event) {
			lastEvent = event;
		}

		public CEvent getLastEvent() {
			return lastEvent;
		}

	}

	private static class AEvent extends AbstractDomainEvent<AId> {

		private static final long serialVersionUID = 1L;

		private static final EventType EVENT_TYPE = new EventType("AEvent");

		public AEvent(AId aid) {
			super(new EntityIdPath(aid));
		}

		@Override
		public EventType getEventType() {
			return EVENT_TYPE;
		}

	}

	private static class BEvent extends AbstractDomainEvent<BId> {

		private static final long serialVersionUID = 1L;

		private static final EventType EVENT_TYPE = new EventType("BEvent");

		public BEvent(AId aid, BId bid) {
			super(new EntityIdPath(aid, bid));
		}

		@Override
		public EventType getEventType() {
			return EVENT_TYPE;
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
