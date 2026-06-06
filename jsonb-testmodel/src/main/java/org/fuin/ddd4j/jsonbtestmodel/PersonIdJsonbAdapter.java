package org.fuin.ddd4j.jsonbtestmodel;

import org.jspecify.annotations.Nullable;
import jakarta.json.bind.adapter.JsonbAdapter;

import javax.annotation.concurrent.ThreadSafe;

/**
 * Converts a person identifier into an integer and back (JSON-B).
 */
@ThreadSafe
public class PersonIdJsonbAdapter implements JsonbAdapter<PersonId, Integer> {

    @Override
    public @Nullable Integer adaptToJson(@Nullable PersonId obj) throws Exception {
        if (obj == null) {
            return null;
        }
        return obj.asBaseType();
    }

    @Override
    public @Nullable PersonId adaptFromJson(@Nullable Integer obj) throws Exception {
        if (obj == null) {
            return null;
        }
        return PersonId.valueOf(obj);
    }

}
