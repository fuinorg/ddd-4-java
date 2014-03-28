package org.fuin.ddd4j.ddd;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Method that is able to return a child entity by it's identifier. The
 * {@link EntityId} is the only argument and the return value is the
 * {@link Entity} that is identified by the given identifier. Returns
 * <code>null</code> if no entity with the ID was found.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ChildEntityLocator {

}
