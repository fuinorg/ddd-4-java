package org.fuin.ddd4j.esc;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.fuin.ddd4j.core.AggregateRootId;
import org.fuin.ddd4j.core.EntityType;
import org.fuin.ddd4j.jsonbtestmodel.VendorId;
import org.fuin.objects4j.core.KeyValue;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for the {@link AggregateStreamId} class.
 */
public class AggregateStreamIdTest {

    @Test
    void testEqualsHashCode() {
        EqualsVerifier.forClass(AggregateStreamId.class)
                .withIgnoredFields("paramName")
                .withPrefabValues(AggregateRootId.class, new VendorId(), new VendorId())
                .verify();
    }


    @Test
    void testCreate() {

        EntityType type = VendorId.TYPE;
        String paramName = "vendorId";
        AggregateRootId paramValue = new VendorId();
        final AggregateStreamId testee = new AggregateStreamId(type, paramName, paramValue);

        assertThat(testee.getName()).isEqualTo(VendorId.TYPE.asString());
        assertThat(testee.getParameters()).containsOnly(new KeyValue(paramName, paramValue));
        assertThat((String) testee.getSingleParamValue()).isEqualTo(paramValue.asString());

    }

}
