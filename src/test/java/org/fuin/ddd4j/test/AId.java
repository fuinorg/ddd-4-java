package org.fuin.ddd4j.test;

import org.fuin.ddd4j.ddd.AggregateRootId;
import org.fuin.ddd4j.ddd.EntityType;

public class AId implements AggregateRootId {

	private final long id;

	public AId(final long id) {
		this.id = id;
	}

	@Override
	public EntityType getType() {
		return new EntityType() {
			@Override
			public String asString() {
				return "A";
			}
		};
	}

	@Override
	public String asString() {
		return "" + id;
	}

	@Override
	public String toString() {
		return asString();
	}

	@Override
	public String asTypedString() {
		return "A " + toString();
	}

}
