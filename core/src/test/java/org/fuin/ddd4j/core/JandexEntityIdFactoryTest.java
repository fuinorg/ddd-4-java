package org.fuin.ddd4j.core;

import org.fuin.ddd4j.coretest.AId;
import org.fuin.ddd4j.coretest.BId;
import org.fuin.ddd4j.coretest.CId;
import org.fuin.ddd4j.coretest.PersonId;
import org.fuin.ddd4j.coretest.VendorId;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for the {@link JandexEntityIdFactory} class.
 */
class JandexEntityIdFactoryTest {

    @Test
    void testCreate() {
        final JandexEntityIdFactory testee = new JandexEntityIdFactory(new File("target/test-classes"));
        assertThat(testee.getIdClasses()).containsOnly(VendorId.class, PersonId.class, AId.class, BId.class, CId.class);
    }

    @Test
    void testCreateAggregateId() {
        final VendorId id = new VendorId();
        final JandexEntityIdFactory testee = new JandexEntityIdFactory(new File("target/test-classes"));
        final EntityId result = testee.createEntityId(VendorId.TYPE.asString(), id.asString());
        assertThat(result).isInstanceOf(VendorId.class);
        assertThat(result).isEqualTo(id);
    }

    @Test
    void testCreateEntityId() {
        final PersonId id = new PersonId(123);
        final JandexEntityIdFactory testee = new JandexEntityIdFactory(new File("target/test-classes"));
        final EntityId result = testee.createEntityId(PersonId.TYPE.asString(), id.asString());
        assertThat(result).isInstanceOf(PersonId.class);
        assertThat(result).isEqualTo(id);
    }

    @Test
    void testContainsType() {
        final JandexEntityIdFactory testee = new JandexEntityIdFactory(new File("target/test-classes"));
        assertThat(testee.containsType(VendorId.TYPE.asString())).isTrue();
        assertThat(testee.containsType(PersonId.TYPE.asString())).isTrue();
        assertThat(testee.containsType(AId.TYPE.asString())).isTrue();
        assertThat(testee.containsType(BId.TYPE.asString())).isTrue();
        assertThat(testee.containsType(CId.TYPE.asString())).isTrue();
        assertThat(testee.containsType("WHATEVER")).isFalse();
    }

    @Test
    void testIsValid() {
        final JandexEntityIdFactory testee = new JandexEntityIdFactory(new File("target/test-classes"));
        assertThat(testee.isValid(VendorId.TYPE.asString(), "123")).isFalse();
        assertThat(testee.isValid(VendorId.TYPE.asString(), UUID.randomUUID().toString())).isTrue();
        assertThat(testee.isValid(VendorId.TYPE.asString(), null)).isTrue();
    }

}
