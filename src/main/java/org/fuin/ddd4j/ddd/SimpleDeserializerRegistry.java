/**
 * Copyright (C) 2013 Future Invent Informationsmanagement GmbH. All rights
 * reserved. <http://www.fuin.org/>
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
 * along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fuin.ddd4j.ddd;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Contains all known deserializer. 
 */
public final class SimpleDeserializerRegistry implements DeserializerRegistry {

	private final Map<Key, Deserializer> map;

	/**
	 * Default constructor.
	 */
	public SimpleDeserializerRegistry() {
		super();
		map = new HashMap<Key, Deserializer>();
	}

	/**
	 * Adds a new deserializer to the registry.
	 * 
	 * @param type Type of the data.
	 * @param version Version of the data.
	 * @param mimeType Mime type.
	 * @param encoding Encoding-
	 * @param deserializer Deserializer.
	 */
	public final void add(final String type, final int version,
			final String mimeType, final Charset encoding,
			final Deserializer deserializer) {

		final Key key = new Key(type, version, mimeType, encoding);
		map.put(key, deserializer);

	}

	@Override
	public final Deserializer getDeserializer(final String type,
			final int version, final String mimeType, final Charset encoding) {
		final Key key = new Key(type, version, mimeType, encoding);
		return map.get(key);
	}

	/**
	 * Key used to find an appropriate deserialize.
	 */
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

		// CHECKSTYLE:OFF Generated code
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = (prime * result)
					+ ((encoding == null) ? 0 : encoding.hashCode());
			result = (prime * result)
					+ ((mimeType == null) ? 0 : mimeType.hashCode());
			result = (prime * result) + ((type == null) ? 0 : type.hashCode());
			result = (prime * result) + version;
			return result;
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final Key other = (Key) obj;
			if (encoding == null) {
				if (other.encoding != null) {
					return false;
				}
			} else if (!encoding.equals(other.encoding)) {
				return false;
			}
			if (mimeType == null) {
				if (other.mimeType != null) {
					return false;
				}
			} else if (!mimeType.equals(other.mimeType)) {
				return false;
			}
			if (type == null) {
				if (other.type != null) {
					return false;
				}
			} else if (!type.equals(other.type)) {
				return false;
			}
			if (version != other.version) {
				return false;
			}
			return true;
		}
		// CHECKSTYLE:ON

	}

}
