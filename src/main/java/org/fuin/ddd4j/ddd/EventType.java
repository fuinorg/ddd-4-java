package org.fuin.ddd4j.ddd;

import javax.validation.constraints.Size;

import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.common.Immutable;
import org.fuin.objects4j.common.NotEmpty;
import org.fuin.objects4j.vo.AbstractStringValueObject;

/**
 * Identifies an event type within an aggregate type.
 */
@Immutable
public class EventType extends AbstractStringValueObject<EventType> {

	private static final long serialVersionUID = 1000L;

	@NotEmpty
	@Size(max = 255)
	private String str;

	/**
	 * Constructor with unique name to use.
	 * 
	 * @param str
	 *            Type name of an aggregate that is unique within all types of
	 *            the context
	 */
	public EventType(@NotEmpty @Size(max = 255) final String str) {
		Contract.requireArgNotEmpty("str", str);
		Contract.requireArgMaxLength("str", str, 255);
		this.str = str;
	}

	/**
	 * Returns the type name as string.
	 * 
	 * @return Unique type name.
	 */
	public final String asString() {
		return str;
	}

	@Override
	public final String toString() {
		return str;
	}

}
