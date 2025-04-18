/**
 * Copyright (C) 2015 Michael Schnell. All rights reserved.
 * http://www.fuin.org/
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see http://www.gnu.org/licenses/.
 */
package org.fuin.ddd4j.core;

import org.fuin.ddd4j.coretest.ACreatedEvent;
import org.fuin.ddd4j.coretest.AId;
import org.fuin.ddd4j.coretest.ARoot;
import org.fuin.ddd4j.coretest.BId;
import org.fuin.ddd4j.coretest.CAddedEvent;
import org.fuin.ddd4j.coretest.CEntity;
import org.fuin.ddd4j.coretest.CId;
import org.fuin.ddd4j.coretest.DEvent;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AbstractAggregateRootTest {

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
    public void testCallAnnotatedEventHandlerMethodOnAggregateRootOrChild() throws DuplicateEntityException {

        // PREPARE
        final AId aid = new AId(1);
        final ARoot a = new ARoot(aid);
        final BId bid = new BId(2);
        a.addB(bid);
        a.markChangesAsCommitted();
        final CId cid = new CId(3);

        final CAddedEvent event = new CAddedEvent(aid, bid, cid);

        // TEST
        AbstractAggregateRoot.callAnnotatedEventHandlerMethodOnAggregateRootOrChild(a, event);

        // VERIFY
        assertThat(a.getFirstChild().getLastEvent()).isSameAs(event);

    }

    @Test
    public void testApplyBaseMethod() {

        // PREPARE
        final AId aid = new AId(1);
        final ARoot a = new ARoot();
        final DEvent event = new DEvent(aid);

        // TEST
        a.apply(event);

        // VERIFY
        assertThat(a.getUncommittedChanges()).containsExactly(event);
        assertThat(a.getVersion()).isEqualTo(-1);
        assertThat(a.getNextVersion()).isEqualTo(0);

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
        assertThat(a.getVersion()).isEqualTo(-1);
        assertThat(a.getNextVersion()).isEqualTo(0);

    }

    @Test
    public void testApplyNewChildEvent() throws DuplicateEntityException {

        // PREPARE
        final AId aid = new AId(1);
        final ARoot a = new ARoot(aid);
        final BId bid = new BId(2);
        a.addB(bid);
        final CId cid = new CId(3);
        a.markChangesAsCommitted();

        final CAddedEvent event = new CAddedEvent(aid, bid, cid);

        // TEST
        a.getFirstChild().apply(event);

        // VERIFY
        assertThat(a.getUncommittedChanges()).containsExactly(event);
        assertThat(a.getVersion()).isEqualTo(1);
        assertThat(a.getNextVersion()).isEqualTo(2);
        assertThat(a.getFirstChild().getLastEvent()).isSameAs(event);

    }

    @Test
    public void testGetNextVersion() {

        // PREPARE
        final AId aid = new AId(1);
        final ARoot a = new ARoot();
        assertThat(a.getVersion()).isEqualTo(-1);
        assertThat(a.getNextVersion()).isEqualTo(-1);
        final ACreatedEvent event = new ACreatedEvent(aid);

        // TEST
        a.apply(event);

        // VERIFY
        assertThat(a.getVersion()).isEqualTo(-1);
        assertThat(a.getNextVersion()).isEqualTo(0);

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
        assertThat(a.getVersion()).isEqualTo(-1);

        // TEST
        a.loadFromHistory(event);

        // VERIFY
        assertThat(a.getVersion()).isEqualTo(0);
        assertThat(a.getUncommittedChanges()).isEmpty();

    }

    @Test
    public void testAggregateRootEvent() {

        // PREPARE
        final AId aid = new AId(1);

        // TEST
        final ARoot a = new ARoot(aid);

        // VERIFY
        assertThat(a.getVersion()).isEqualTo(-1);
        assertThat(a.getNextVersion()).isEqualTo(0);
        assertThat(a.getUncommittedChanges()).hasSize(1);
        final DomainEvent<?> ev = a.getUncommittedChanges().get(0);
        assertThat(ev).isSameAs(a.getLastEvent());

    }

    @Test
    public void testChildEvent() throws DuplicateEntityException, EntityNotFoundException {

        // PREPARE
        final AId aid = new AId(1);
        final ARoot a = new ARoot(aid);
        assertThat(a.getVersion()).isEqualTo(-1);
        final BId bid = new BId(2);
        a.addB(bid);
        a.markChangesAsCommitted();
        final CId cid = new CId(3);

        // TEST
        a.addC(bid, cid);

        // VERIFY
        assertThat(a.getVersion()).isEqualTo(1);
        assertThat(a.getUncommittedChanges()).hasSize(1);
        assertThat(a.getNextVersion()).isEqualTo(2);
        final DomainEvent<?> ev = a.getUncommittedChanges().get(0);
        assertThat(ev).isSameAs(a.getFirstChild().getLastEvent());

    }

    @Test
    public void testSubChildEvent() throws DuplicateEntityException, EntityNotFoundException {

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
        assertThat(a.getVersion()).isEqualTo(2);
        assertThat(a.getUncommittedChanges()).hasSize(1);
        assertThat(a.getNextVersion()).isEqualTo(3);
        final DomainEvent<?> ev = a.getUncommittedChanges().get(0);
        assertThat(ev).isSameAs(c.getLastEvent());

    }

    @Test
    public void testMultipleEvent() throws DuplicateEntityException, EntityNotFoundException {

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
        assertThat(a.getVersion()).isEqualTo(1);
        assertThat(a.getUncommittedChanges()).hasSize(2);
        assertThat(a.getNextVersion()).isEqualTo(3);
        final DomainEvent<?> evB = a.getUncommittedChanges().get(0);
        assertThat(evB).isSameAs(a.getFirstChild().getLastEvent());
        final DomainEvent<?> evC = a.getUncommittedChanges().get(1);
        assertThat(evC).isSameAs(a.getFirstChild().getFirstChild().getLastEvent());

    }

}
