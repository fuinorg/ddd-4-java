package org.fuin.ddd4j.ddd;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * Base class for domain events.
 */
public abstract class AbstractDomainEvent<ID extends EntityId> extends
		AbstractEvent implements DomainEvent<ID> {

	private static final long serialVersionUID = 1000L;

	@XmlAttribute(name = "entity-id-path")
	private EntityIdPath entityIdPath;

	/**
	 * Protected default constructor for deserialization.
	 */
	protected AbstractDomainEvent() {
		super();
	}

	/**
	 * Constructor with entity identifier path.
	 * 
	 * @param entityIdPath
	 *            Identifier path from aggregate root to the entity that emitted
	 *            the event.
	 */
	public AbstractDomainEvent(final EntityIdPath entityIdPath) {
		super();
		this.entityIdPath = entityIdPath;
	}

	@Override
	public final EntityIdPath getEntityIdPath() {
		return entityIdPath;
	}

	@Override
	public final ID getEntityId() {
		return entityIdPath.last();
	}

}
