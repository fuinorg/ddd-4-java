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
package org.fuin.ddd4j.jsonb;

import org.fuin.ddd4j.core.AggregateVersion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AggregateVersionJsonbAdapterTest {

    @Test
    public final void testAdaptFromJson() throws Exception {
        final AggregateVersionJsonbAdapter testee = new AggregateVersionJsonbAdapter();
        assertThat(testee.adaptFromJson(null)).isNull();
        assertThat(testee.adaptFromJson(123)).isEqualTo(new AggregateVersion(123));
    }

    @Test
    public final void testAdaptToJson() throws Exception {

        final AggregateVersionJsonbAdapter testee = new AggregateVersionJsonbAdapter();
        assertThat(testee.adaptToJson(null)).isNull();
        assertThat(testee.adaptToJson(new AggregateVersion(123))).isEqualTo(123);

    }

}
