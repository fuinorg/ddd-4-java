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
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fuin.utils4j.Utils4J.deserialize;
import static org.fuin.utils4j.Utils4J.serialize;

/**
 * Tests for {@link DuplicateEntityException}.
 */
public class DuplicateEntityExceptionTest {

    @Test
    public void testSerializeDeserialize() {

        // PREPARE
        final DuplicateEntityException original = new DuplicateEntityException(new EntityIdPath(new AId(1L)), new BId(1));

        // TEST
        final byte[] data = serialize(original);
        final DuplicateEntityException copy = deserialize(data);

        // VERIFY
        assertThat(copy.getShortId()).isEqualTo(original.getShortId());
        assertThat(copy.getParentIdPath()).isEqualTo(original.getParentIdPath());
        assertThat(copy.getEntityId()).isEqualTo(original.getEntityId());
        assertThat(copy.getMessage()).isEqualTo(original.getMessage());

    }
}
