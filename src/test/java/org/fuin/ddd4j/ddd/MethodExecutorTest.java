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

import java.lang.reflect.Method;

import org.fuin.ddd4j.test.AId;
import org.fuin.ddd4j.test.ARoot;
import org.fuin.ddd4j.test.BId;
import org.junit.Test;

//CHECKSTYLE:OFF
public class MethodExecutorTest {

    @Test
    public void testFindDeclaredAnnotatedMethodAndInvoke() throws DuplicateEntityException {

	// PREPARE
	final MethodExecutor testee = new MethodExecutor();

	final AId aid = new AId(1);
	final ARoot a = new ARoot(aid);

	final BId bid = new BId(2);

	a.addB(bid);

	final Method method = testee.findDeclaredAnnotatedMethod(a,
		ChildEntityLocator.class, BId.class);

	// TEST
	Entity<?> found = testee.invoke(method, a, bid);

	// VERIFY
	assertThat(found.getId()).isSameAs(bid);

    }

}
// CHECKSTYLE:ON
