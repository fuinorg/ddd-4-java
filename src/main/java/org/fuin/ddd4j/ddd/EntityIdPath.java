package org.fuin.ddd4j.ddd;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.vo.ValueObject;

/**
 * An ordered list of entity identifiers. An aggregate root will be the first
 * entry if it's contained in the list.
 */
@XmlJavaTypeAdapter(EntityIdPathConverter.class)
public final class EntityIdPath implements ValueObject, Serializable {

	private static final long serialVersionUID = 1000L;

	/** Divides the entity identifiers in the path. */
	public static final String PATH_SEPARATOR = "/";

	private List<EntityId> entityIds;

	/**
	 * Constructor with ID array.
	 * 
	 * @param entityIds
	 *            Entity identifier in correct order (from outer to inner).
	 */
	public EntityIdPath(EntityId... entityIds) {
		super();
		Contract.requireArgNotNull("entityIds", entityIds);
		if (entityIds.length == 0) {
			throw new IllegalArgumentException(
					"Identifier array cannot be empty");
		}
		this.entityIds = new ArrayList<EntityId>();
		this.entityIds.addAll(Arrays.asList(entityIds));
	}

	/**
	 * Constructor with ID list.
	 * 
	 * @param entityIds
	 *            Entity identifier in correct order (from outer to inner).
	 */
	public EntityIdPath(final List<EntityId> ids) {
		Contract.requireArgNotNull("ids", ids);
		if (ids.size() == 0) {
			throw new IllegalArgumentException(
					"Identifier list cannot be empty");
		}
		this.entityIds = new ArrayList<EntityId>();
		this.entityIds.addAll(ids);
	}

	/**
	 * Creates a NEW list of the entity identifiers contained in the entity id
	 * path and returns an iterator on it. This means deleting an element using
	 * the {@link Iterator#remove()} method will NOT remove something from this
	 * entity id path.
	 * 
	 * @return Iterator on a new list instance.
	 */
	public final Iterator<EntityId> iterator() {
		return new ArrayList<EntityId>(entityIds).iterator();
	}

	/**
	 * Returns the first entity identifier in the path.
	 * 
	 * @return First entity identifier in the path.
	 */
	public final EntityId first() {
		return entityIds.get(0);
	}

	/**
	 * Returns the last entity identifier in the path.
	 * 
	 * @return Last entity identifier in the path.
	 */
	@SuppressWarnings("unchecked")
	public final <T extends EntityId> T last() {
		return (T) entityIds.get(entityIds.size() - 1);
	}

	/**
	 * Returns the path as string.
	 * 
	 * @return Path.
	 */
	public final String asString() {
		final StringBuilder sb = new StringBuilder();
		for (final EntityId entityId : entityIds) {
			if (sb.length() > 0) {
				sb.append(PATH_SEPARATOR);
			}
			sb.append(entityId.asTypedString());
		}
		return sb.toString();
	}

}
