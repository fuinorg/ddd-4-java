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
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the {@link RemovedPrivateData} marker interface.
 */
public class RemovedPrivateDataTest {

    @Test
    public void testGetSubjectId() {

        // PREPARE
        final AggregateRootId subjectId = new AId(1L);
        final RemovedPrivateData event = () -> subjectId;

        // TEST & VERIFY: a consumer can recover the shredded subject to purge its derived data
        assertThat(event).isInstanceOf(RemovedPrivateData.class);
        assertThat(event.getSubjectId()).isSameAs(subjectId);
    }

}
