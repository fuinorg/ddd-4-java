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
import org.fuin.ddd4j.coretest.ARoot;
import org.fuin.ddd4j.coretest.BAddedEvent;
import org.fuin.ddd4j.coretest.BId;
import org.fuin.ddd4j.coretest.BaseRoot;
import org.fuin.ddd4j.coretest.DEvent;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MethodExecutorTest {

    @Test
    public void testFindDeclaredAnnotatedMethodAndInvoke() throws DuplicateEntityException {

        // PREPARE
        final MethodExecutor testee = new MethodExecutor();

        final AId aid = new AId(1);
        final ARoot a = new ARoot(aid);

        final BId bid = new BId(2);

        a.addB(bid);

        final Method method = testee.findDeclaredAnnotatedMethod(a, ChildEntityLocator.class, BId.class);

        // TEST
        Entity<?> found = testee.invoke(method, a, bid);

        // VERIFY
        assertThat(found.getId()).isSameAs(bid);

    }

    @Test
    public void testFindAnnotatedMethodInBaseClassAndInvoke() throws DuplicateEntityException {

        // PREPARE
        final MethodExecutor testee = new MethodExecutor();

        final AId aid = new AId(1);
        final ARoot a = new ARoot(aid);
        final DEvent event = new DEvent(aid);

        final Method method = testee.findDeclaredAnnotatedMethod(a, ApplyEvent.class, DEvent.class);

        // TEST
        testee.invoke(method, a, event);

        // VERIFY
        assertThat(a.getStored()).isSameAs(event);

    }

    @Test
    public void testGetDeclaredMethodsIncludingSuperClasses() throws NoSuchMethodException, SecurityException {

        // PREPARE
        final MethodExecutor testee = new MethodExecutor();
        final Method applyEventB = ARoot.class.getDeclaredMethod("applyEvent", BAddedEvent.class);
        final Method applyEventD = BaseRoot.class.getDeclaredMethod("applyEvent", DEvent.class);

        // TEST
        final List<Method> methods1 = testee.getDeclaredMethodsIncludingSuperClasses(ARoot.class, AbstractAggregateRoot.class);

        // VERIFY
        assertThat(methods1).contains(applyEventB, applyEventD);

        // TEST
        final List<Method> methods2 = testee.getDeclaredMethodsIncludingSuperClasses(ARoot.class);

        // VERIFY
        assertThat(methods2).contains(applyEventB, applyEventD);

    }

}
