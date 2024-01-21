package org.fuin.ddd4j.ddd;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import org.fuin.ddd4j.test.AId;
import org.fuin.ddd4j.test.VendorId;
import org.fuin.esc.api.HasSerializedDataTypeConstant;
import org.fuin.esc.api.SerializedDataType;
import org.fuin.esc.api.TypeName;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fuin.utils4j.Utils4J.deserialize;
import static org.fuin.utils4j.Utils4J.serialize;
import static org.fuin.utils4j.jaxb.JaxbUtils.marshal;
import static org.fuin.utils4j.jaxb.JaxbUtils.unmarshal;

//CHECKSTYLE:OFF Test code
public class AbstractDomainEventTest {

    private static final EventType MY_EVENT_1_TYPE = new EventType("MyEvent1");

    private static final EventType MY_EVENT_2_TYPE = new EventType("MyEvent2");

    @Test
    public final void testConstructorCorrelationCausationIds() {

        // PREPARE
        final EventId correlationId = new EventId();
        final EventId causationId = new EventId();
        final VendorId vendorId = new VendorId();

        // TEST
        final AbstractDomainEvent<VendorId> testee = new MyEvent1(vendorId, correlationId, causationId);

        // VERIFY
        assertThat(testee.getEntityId()).isEqualTo(vendorId);
        assertThat(testee.getEntityIdPath()).isEqualTo(new EntityIdPath(vendorId));
        assertThat(testee.getEventId()).isNotNull();
        assertThat(testee.getEventTimestamp()).isNotNull();
        assertThat(testee.getCausationId()).isEqualTo(causationId);
        assertThat(testee.getCorrelationId()).isEqualTo(correlationId);
        assertThat(testee.getEventType()).isEqualTo(MY_EVENT_1_TYPE);

    }

    @Test
    public final void testConstructorEvent() {

        // PREPARE
        final AId aid = new AId(1L);
        final EventId correlationId = new EventId();
        final EventId causationId = new EventId();
        final MyEvent2 event = new MyEvent2(aid, correlationId, causationId);
        final VendorId vendorId = new VendorId();

        // TEST
        final AbstractDomainEvent<VendorId> testee = new MyEvent1(vendorId, event);

        // VERIFY
        assertThat(testee.getEntityId()).isEqualTo(vendorId);
        assertThat(testee.getEntityIdPath()).isEqualTo(new EntityIdPath(vendorId));
        assertThat(testee.getEventId()).isNotNull();
        assertThat(testee.getEventTimestamp()).isNotNull();
        assertThat(testee.getCausationId()).isEqualTo(event.getEventId());
        assertThat(testee.getCorrelationId()).isEqualTo(correlationId);
        assertThat(testee.getEventType()).isEqualTo(MY_EVENT_1_TYPE);

    }

    @Test
    public final void testBuilder() {

        // PREPARE
        final MyEvent1.Builder testee = new MyEvent1.Builder();
        final EventId eventId = new EventId();
        final ZonedDateTime timestamp = ZonedDateTime.now();
        final EventId causationId = new EventId();
        final EventId correlationId = new EventId();
        final VendorId vendorId = new VendorId();
        final EntityIdPath entityIdPath = new EntityIdPath(vendorId);

        // TEST
        final MyEvent1 event = testee.eventId(eventId).timestamp(timestamp).causationId(causationId).correlationId(correlationId)
                .entityIdPath(entityIdPath).build();

        // VERIFY
        assertThat(event.getEventId()).isEqualTo(eventId);
        assertThat(event.getEventTimestamp()).isEqualTo(timestamp);
        assertThat(event.getCausationId()).isEqualTo(causationId);
        assertThat(event.getCorrelationId()).isEqualTo(correlationId);
        assertThat(event.getEntityIdPath()).isEqualTo(entityIdPath);

    }

    @Test
    public final void testSerializeDeserialize() {

        // PREPARE
        final VendorId vendorId = new VendorId();
        final EventId correlationId = new EventId();
        final EventId causationId = new EventId();
        final MyEvent1 original = new MyEvent1(vendorId, correlationId, causationId);

        // TEST
        final MyEvent1 copy = deserialize(serialize(original));

        // VERIFY
        assertThat(copy).isEqualTo(original);
        assertThat(copy.getEntityId()).isEqualTo(original.getEntityId());
        assertThat(copy.getEntityIdPath()).isEqualTo(original.getEntityIdPath());
        assertThat(copy.getCausationId()).isEqualTo(original.getCausationId());
        assertThat(copy.getCorrelationId()).isEqualTo(original.getCorrelationId());
        assertThat(copy.getEventId()).isEqualTo(original.getEventId());
        assertThat(copy.getEventType()).isEqualTo(original.getEventType());
        assertThat(copy.getEventTimestamp()).isEqualTo(original.getEventTimestamp());

    }

    @Test
    public final void testMarshalUnmarshal() {

        // PREPARE
        final VendorId vendorId = new VendorId();
        final EventId correlationId = new EventId();
        final EventId causationId = new EventId();
        final MyEvent1 original = new MyEvent1(vendorId, correlationId, causationId);

        // TEST
        final String xml = marshal(original, createXmlAdapter(), MyEvent1.class);
        final MyEvent1 copy = unmarshal(xml, createXmlAdapter(), MyEvent1.class);

        // VERIFY
        assertThat(copy).isEqualTo(original);
        assertThat(copy.getEntityId()).isEqualTo(original.getEntityId());
        assertThat(copy.getEntityIdPath()).isEqualTo(original.getEntityIdPath());
        assertThat(copy.getCausationId()).isEqualTo(original.getCausationId());
        assertThat(copy.getCorrelationId()).isEqualTo(original.getCorrelationId());
        assertThat(copy.getEventId()).isEqualTo(original.getEventId());
        assertThat(copy.getEventType()).isEqualTo(original.getEventType());
        assertThat(copy.getEventTimestamp()).isEqualTo(original.getEventTimestamp());

    }

