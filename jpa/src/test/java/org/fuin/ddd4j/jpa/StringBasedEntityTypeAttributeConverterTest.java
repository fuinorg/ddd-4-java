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
import org.fuin.ddd4j.core.StringBasedEntityType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the {@link StringBasedEntityType} class.
 */
public class StringBasedEntityTypeAttributeConverterTest extends AbstractPersistenceTest {

    @Test
    public void testJPA() {

        // PREPARE
        beginTransaction();
        final StringBasedEntityTypeParentEntity parent1 = new StringBasedEntityTypeParentEntity(1);
        getEm().persist(parent1); // No version
        final StringBasedEntityTypeParentEntity parent2 = new StringBasedEntityTypeParentEntity(2);
        parent2.setStringBasedEntityType(new StringBasedEntityType("TWO"));
        getEm().persist(parent2);
        commitTransaction();

        // TEST UPDATE
        beginTransaction();
        final StringBasedEntityTypeParentEntity copyParent1 = getEm().find(StringBasedEntityTypeParentEntity.class, 1L);
        copyParent1.setStringBasedEntityType(new StringBasedEntityType("ONE"));
        commitTransaction();

        // VERIFY
        beginTransaction();

        // Read using entity manager
        final  StringBasedEntityTypeParentEntity p1 = getEm().find(StringBasedEntityTypeParentEntity.class, 1L);
        assertThat(p1).isNotNull();
        assertThat(p1.getId()).isEqualTo(1);
        assertThat(p1.getStringBasedEntityType()).isEqualTo(new StringBasedEntityType("ONE"));
        final StringBasedEntityTypeParentEntity p2 = getEm().find(StringBasedEntityTypeParentEntity.class, 2L);
        assertThat(p2).isNotNull();
        assertThat(p2.getId()).isEqualTo(2);
        assertThat(p2.getStringBasedEntityType()).isEqualTo(new StringBasedEntityType("TWO"));

        // Select using native SQL
        assertThat(executeSingleResult("select * from STRING_BASED_ENTITY_TYPE_PARENT where ENTITY_TYPE='ONE'")).isNotNull();
        assertThat(executeSingleResult("select * from STRING_BASED_ENTITY_TYPE_PARENT where ENTITY_TYPE='TWO'")).isNotNull();

        commitTransaction();

    }

    private Object executeSingleResult(String sql) {
        final Query query = getEm().createNativeQuery(sql);
        return query.getSingleResult();
    }

}
