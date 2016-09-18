/**
 * Copyright (C) 2015 Michael Schnell. All rights reserved. 
 * http://www.fuin.org/
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
 * along with this library. If not, see http://www.gnu.org/licenses/.
 */
package org.fuin.ddd4j.ddd;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.fuin.ddd4j.test.AId;
import org.fuin.ddd4j.test.BId;
import org.fuin.ddd4j.test.CId;
import org.fuin.objects4j.common.ConstraintViolationException;
import org.junit.Test;

//CHECKSTYLE:OFF Test code
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
            new EntityIdPath(new EntityId[] {});
            fail();
        } catch (ConstraintViolationException ex) {
            assertThat(ex.getMessage()).isEqualTo("Identifier array cannot be empty");
        }

    }

    @Test
    public void testConstructorArrayNullValues() {

        try {
            new EntityIdPath(new EntityId[] { null, null, null });
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
            new EntityIdPath(new ArrayList<EntityId>());
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
        assertThat((EntityId)testee.first()).isEqualTo(aid);
        assertThat((EntityId)testee.last()).isEqualTo(cid);
        
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
        assertThat((EntityId)testee.first()).isEqualTo(aid);
        assertThat((EntityId)testee.last()).isEqualTo(cid);

        // TEST
        final EntityIdPath two = testee.rest();
        
        // VERIFY
        assertThat(two.size()).isEqualTo(2);
        assertThat((EntityId)two.first()).isEqualTo(bid);
        assertThat((EntityId)two.last()).isEqualTo(cid);

        // TEST
        final EntityIdPath one = two.rest();
        
        // VERIFY
        assertThat(one.size()).isEqualTo(1);
        assertThat((EntityId)one.first()).isEqualTo(cid);
        assertThat((EntityId)one.last()).isEqualTo(cid);
        
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
    
    

}
// CHECKSTYLE:ON