    @Test
    public final void testUnmarshal() {

        // PREPARE
        final String xml = "<my-event-1>" + "<entity-id-path>Vendor f6773571-f7a8-4b73-8734-e69991d58732</entity-id-path>"
                + "<event-id>f910c6d7-debc-46e1-ae02-9ca6f4658cf5</event-id>"
                + "<event-timestamp>2016-09-18T10:38:08.0+02:00[Europe/Berlin]</event-timestamp>"
                + "<correlation-id>2a5893a9-00da-4003-b280-98324eccdef1</correlation-id>"
                + "<causation-id>f13d3481-51b7-423f-8fe7-5c342f7d7c46</causation-id></my-event-1>";

        // TEST
        final MyEvent1 copy = unmarshal(xml, createXmlAdapter(), MyEvent1.class);

        // VERIFY
        assertThat(copy.getEntityId()).isEqualTo(new VendorId(UUID.fromString("f6773571-f7a8-4b73-8734-e69991d58732")));
        assertThat(copy.getEntityIdPath())
                .isEqualTo(new EntityIdPath(new VendorId(UUID.fromString("f6773571-f7a8-4b73-8734-e69991d58732"))));
        assertThat(copy.getCausationId()).isEqualTo(new EventId(UUID.fromString("f13d3481-51b7-423f-8fe7-5c342f7d7c46")));
        assertThat(copy.getCorrelationId()).isEqualTo(new EventId(UUID.fromString("2a5893a9-00da-4003-b280-98324eccdef1")));
        assertThat(copy.getEventId()).isEqualTo(new EventId(UUID.fromString("f910c6d7-debc-46e1-ae02-9ca6f4658cf5")));
        assertThat(copy.getEventType()).isEqualTo(copy.getEventType());
        assertThat(copy.getEventTimestamp()).isEqualTo(ZonedDateTime.of(2016, 9, 18, 10, 38, 8, 0, ZoneId.of("Europe/Berlin")));

    }

    @XmlRootElement(name = "my-event-1")
    @HasSerializedDataTypeConstant
    public static class MyEvent1 extends AbstractDomainEvent<VendorId> {

        private static final long serialVersionUID = 1L;

        /** Unique name of the event. */
        public static final TypeName TYPE = new TypeName("MyEvent1");

        /** Unique name of the serialized event. */
        public static final SerializedDataType SER_TYPE = new SerializedDataType(TYPE.asBaseType());

        protected MyEvent1() {
            super();
        }

        public MyEvent1(VendorId vendorId, Event respondTo) {
            super(new EntityIdPath(vendorId), respondTo);
        }

        public MyEvent1(VendorId vendorId, EventId correlationId, EventId causationId) {
            super(new EntityIdPath(vendorId), correlationId, causationId);
        }

        @Override
        public EventType getEventType() {
            return MY_EVENT_1_TYPE;
        }

        private static class Builder extends AbstractDomainEvent.Builder<VendorId, MyEvent1, Builder> {

            private MyEvent1 delegate;

            public Builder() {
                super(new MyEvent1());
                delegate = delegate();
            }

            public MyEvent1 build() {
                final MyEvent1 result = delegate;
                delegate = new MyEvent1();
                resetAbstractEvent(delegate);
                return result;
            }

        }

    }

    @HasSerializedDataTypeConstant
    public static class MyEvent2 extends AbstractDomainEvent<VendorId> {

        private static final long serialVersionUID = 1L;

        /** Unique name of the event. */
        public static final TypeName TYPE = new TypeName("MyEvent1");

        /** Unique name of the serialized event. */
        public static final SerializedDataType SER_TYPE = new SerializedDataType(TYPE.asBaseType());

        public MyEvent2(AId id, EventId correlationId, EventId causationId) {
            super(new EntityIdPath(id), correlationId, causationId);
        }

        @Override
        public EventType getEventType() {
            return MY_EVENT_2_TYPE;
        }

    }

    private static final class MyIdFactory implements EntityIdFactory {
        @Override
        public EntityId createEntityId(final String type, final String id) {
            if (type.equals("A")) {
                return new AId(Long.valueOf(id));
            }
            if (type.equals("Vendor")) {
                return new VendorId(UUID.fromString(id));
            }
            throw new IllegalArgumentException("Unknown type: '" + type + "'");
        }

        @Override
        public boolean containsType(final String type) {
            if (type.equals("A")) {
                return true;
            }
            if (type.equals("Vendor")) {
                return true;
            }
            return false;
        }

        @Override
        public boolean isValid(String type, String id) {
            if (type.equals("A")) {
                try {
                    Long.valueOf(id);
                    return true;
                } catch (final NumberFormatException ex) {
                    return false;
                }
            }
            if (type.equals("Vendor")) {
                try {
                    UUID.fromString(id);
                    return true;
                } catch (final IllegalArgumentException ex) {
                    return false;
                }
            }
            return false;
        }
    }

    @SuppressWarnings("rawtypes")
    private static XmlAdapter[] createXmlAdapter() {
        return new XmlAdapter[] { new EntityIdPathConverter(new MyIdFactory()) };
    }

}
// CHECKSTYLE:ON
