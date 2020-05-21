package org.fuin.ddd4j.ddd;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fuin.utils4j.JaxbUtils.marshal;
import static org.fuin.utils4j.JaxbUtils.unmarshal;
import static org.fuin.utils4j.Utils4J.deserialize;
import static org.fuin.utils4j.Utils4J.serialize;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import javax.xml.bind.annotation.XmlRootElement;

import org.junit.Test;

//CHECKSTYLE:OFF Test code
public class AbstractEventTest {

    private static final EventType MY_EVENT_1_TYPE = new EventType("MyEvent1");

    private static final EventType MY_EVENT_2_TYPE = new EventType("MyEvent2");

    @Test
    public final void testConstructorDefault() {

        // TEST
        final AbstractEvent testee = new MyEvent1();

        // VERIFY
        assertThat(testee.getEventId()).isNotNull();
        assertThat(testee.getEventTimestamp()).isNotNull();
        assertThat(testee.getCausationId()).isNull();
        assertThat(testee.getCorrelationId()).isNull();
        assertThat(testee.getEventType()).isEqualTo(MY_EVENT_1_TYPE);

    }

    @Test
    public final void testConstructorEvent() {

        // PREPARE
        final EventId correlationId = new EventId();
        final EventId causationId = new EventId();
        final MyEvent2 event = new MyEvent2(correlationId, causationId);

        // TEST
        final AbstractEvent testee = new MyEvent1(event);

        // VERIFY
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

        // TEST
        final MyEvent1 event = testee.eventId(eventId).timestamp(timestamp).causationId(causationId).correlationId(correlationId).build();

        // VERIFY
        assertThat(event.getEventId()).isEqualTo(eventId);
        assertThat(event.getEventTimestamp()).isEqualTo(timestamp);
        assertThat(event.getCausationId()).isEqualTo(causationId);
        assertThat(event.getCorrelationId()).isEqualTo(correlationId);

    }

    @Test
    public final void testSerializeDeserialize() {

        // PREPARE
        final EventId correlationId = new EventId();
        final EventId causationId = new EventId();
        final MyEvent1 original = new MyEvent1(correlationId, causationId);

        // TEST
        final MyEvent1 copy = deserialize(serialize(original));

        // VERIFY
        assertThat(original).isEqualTo(copy);
        assertThat(original.getCausationId()).isEqualTo(copy.getCausationId());
        assertThat(original.getCorrelationId()).isEqualTo(copy.getCorrelationId());
        assertThat(original.getEventId()).isEqualTo(copy.getEventId());
        assertThat(original.getEventType()).isEqualTo(copy.getEventType());
        assertThat(original.getEventTimestamp()).isEqualTo(copy.getEventTimestamp());

    }

    @Test
    public final void testMarshalUnmarshal() {

        // PREPARE
        final EventId correlationId = new EventId();
        final EventId causationId = new EventId();
        final MyEvent1 original = new MyEvent1(correlationId, causationId);

        // TEST
        final String xml = marshal(original, MyEvent1.class);
        final MyEvent1 copy = unmarshal(xml, MyEvent1.class);

        // VERIFY
        assertThat(original).isEqualTo(copy);
        assertThat(original.getCausationId()).isEqualTo(copy.getCausationId());
        assertThat(original.getCorrelationId()).isEqualTo(copy.getCorrelationId());
        assertThat(original.getEventId()).isEqualTo(copy.getEventId());
        assertThat(original.getEventType()).isEqualTo(copy.getEventType());
        assertThat(original.getEventTimestamp()).isEqualTo(copy.getEventTimestamp());

    }

    @Test
    public final void testUnmarshal() {

        // PREPARE
        final String xml = "<my-event-1><event-id>f910c6d7-debc-46e1-ae02-9ca6f4658cf5</event-id>"
                + "<event-timestamp>2016-09-18T10:38:08.0+02:00[Europe/Berlin]</event-timestamp>"
                + "<correlation-id>2a5893a9-00da-4003-b280-98324eccdef1</correlation-id>"
                + "<causation-id>f13d3481-51b7-423f-8fe7-5c342f7d7c46</causation-id></my-event-1>";

        // TEST
        final MyEvent1 copy = unmarshal(xml, MyEvent1.class);

        // VERIFY
        assertThat(copy.getCausationId()).isEqualTo(new EventId(UUID.fromString("f13d3481-51b7-423f-8fe7-5c342f7d7c46")));
        assertThat(copy.getCorrelationId()).isEqualTo(new EventId(UUID.fromString("2a5893a9-00da-4003-b280-98324eccdef1")));
        assertThat(copy.getEventId()).isEqualTo(new EventId(UUID.fromString("f910c6d7-debc-46e1-ae02-9ca6f4658cf5")));
        assertThat(copy.getEventType()).isEqualTo(copy.getEventType());
        assertThat(copy.getEventTimestamp()).isEqualTo(ZonedDateTime.of(2016, 9, 18, 10, 38, 8, 0, ZoneId.of("Europe/Berlin")));

    }

    @XmlRootElement(name = "my-event-1")
    public static class MyEvent1 extends AbstractEvent {

        private static final long serialVersionUID = 1L;

        public MyEvent1() {
            super();
        }

        public MyEvent1(Event respondTo) {
            super(respondTo);
        }

        public MyEvent1(EventId correlationId, EventId causationId) {
            super(correlationId, causationId);
        }

        @Override
        public EventType getEventType() {
            return MY_EVENT_1_TYPE;
        }

        private static class Builder extends AbstractEvent.Builder<MyEvent1, Builder> {

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

    public static class MyEvent2 extends AbstractEvent {

        private static final long serialVersionUID = 1L;

        public MyEvent2(EventId correlationId, EventId causationId) {
            super(correlationId, causationId);
        }

        @Override
        public EventType getEventType() {
            return MY_EVENT_2_TYPE;
        }

    }

}
// CHECKSTYLE:ON
