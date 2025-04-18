package org.fuin.ddd4j.jsonbtestmodel;

import jakarta.json.bind.adapter.JsonbAdapter;

import javax.annotation.concurrent.ThreadSafe;

/**
 * Converts a person identifier into an integer and back (JSON-B).
 */
@ThreadSafe
public class PersonIdJsonbAdapter implements JsonbAdapter<PersonId, Integer> {

    @Override
    public Integer adaptToJson(PersonId obj) throws Exception {
        if (obj == null) {
            return null;
        }
        return obj.asBaseType();
    }

    @Override
    public PersonId adaptFromJson(Integer obj) throws Exception {
        if (obj == null) {
            return null;
        }
        return PersonId.valueOf(obj);
    }

}
