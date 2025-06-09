package org.fuin.ddd4j.core;

import jakarta.validation.constraints.NotNull;
import org.fuin.objects4j.common.AsStringCapable;
import org.fuin.objects4j.common.ValueObjectWithBaseType;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Unique tenant identifier with a restricted character set.
 * The following rules apply:
 * <ul>
 *     <li>Only lower case characters</li>
 *     <li>First character only 'a'-'z'</li>
 *     <li>Second and later characters only 'a'-z' or '0'-'9' or '_' (underscore)</li>
 *     <li>Last character only 'a'-z' or '0'-'9'</li>
 *     <li>Min length: {@link #MIN_LENGTH} characters</li>
 *     <li>Max length: {@link #MAX_LENGTH} characters</li>
 * </ul>
 *
 * @param name Unique tenant name.
 */
public record TenantId(
        String name) implements AsStringCapable, Serializable, Comparable<TenantId>, ValueObjectWithBaseType<String> {

    @Serial
    private static final long serialVersionUID = 1000L;

    public static final String REG_EXPR = "^[a-z][a-z|0-9|_]*[a-z|0-9]$";

    private static final Pattern PATTERN = Pattern.compile(REG_EXPR);

    static final int MIN_LENGTH = 2;

    static final int MAX_LENGTH = 10;

    public TenantId {
        Objects.requireNonNull(name, "name is mandatory");
        if (name.length() < 2) {
            throw new IllegalArgumentException("Min length for name is " + MIN_LENGTH
                    + " characters, but was: " + name.length() + " (" + name + ")");
        }
        if (name.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Max length for name is " + MAX_LENGTH
                    + " characters, but was: " + name.length() + " (" + name + ")");
        }
        if (!PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException("Expected pattern '" + REG_EXPR + "' for name, but name was:'" + name + "'");
        }
    }

    @Override
    public int compareTo(TenantId other) {
        return name.compareTo(other.name);
    }

    @Override
    public String asString() {
        return name;
    }

    @Override
    @NotNull
    public Class<String> getBaseType() {
        return String.class;
    }

    @Override
    @NotNull
    public String asBaseType() {
        return name;
    }

}
