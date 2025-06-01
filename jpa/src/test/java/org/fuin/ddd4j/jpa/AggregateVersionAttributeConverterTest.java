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
package org.fuin.ddd4j.jpa;

import jakarta.persistence.Query;
import org.fuin.ddd4j.core.AggregateVersion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the {@link AggregateVersion} class.
 */
public class AggregateVersionAttributeConverterTest extends AbstractPersistenceTest {

    @Test
    public void testJPA() {

        // PREPARE
        beginTransaction();
        final AggregateVersionParentEntity parent1 = new AggregateVersionParentEntity(1);
        getEm().persist(parent1); // No version
        final AggregateVersionParentEntity parent2 = new AggregateVersionParentEntity(2);
        parent2.setVersion(new AggregateVersion(2));
        getEm().persist(parent2);
        commitTransaction();

        // TEST UPDATE
        beginTransaction();
        final AggregateVersionParentEntity copyParent1 = getEm().find(AggregateVersionParentEntity.class, 1L);
        copyParent1.setVersion(new AggregateVersion(1));
        commitTransaction();

        // VERIFY
        beginTransaction();

        // Read using entity manager
        final  AggregateVersionParentEntity p1 = getEm().find(AggregateVersionParentEntity.class, 1L);
        assertThat(p1).isNotNull();
        assertThat(p1.getId()).isEqualTo(1);
        assertThat(p1.getVersion()).isEqualTo(AggregateVersion.valueOf(1));
        final AggregateVersionParentEntity p2 = getEm().find(AggregateVersionParentEntity.class, 2L);
        assertThat(p2).isNotNull();
        assertThat(p2.getId()).isEqualTo(2);
        assertThat(p2.getVersion()).isEqualTo(AggregateVersion.valueOf(2));

        // Select using native SQL
        assertThat(executeSingleResult("select * from AGGREGATE_VERSION_PARENT where VERSION=1")).isNotNull();
        assertThat(executeSingleResult("select * from AGGREGATE_VERSION_PARENT where VERSION=2")).isNotNull();

        commitTransaction();

    }

    private Object executeSingleResult(String sql) {
        final Query query = getEm().createNativeQuery(sql);
        return query.getSingleResult();
    }

}
