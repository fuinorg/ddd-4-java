package org.fuin.ddd4j.ddd;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * Base class for events.
 */
public abstract class AbstractEvent implements Event {

	private static final long serialVersionUID = 1000L;

	@XmlAttribute(name = "event-id")
	private EventId eventId;

	@XmlAttribute(name = "event-timestamp")
	private Date timestamp;

	/**
	 * Default constructor.
	 */
	public AbstractEvent() {
		super();
		this.eventId = new EventId();
		this.timestamp = new Date();
	}

	@Override
	public final EventId getEventId() {
		return eventId;
	}

	@Override
	public final Date getTimestamp() {
		return timestamp;
	}

	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((eventId == null) ? 0 : eventId.hashCode());
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

}
