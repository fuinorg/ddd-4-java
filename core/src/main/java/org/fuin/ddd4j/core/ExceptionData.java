package org.fuin.ddd4j.core;

import org.fuin.objects4j.common.ToExceptionCapable;
import org.fuin.objects4j.common.ValueObject;

import java.io.Serializable;

/**
 * Base for all classes that store data from an exception for marshalling and allowing to recreate it after unmarshalling.
 * The idea is to transport an exception from the server to the client (without stack trace) and recreate it to be thrown on the client.
 *
 * @param <EX> Concrete type of wrapped exception.
 */
public interface ExceptionData<EX extends Exception> extends Serializable, ValueObject, ToExceptionCapable<EX> {

    /**
     * Returns the name of the data attribute.
     *
     * @return Data element name.
     */
    public String getDataElement();

}
