package org.fuin.ddd4j.ddd;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class SimpleDeserializerRegistry implements DeserializerRegistry {

	private Map<Key, Deserializer> map;

	public SimpleDeserializerRegistry() {
		super();
		map = new HashMap<Key, Deserializer>();
	}

	public void add(final String type, final int version,
			final String mimeType, final Charset encoding,
			final Deserializer deserializer) {

		final Key key = new Key(type, version, mimeType, encoding);
		map.put(key, deserializer);

	}

	@Override
	public Deserializer getDeserializer(final String type, final int version,
			final String mimeType, final Charset encoding) {
		final Key key = new Key(type, version, mimeType, encoding);
		return map.get(key);
	}

	private static class Key {

		private final String type;
		private final int version;
		private final String mimeType;
		private final Charset encoding;

		public Key(final String type, final int version, final String mimeType,
				final Charset encoding) {
			this.type = type;
			this.version = version;
			this.mimeType = mimeType;
			this.encoding = encoding;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((encoding == null) ? 0 : encoding.hashCode());
			result = prime * result
					+ ((mimeType == null) ? 0 : mimeType.hashCode());
			result = prime * result + ((type == null) ? 0 : type.hashCode());
			result = prime * result + version;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Key other = (Key) obj;
			if (encoding == null) {
				if (other.encoding != null)
					return false;
			} else if (!encoding.equals(other.encoding))
				return false;
			if (mimeType == null) {
				if (other.mimeType != null)
					return false;
			} else if (!mimeType.equals(other.mimeType))
				return false;
			if (type == null) {
				if (other.type != null)
					return false;
			} else if (!type.equals(other.type))
				return false;
			if (version != other.version)
				return false;
			return true;
		}

	}

}
