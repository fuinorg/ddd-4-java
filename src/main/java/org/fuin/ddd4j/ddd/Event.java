package org.fuin.ddd4j.ddd;

import java.io.Serializable;
import java.util.Date;

import org.fuin.objects4j.common.NeverNull;

public interface Event extends Serializable {

	/**
	 * Returns the identifier of the event.
	 * 
	 * @return Unique identifier event.
	 */
	@NeverNull
	public EventId getEventId();

	/**
	 * Returns the type of the event (What happened).
	 * 
	 * @return A text unique for all events of an aggregate.
	 */
	@NeverNull
	public EventType getEventType();

	/**
	 * Date, time and time zone the event was created.
	 * 
	 * @return Event creation date and time.
	 */
	public Date getTimestamp();

}
