/**
 * Copyright (C) 2015 Michael Schnell. All rights reserved. 
 * http://www.fuin.org/
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
 * along with this library. If not, see http://www.gnu.org/licenses/.
 */
package org.fuin.ddd4j.ddd;

import io.github.threetenjaxb.core.ZonedDateTimeXmlAdapter;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbTypeAdapter;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.common.Nullable;
import org.fuin.objects4j.ui.Label;
import org.fuin.objects4j.ui.Prompt;
import org.fuin.objects4j.ui.ShortLabel;
import org.fuin.objects4j.ui.Tooltip;

import java.time.ZonedDateTime;

/**
 * Base class for events. Equals and hash code are solely based on the event id.
 */
public abstract class AbstractEvent implements Event {

    private static final long serialVersionUID = 1000L;

    @NotNull(message = "event-id must not be null")
    @JsonbTypeAdapter(EventIdConverter.class)
    @JsonbProperty("event-id")
    @XmlJavaTypeAdapter(EventIdConverter.class)
    @XmlElement(name = "event-id")
    private EventId eventId;

    @Label("Timestamp")
    @ShortLabel("Time")
    @Tooltip("Date/Time the event was created")
    @Prompt("2016-12-31T23:59:59+02:00")
    @NotNull(message = "event-timestamp must not be null")
    @JsonbProperty("event-timestamp")
    @XmlJavaTypeAdapter(ZonedDateTimeXmlAdapter.class)
    @XmlElement(name = "event-timestamp")
    private ZonedDateTime eventTimestamp;

    @Label("Correlation Identifier")
    @ShortLabel("CorrID")
    @Tooltip("Event this one correlates to")
    @Nullable
    @JsonbTypeAdapter(EventIdConverter.class)
    @JsonbProperty("correlation-id")
    @XmlJavaTypeAdapter(EventIdConverter.class)
    @XmlElement(name = "correlation-id")
    private EventId correlationId;

    @Label("Causation Identifier")
    @ShortLabel("CauseID")
    @Tooltip("Event that caused this one")
    @Nullable
    @JsonbTypeAdapter(EventIdConverter.class)
    @JsonbProperty("causation-id")
    @XmlJavaTypeAdapter(EventIdConverter.class)
    @XmlElement(name = "causation-id")
    private EventId causationId;

    /**
     * Default constructor.
     */
    public AbstractEvent() {
        this(null, null);
    }

    /**
     * Constructor with event this one responds to. Convenience method to set the correlation and causation identifiers correctly.
     * 
     * @param respondTo
     *            Causing event.
     */
    public AbstractEvent(@NotNull final Event respondTo) {
        this(respondTo.getCorrelationId(), respondTo.getEventId());
    }

    /**
     * Constructor with optional data.
     * 
     * @param correlationId
     *            Correlation ID.
     * @param causationId
     *            ID of the event that caused this one.
     */
    public AbstractEvent(@Nullable final EventId correlationId, @Nullable final EventId causationId) {
        super();
        this.eventId = new EventId();
        this.eventTimestamp = ZonedDateTime.now();
        this.correlationId = correlationId;
        this.causationId = causationId;
    }

    @Override
    public final EventId getEventId() {
        return eventId;
    }

    @Override
    public final ZonedDateTime getEventTimestamp() {
        return eventTimestamp;
    }

    @Override
    public final EventId getCorrelationId() {
        return correlationId;
    }

    @Override
    public final EventId getCausationId() {
        return causationId;
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((eventId == null) ? 0 : eventId.hashCode());
        return result;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AbstractEvent other = (AbstractEvent) obj;
        if (eventId == null) {
            if (other.eventId != null) {
                return false;
            }
        } else if (!eventId.equals(other.eventId)) {
            return false;
        }
        return true;
    }

    /**
     * Base class for event builders.
     * 
     * @param <TYPE>
     *            Type of the event.
     * @param <BUILDER>
     *            Type of the builder.
     */
    protected abstract static class Builder<TYPE extends AbstractEvent, BUILDER extends Builder<TYPE, BUILDER>> {

        private AbstractEvent delegate;

        /**
         * Constructor with event.
         * 
         * @param delegate
         *            Event to populate with data.
         */
        public Builder(final TYPE delegate) {
            super();
            this.delegate = delegate;
        }

        /**
         * Sets the unique identifier of the event.
         * 
         * @param eventId
         *            Unique event id.
         * 
         * @return This builder.
         */
        @SuppressWarnings("unchecked")
        public final BUILDER eventId(@NotNull final EventId eventId) {
            Contract.requireArgNotNull("eventId", eventId);
            delegate.eventId = eventId;
            return (BUILDER) this;
        }

        /**
         * Sets the date/Time the event was created.
         * 
         * @param eventTimestamp
         *            Date/Time the event was created.
         * 
         * @return This builder.
         */
        @SuppressWarnings("unchecked")
        public final BUILDER timestamp(@NotNull final ZonedDateTime eventTimestamp) {
            Contract.requireArgNotNull("eventTimestamp", eventTimestamp);
            delegate.eventTimestamp = eventTimestamp;
            return (BUILDER) this;
        }

        /**
         * Sets the event this one correlates to.
         * 
         * @param correlationId
         *            Identifier of the event this one correlates to.
         * 
         * @return This builder.
         */
        @SuppressWarnings("unchecked")
        public final BUILDER correlationId(final EventId correlationId) {
            delegate.correlationId = correlationId;
            return (BUILDER) this;
        }

        /**
         * Sets the event that caused this one.
         * 
         * @param causationId
         *            Identifier of the event that caused this one.
         * 
         * @return This builder.
         */
        @SuppressWarnings("unchecked")
        public final BUILDER causationId(final EventId causationId) {
            delegate.causationId = causationId;
            return (BUILDER) this;
        }

        /**
         * Takes the id of the event as causation id and copies the correlation id.
         * 
         * @param event
         *            Event that caused the one that is being build.
         * 
         * @return This builder.
         */
        @SuppressWarnings("unchecked")
        public final BUILDER causingEvent(final Event event) {
            delegate.causationId = event.getEventId();
            delegate.correlationId = event.getCausationId();
            return (BUILDER) this;
        }

        /**
         * Ensures that everything is setup for building the object or throws a runtime exception otherwise.
         */
        protected final void ensureBuildableAbstractEvent() {
            ensureNotNull("eventId", delegate.eventId);
            ensureNotNull("timestamp", delegate.eventTimestamp);
        }

        /**
         * Sets the internal instance to a new one. This must be called within the build method.
         * 
         * @param delegate
         *            Delegate to use.
         */
        protected final void resetAbstractEvent(final TYPE delegate) {
            this.delegate = delegate;
        }

        /**
         * Returns the delegate.
         * 
         * @return Delegate.
         */
        @SuppressWarnings("unchecked")
        protected final TYPE delegate() {
            return (TYPE) delegate;
        }

        /**
         * Ensures that a filed is set or throws a runtime exception otherwise.
         * 
         * @param name
         *            Name of the field.
         * @param value
         *            Value to test for {@literal null}.
         */
        protected final void ensureNotNull(final String name, final Object value) {
            if (value == null) {
                throw new RuntimeException("The value of '" + name + "' has not been set");
            }
        }

    }

}
