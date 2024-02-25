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

import org.fuin.ddd4j.coretest.AId;
import org.fuin.ddd4j.coretest.BId;
import org.fuin.ddd4j.coretest.CId;
import org.fuin.objects4j.common.ConstraintViolationException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.fail;

public class EntityIdPathTest {

    @Test
    public void testConstructorArray() {

        // PREPARE
        final AId aid = new AId(1L);

        // TEST
        final EntityIdPath testee = new EntityIdPath(aid);

        // VERIFY
        assertThat(testee.size()).isEqualTo(1);
        final AId firstId = testee.first();
        assertThat(firstId).isEqualTo(aid);

    }

    @Test
    public void testConstructorList() {

        // PREPARE
        final AId aid = new AId(1L);
        final List<EntityId> list = new ArrayList<>();
        list.add(aid);

        // TEST
        final EntityIdPath testee = new EntityIdPath(list);

        // VERIFY
        assertThat(testee.size()).isEqualTo(1);
        final AId firstId = testee.first();
        assertThat(firstId).isEqualTo(aid);

    }

    @Test
    public void testConstructorArrayNull() {

        try {
            new EntityIdPath((EntityId[]) null);
            fail();
        } catch (ConstraintViolationException ex) {
            assertThat(ex.getMessage()).isEqualTo("The argument 'entityIds' cannot be null");
        }

    }

    @Test
    public void testConstructorArrayEmpty() {

        try {
            new EntityIdPath();
            fail();
        } catch (ConstraintViolationException ex) {
            assertThat(ex.getMessage()).isEqualTo("Identifier array cannot be empty");
        }

    }

    @Test
    public void testConstructorArrayNullValues() {

        try {
            new EntityIdPath(new EntityId[]{null, null, null});
            fail();
        } catch (ConstraintViolationException ex) {
            assertThat(ex.getMessage()).isEqualTo("Identifiers in the array cannot be null");
        }

    }

    @Test
    public void testConstructorListNull() {

        try {
            new EntityIdPath((List<EntityId>) null);
            fail();
        } catch (ConstraintViolationException ex) {
            assertThat(ex.getMessage()).isEqualTo("The argument 'ids' cannot be null");
        }

    }

    @Test
    public void testConstructorListEmpty() {

        try {
            new EntityIdPath(new ArrayList<>());
            fail();
        } catch (ConstraintViolationException ex) {
            assertThat(ex.getMessage()).isEqualTo("Identifier list cannot be empty");
        }

    }

    @Test
    public void testConstructorListNullValues() {

        try {
            final List<EntityId> list = new ArrayList<>();
            list.add(null);
            new EntityIdPath(list);
            fail();
        } catch (ConstraintViolationException ex) {
            assertThat(ex.getMessage()).isEqualTo("Identifiers in the list cannot be null");
        }

    }

    @Test
    public void testIterator() {

        // PREPARE
        final AId aid = new AId(1L);
        final BId bid = new BId(2L);
        final CId cid = new CId(3L);
        final List<EntityId> list = new ArrayList<>();
        list.add(aid);
        list.add(bid);
        list.add(cid);
        final EntityIdPath testee = new EntityIdPath(list);

        // TEST
        final Iterator<EntityId> it = testee.iterator();

        // VERIFY
        assertThat(it.next()).isEqualTo(aid);
        assertThat(it.next()).isEqualTo(bid);
        assertThat(it.next()).isEqualTo(cid);

    }

    @Test
    public void testFirstLast() {

        // PREPARE
        final AId aid = new AId(1L);
        final BId bid = new BId(2L);
        final CId cid = new CId(3L);
        final List<EntityId> list = new ArrayList<>();
        list.add(aid);
        list.add(bid);
        list.add(cid);
        final EntityIdPath testee = new EntityIdPath(list);

        // TEST & VERIFY
        assertThat((EntityId) testee.first()).isEqualTo(aid);
        assertThat((EntityId) testee.last()).isEqualTo(cid);

    }

    @Test
    public void testRestWithSizeOne() {

        final EntityIdPath testee = new EntityIdPath(new AId(1L));
        assertThat(testee.rest()).isNull();

    }

    @Test
    public void testRestAndSize() {

        // PREPARE
        final AId aid = new AId(1L);
        final BId bid = new BId(2L);
        final CId cid = new CId(3L);
        final List<EntityId> list = new ArrayList<>();
        list.add(aid);
        list.add(bid);
        list.add(cid);
        final EntityIdPath testee = new EntityIdPath(list);
        assertThat(testee.size()).isEqualTo(3);
        assertThat((EntityId) testee.first()).isEqualTo(aid);
        assertThat((EntityId) testee.last()).isEqualTo(cid);

        // TEST
        final EntityIdPath two = testee.rest();

        // VERIFY
        assertThat(two.size()).isEqualTo(2);
        assertThat((EntityId) two.first()).isEqualTo(bid);
        assertThat((EntityId) two.last()).isEqualTo(cid);

        // TEST
        final EntityIdPath one = two.rest();

        // VERIFY
        assertThat(one.size()).isEqualTo(1);
        assertThat((EntityId) one.first()).isEqualTo(cid);
        assertThat((EntityId) one.last()).isEqualTo(cid);

    }

    @Test
    public void testParentWithSizeOne() {

        final EntityIdPath testee = new EntityIdPath(new AId(1L));
        assertThat(testee.parent()).isNull();

    }

