package org.fuin.ddd4j.ddd;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.fuin.objects4j.common.ThreadSafe;
import org.fuin.objects4j.vo.AbstractValueObjectConverter;

@ThreadSafe
@ApplicationScoped
@Converter(autoApply = true)
public final class EntityIdPathConverter extends
		AbstractValueObjectConverter<String, EntityIdPath> implements
		AttributeConverter<EntityIdPath, String> {

	@Inject
	private EntityIdFactory factory;

	@Override
	public final Class<String> getBaseTypeClass() {
		return String.class;
	}

	@Override
	public final Class<EntityIdPath> getValueObjectClass() {
		return EntityIdPath.class;
	}

	@Override
	public final boolean isValid(final String value) {
		if (value == null) {
			return true;
		}
		assertFactoryIsSet();
		final List<Entry> entryList = entries(value);
		if ((entryList == null) || (entryList.size() == 0)) {
			return false;
		}
		for (final Entry entry : entryList) {
			if (!factory.containsType(entry.type)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public final EntityIdPath toVO(final String value) {
		if (value == null) {
			return null;
		}
		assertFactoryIsSet();
		final List<Entry> entryList = entries(value);
		if ((entryList == null) || (entryList.size() == 0)) {
			throw new IllegalArgumentException("Invalid entity path: '" + value
					+ "'");
		}
		final List<EntityId> ids = new ArrayList<EntityId>();
		for (final Entry entry : entryList) {
			ids.add(factory.createEntityId(entry.type, entry.id));
		}
		return new EntityIdPath(ids);
	}

	@Override
	public final String fromVO(final EntityIdPath value) {
		if (value == null) {
			return null;
		}
		return value.asString();
	}

	/**
	 * Sets the factory to a new value.
	 * 
	 * @param factory
	 *            Factory to set.
	 */
	public final void setFactory(final EntityIdFactory factory) {
		this.factory = factory;
	}

	private void assertFactoryIsSet() {
		if (factory == null) {
			throw new IllegalStateException("Factory not set");
		}
	}

	private List<Entry> entries(final String value) {
		final List<Entry> list = new ArrayList<Entry>();
		final StringTokenizer tok = new StringTokenizer(value,
				EntityIdPath.PATH_SEPARATOR);
		while (tok.hasMoreTokens()) {
			final String str = tok.nextToken();
			final int p = str.indexOf(' ');
			if (p == -1) {
				// Error
				return null;
			}
			final String type = str.substring(0, p);
			final String id = str.substring(p + 1);
			list.add(new Entry(type, id));
		}
		return list;
	}

	private static final class Entry {

		public String type;
		public String id;

		public Entry(final String type, final String id) {
			this.type = type;
			this.id = id;
		}

	}

}
