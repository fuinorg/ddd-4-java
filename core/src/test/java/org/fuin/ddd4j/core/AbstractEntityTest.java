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

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.fuin.ddd4j.coretest.AId;
import org.fuin.ddd4j.coretest.ARoot;
import org.fuin.ddd4j.coretest.BEntity;
import org.fuin.ddd4j.coretest.BId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AbstractEntityTest {

    @Test
    public void testEqualsHashCode() {
        EqualsVerifier.forClass(BEntity.class).withPrefabValues(AbstractAggregateRoot.class, new ARoot(new AId(1)), new ARoot(new AId(2)))
                .suppress(Warning.NULL_FIELDS, Warning.ALL_FIELDS_SHOULD_BE_USED).withRedefinedSuperclass().verify();
    }

    @Test
    public void testGetRootId() throws DuplicateEntityException {

        // PREPARE
        final AId aid = new AId(1);
        final ARoot a = new ARoot(aid);
        final BId bid = new BId(2);
        final BEntity testee = new BEntity(a, bid);

        // TEST & VERIFY
        assertThat(testee.getRoot()).isEqualTo(a);
        assertThat(testee.getRootId()).isEqualTo(aid);

    }

}
