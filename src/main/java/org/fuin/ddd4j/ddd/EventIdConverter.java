package org.fuin.ddd4j.ddd;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.fuin.objects4j.common.ThreadSafe;
import org.fuin.objects4j.vo.AbstractValueObjectConverter;
import org.fuin.objects4j.vo.UUIDStrValidator;

@ThreadSafe
@ApplicationScoped
@Converter(autoApply = true)
public final class EventIdConverter extends
		AbstractValueObjectConverter<String, EventId> implements
		AttributeConverter<EventId, String> {

	@Override
	public Class<EventId> getValueObjectClass() {
		return EventId.class;
	}

	@Override
	public final EventId toVO(final String value) {
		return new EventId(UUID.fromString(value));
	}

	@Override
	public Class<String> getBaseTypeClass() {
		return String.class;
	}

	@Override
	public final boolean isValid(final String value) {
		return UUIDStrValidator.isValid(value);
	}

	@Override
	public final String fromVO(final EventId value) {
		if (value == null) {
			return null;
		}
		return value.toString();
	}

}