    @Test
    public void testParent() {

        // PREPARE
        final AId aid = new AId(1L);
        final BId bid = new BId(2L);
        final CId cid = new CId(3L);
        final List<EntityId> list = new ArrayList<>();
        list.add(aid);
        list.add(bid);
        list.add(cid);
        final EntityIdPath testee = new EntityIdPath(list);

        // TEST
        final EntityIdPath parent = testee.parent();

        // VERIFY
        assertThat(parent.size()).isEqualTo(2);
        assertThat((EntityId) parent.first()).isEqualTo(aid);
        assertThat((EntityId) parent.last()).isEqualTo(bid);

        // TEST
        final EntityIdPath parentParent = parent.parent();

        // VERIFY
        assertThat(parentParent.size()).isEqualTo(1);
        assertThat((EntityId) parentParent.first()).isEqualTo(aid);
        assertThat((EntityId) parentParent.last()).isEqualTo(aid);

    }

    @Test
    public void testAsString() {

        // PREPARE
        final AId aid = new AId(1L);
        final BId bid = new BId(2L);
        final CId cid = new CId(3L);
        final List<EntityId> list = new ArrayList<>();
        list.add(aid);
        list.add(bid);
        list.add(cid);
        final EntityIdPath testee = new EntityIdPath(list);

        // TEST
        final String result = testee.asString();

        // VERIFY
        assertThat(result).isEqualTo("A 1/B 2/C 3");

    }

    @Test
    public void testIsValid() {

        // PREPARE
        final EntityIdFactory factory = new MyIdFactory();

        // TEST & VERIFY
        assertThat(EntityIdPath.isValid(factory, null)).isTrue();
        assertThat(EntityIdPath.isValid(factory, "A 1")).isTrue();
        assertThat(EntityIdPath.isValid(factory, "A 1/B 2")).isTrue();
        assertThat(EntityIdPath.isValid(factory, "A 1/B 2/C 3")).isTrue();

        assertThat(EntityIdPath.isValid(factory, "X 1")).isFalse();
        assertThat(EntityIdPath.isValid(factory, "X x")).isFalse();
        assertThat(EntityIdPath.isValid(factory, "A 1/X 2")).isFalse();
        assertThat(EntityIdPath.isValid(factory, "A 1/B 2/X 3")).isFalse();
        assertThat(EntityIdPath.isValid(factory, "")).isFalse();

    }

    @Test
    public void testRequireArgValid() {

        // PREPARE
        final EntityIdFactory factory = new MyIdFactory();

        // TEST & VERIFY

        EntityIdPath.requireArgValid(factory, "a", null);
        EntityIdPath.requireArgValid(factory, "x", "A 1");
        EntityIdPath.requireArgValid(factory, "x", "A 1/B 2");
        EntityIdPath.requireArgValid(factory, "x", "A 1/B 2/C 3");

        assertThatThrownBy(() -> EntityId.requireArgValid(factory, "a", "X 1"))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessage("The argument 'a' is not valid: 'X 1'");

        assertThatThrownBy(() -> EntityId.requireArgValid(factory, "a", ""))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessage("The argument 'a' is not valid: ''");

    }

    @Test
    public void testValueOf() {

        // PREPARE
        final EntityIdFactory factory = new MyIdFactory();
        EntityIdPath path;
        Iterator<EntityId> it;
        EntityId first;
        EntityId last;

        // TEST & VERIFY

        // Single
        path = EntityIdPath.valueOf(factory, "A 1");
        first = path.first();
        last = path.last();
        assertValues(first, AId.class, "A", 1L);
        assertValues(last, AId.class, "A", 1L);
        assertThat(first).isSameAs(last);
        it = path.iterator();
        assertValues(it.next(), AId.class, "A", 1L);
        assertThat(it.hasNext()).isFalse();

        // Two
        path = EntityIdPath.valueOf(factory, "A 1/B 2");
        first = path.first();
        last = path.last();
        assertValues(first, AId.class, "A", 1L);
        assertValues(last, BId.class, "B", 2L);
        it = path.iterator();
        assertValues(it.next(), AId.class, "A", 1L);
        assertValues(it.next(), BId.class, "B", 2L);
        assertThat(it.hasNext()).isFalse();

        // Three
        path = EntityIdPath.valueOf(factory, "A 1/B 2/C 3");
        first = path.first();
        last = path.last();
        assertValues(first, AId.class, "A", 1L);
        assertValues(last, CId.class, "C", 3L);
        it = path.iterator();
        assertValues(it.next(), AId.class, "A", 1L);
        assertValues(it.next(), BId.class, "B", 2L);
        assertValues(it.next(), CId.class, "C", 3L);
        assertThat(it.hasNext()).isFalse();

    }

    private void assertValues(EntityId entityId, Class<?> typeClass, String type, long id) {
        assertThat(entityId).isInstanceOf(typeClass);
        assertThat(entityId.getType().asString()).isEqualTo(type);
        assertThat(entityId.asString()).isEqualTo("" + id);
    }

    private static final class MyIdFactory implements EntityIdFactory {

        @Override
        public EntityId createEntityId(final String type, final String id) {
            if (type.equals("A")) {
                return new AId(Long.parseLong(id));
            }
            if (type.equals("B")) {
                return new BId(Long.parseLong(id));
            }
            if (type.equals("C")) {
                return new CId(Long.parseLong(id));
            }
            throw new IllegalArgumentException("Unknown type: '" + type + "'");
        }

        @Override
        public boolean containsType(final String type) {
            if (type.equals("A")) {
                return true;
            }
            if (type.equals("B")) {
                return true;
            }
            if (type.equals("C")) {
                return true;
            }
            return false;
        }

        @Override
        public boolean isValid(String type, String id) {
            try {
                if (type.equals("A")) {
                    Long.parseLong(id);
                    return true;
                }
                if (type.equals("B")) {
                    Long.parseLong(id);
                    return true;
                }
                if (type.equals("C")) {
                    Long.parseLong(id);
                    return true;
                }
                return false;
            } catch (final NumberFormatException ex) {
                return false;
            }
        }

    }

}
