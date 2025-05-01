package org.fuin.ddd4j.jacksontest;

import org.fuin.ddd4j.core.EntityId;
import org.fuin.ddd4j.core.EntityIdFactory;

public final class JacksonTestEntityIdFactory implements EntityIdFactory {

    @Override
    public EntityId createEntityId(final String type, final String id) {
        if (type.equals(AId.TYPE.asString())) {
            return AId.valueOf(id);
        }
        if (type.equals(BId.TYPE.asString())) {
            return BId.valueOf(id);
        }
        if (type.equals(CId.TYPE.asString())) {
            return CId.valueOf(id);
        }
        if (type.equals(VendorId.TYPE.asString())) {
            return VendorId.valueOf(id);
        }
        throw new IllegalArgumentException("Unknown type: '" + type + "'");
    }

    @Override
    public boolean containsType(final String type) {
        if (type.equals(AId.TYPE.asString())) {
            return true;
        }
        if (type.equals(AId.TYPE.asString())) {
            return true;
        }
        if (type.equals(CId.TYPE.asString())) {
            return true;
        }
        if (type.equals(VendorId.TYPE.asString())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isValid(String type, String id) {
        try {
            if (type.equals(AId.TYPE.asString())) {
                return AId.isValid(id);
            }
            if (type.equals(BId.TYPE.asString())) {
                return BId.isValid(id);
            }
            if (type.equals(CId.TYPE.asString())) {
                return CId.isValid(id);
            }
            if (type.equals(VendorId.TYPE.asString())) {
                return VendorId.isValid(id);
            }
            return false;
        } catch (final NumberFormatException ex) {
            return false;
        }
    }

}
