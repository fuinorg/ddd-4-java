package org.fuin.ddd4j.test;

import org.fuin.ddd4j.ddd.EntityId;
import org.fuin.ddd4j.ddd.EntityType;

public class BId implements EntityId {

	private final long id;

	public BId(final long id) {
		this.id = id;
	}

	@Override
	public EntityType getType() {
		return new EntityType() {
			@Override
			public String asString() {
				return "B";
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
		return "B " + toString();
	}

}
