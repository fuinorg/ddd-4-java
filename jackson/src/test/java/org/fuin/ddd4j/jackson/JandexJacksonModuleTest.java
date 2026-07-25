package org.fuin.ddd4j.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.fuin.ddd4j.jacksontest.PersonName;
import org.fuin.ddd4j.jacksontest.Quantity;
import org.fuin.ddd4j.jacksontest.VendorId;
import org.fuin.ddd4j.jacksontest.VendorKey;
import org.fuin.ddd4j.jacksontest.VendorName;
import org.fuin.ddd4j.jacksontest.Weight;
import org.fuin.ddd4j.core.AggregateVersion;
import org.fuin.ddd4j.core.EntityIdPath;
import org.fuin.ddd4j.core.EventId;
import org.fuin.ddd4j.core.EventType;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for the {@link JandexJacksonModule} class.
 */
class JandexJacksonModuleTest {

    @Test
    void testScanFindsValueObjects() {
        assertThat(testee().getValueObjectClasses())
                .contains(VendorName.class, VendorKey.class, PersonName.class, Quantity.class, Weight.class);
    }

    @Test
    void testScanIgnoresEntityIds() {
        // An entity identifier is a ValueObjectWithBaseType too, but Ddd4JacksonModule registers it with
        // the type in front of the value. Registering it here would silently replace that with a raw UUID.
        assertThat(testee().getValueObjectClasses()).doesNotContain(VendorId.class);
    }

    @Test
    void testScanIgnoresTypesHandledByDdd4JacksonModule() {
        assertThat(testee().getValueObjectClasses())
                .doesNotContain(AggregateVersion.class, EntityIdPath.class, EventId.class, EventType.class);
    }

    @Test
    void testScanOnlyRegistersTheGivenPackages() {
        // A library value object may already be registered by its own module with a (de)serializer that
        // does not agree with the generic one. Naming the application's packages keeps them apart.
        final JandexJacksonModule testee = new JandexJacksonModule(
                List.of("org.fuin.ddd4j.jacksontest"), new File("target/test-classes"));
        assertThat(testee.getValueObjectClasses()).contains(VendorName.class, Quantity.class, Weight.class);
        assertThat(testee.getValueObjectClasses())
                .allSatisfy(clasz -> assertThat(clasz.getName()).startsWith("org.fuin.ddd4j.jacksontest."));
    }

    @Test
    void testScanIgnoresAbstractClassesAndInterfaces() {
        assertThat(testee().getValueObjectClasses()).allSatisfy(clasz -> {
            assertThat(clasz.isInterface()).isFalse();
            assertThat(java.lang.reflect.Modifier.isAbstract(clasz.getModifiers())).isFalse();
        });
    }

    @Test
    void testStringBasedValueObject() throws Exception {
        assertThat(mapper().writeValueAsString(new VendorName("Hazel Ltd.")))
                .isEqualTo("\"Hazel Ltd.\"");
        assertThat(mapper().readValue("\"Hazel Ltd.\"", VendorName.class))
                .isEqualTo(new VendorName("Hazel Ltd."));
    }

    @Test
    void testDecimalBasedValueObjectWithoutStaticFactory() throws Exception {
        // Weight has only a public constructor - the scan has to fall back to it.
        final String json = mapper().writeValueAsString(new Weight(new BigDecimal("12.50")));
        assertThat(json).isEqualTo("12.50").doesNotContain("baseType");
        assertThat(mapper().readValue(json, Weight.class)).isEqualTo(new Weight(new BigDecimal("12.50")));
    }

    @Test
    void testLongBasedValueObjectWithStaticFactory() throws Exception {
        // Quantity is AsStringCapable, but its base type is a long, so it stays a JSON number.
        final String json = mapper().writeValueAsString(new Quantity(42L));
        assertThat(json).isEqualTo("42").doesNotContain("baseType");
        assertThat(mapper().readValue(json, Quantity.class)).isEqualTo(new Quantity(42L));
    }

    private static JandexJacksonModule testee() {
        return new JandexJacksonModule(List.of(), new File("target/test-classes"));
    }

    private static ObjectMapper mapper() {
        return new ObjectMapper()
                .enable(SerializationFeature.WRITE_BIGDECIMAL_AS_PLAIN)
                .registerModule(testee());
    }

}
