package org.fuin.ddd4j.ddd;

import java.util.UUID;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Universal unique event identifier.
 */
@XmlJavaTypeAdapter(EventIdConverter.class)
public class EventId extends AbstractUUIDVO implements TechnicalId {

	private static final long serialVersionUID = 1000L;

	/**
	 * Default constructor.
	 */
	public EventId() {
		super();
	}

	/**
	 * Constructor with UUID.
	 * 
	 * @param uuid
	 *            UUID.
	 */
	public EventId(@NotNull UUID uuid) {
		super(uuid);
	}

}
