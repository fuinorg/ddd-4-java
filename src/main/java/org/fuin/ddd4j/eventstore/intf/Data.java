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
package org.fuin.ddd4j.eventstore.intf;

import java.nio.charset.Charset;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.common.Immutable;
import org.fuin.objects4j.common.NeverNull;
import org.fuin.objects4j.vo.ValueObject;

/**
 * Represents a block of data in a serialized form.
 */
@Immutable
@Embeddable
public class Data implements ValueObject {

	/** Unique type of the data. */
	@NotNull
	@Column(name = "DATA_TYPE", length = 255, nullable = false)
	private String type;

	/** Version of the type. */
	@NotNull
	@Column(name = "DATA_VERSION", nullable = false)
	private Integer version;

	/** Internet Media Type that classifies the raw event data. */
	@NotNull
	@Column(name = "DATA_MIME_TYPE", length = 255, nullable = false)
	private String mimeType;

	/** Encoding used for the raw event data. */
	@NotNull
	@Column(name = "DATA_ENCODING", length = 30, nullable = false)
	private String encoding;

	/** Raw event data in format defined by the mime type and encoding. */
	@NotNull
	@Lob
	@Column(name = "DATA_RAW", nullable = false)
	private byte[] raw;

	/**
	 * Protected constructor for deserialization.
	 */
	protected Data() {
		super();
	}

	/**
	 * Creates a data object.
	 * 
	 * @param type
	 *            Unique identifier for the type of data.
	 * @param version
	 *            Version of the data.
	 * @param mimeType
	 *            Internet Media Type that classifies the data.
	 * @param encoding
	 *            Encoding of the data (Examples: "iso-8859-5 or "utf-8").
	 * @param raw
	 *            Raw data block.
	 */
	public Data(@NotNull final String type, final Integer version,
			@NotNull final String mimeType, @NotNull final Charset encoding,
			@NotNull final byte[] raw) {
		super();

		Contract.requireArgNotNull("type", type);
		Contract.requireArgNotNull("version", version);
		Contract.requireArgNotNull("mimeType", mimeType);
		Contract.requireArgNotNull("encoding", encoding);
		Contract.requireArgNotNull("raw", raw);

		this.type = type;
		this.version = version;
		this.mimeType = mimeType;
		this.encoding = encoding.name();
		this.raw = raw;
	}

	/**
	 * Returns the unique identifier for the type of data.
	 * 
	 * @return Unique and never changing type name.
	 */
	@NeverNull
	public final String getType() {
		return type;
	}

	/**
	 * Returns the version of the type.
	 * 
	 * @return Version.
	 */
	@NeverNull
	public final Integer getVersion() {
		return version;
	}

	/**
	 * Returns the Internet Media Type that classifies the data.
	 * 
	 * @return Mime type.
	 */
	@NeverNull
	public final String getMimeType() {
		return mimeType;
	}

	/**
	 * Returns the encoding of the data (Examples: "iso-8859-5 or "utf-8").
	 * 
	 * @return Encoding.
	 */
	@NeverNull
	public final Charset getEncoding() {
		return Charset.forName(encoding);
	}

	/**
	 * Returns the raw data block.
	 * 
	 * @return Raw data.
	 */
	@NeverNull
	public final byte[] getRaw() {
		return raw;
	}

}
