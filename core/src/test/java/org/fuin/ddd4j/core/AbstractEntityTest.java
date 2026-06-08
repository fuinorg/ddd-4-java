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
import org.fuin.objects4j.common.ConstraintViolationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Test
    public void testBuilderBuild() {

        // PREPARE
        final ARoot root = new ARoot(new AId(1));
        final BId id = new BId(2);

        // TEST
        final BEntity entity = new BEntityBuilder().rootAggregate(root).id(id).build();

        // VERIFY
        assertThat(entity).isNotNull();
        assertThat(entity.getRoot()).isEqualTo(root);
        assertThat(entity.getId()).isEqualTo(id);

    }

    @Test
    public void testBuilderReturnsItself() {

        // PREPARE
        final BEntityBuilder builder = new BEntityBuilder();

        // TEST & VERIFY
        assertThat(builder.rootAggregate(new ARoot(new AId(1)))).isSameAs(builder);
        assertThat(builder.id(new BId(2))).isSameAs(builder);

    }

    @Test
    public void testBuilderRootAggregateNull() {
        assertThatThrownBy(() -> new BEntityBuilder().rootAggregate(null))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("rootAggregate");
    }

    @Test
    public void testBuilderIdNull() {
        assertThatThrownBy(() -> new BEntityBuilder().id(null))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("id");
    }

    @Test
    public void testBuilderEnsureBuildableFailsWhenRootAggregateMissing() {
        assertThatThrownBy(() -> new BEntityBuilder().id(new BId(2)).build())
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("rootAggregate");
    }

    @Test
    public void testBuilderEnsureBuildableFailsWhenIdMissing() {
        assertThatThrownBy(() -> new BEntityBuilder().rootAggregate(new ARoot(new AId(1))).build())
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("id");
    }

    @Test
    public void testBuilderResetsAfterBuild() {

        // PREPARE
        final BEntityBuilder builder = new BEntityBuilder();
        builder.rootAggregate(new ARoot(new AId(1))).id(new BId(2)).build();

        // TEST & VERIFY - the mandatory data was cleared, so a second build fails
        assertThatThrownBy(builder::build)
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("rootAggregate");

    }

    /**
     * Concrete builder used to test the abstract {@link AbstractEntity.Builder}.
     */
    private static final class BEntityBuilder
            extends AbstractEntity.Builder<AId, ARoot, BId, BEntity, BEntityBuilder> {

        @Override
        public BEntity build() {
            ensureBuildableAbstractEntity();
            final BEntity entity = new BEntity(getRootAggregate(), getEntityId());
            resetAbstractEntity();
            return entity;
        }

    }

}
