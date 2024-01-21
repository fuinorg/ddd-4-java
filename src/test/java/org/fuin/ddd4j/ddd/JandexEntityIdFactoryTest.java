package org.fuin.ddd4j.ddd;

import org.fuin.ddd4j.test.*;
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
    void testCreateEntityId() {
        final VendorId id = new VendorId();
        final JandexEntityIdFactory testee = new JandexEntityIdFactory(new File("target/test-classes"));
        final EntityId result = testee.createEntityId(VendorId.TYPE.asString(), id.asString());
        assertThat(result).isInstanceOf(VendorId.class);
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
