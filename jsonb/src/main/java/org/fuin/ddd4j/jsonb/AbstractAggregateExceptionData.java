package org.fuin.ddd4j.jsonb;

import jakarta.json.bind.annotation.JsonbProperty;
import org.fuin.ddd4j.core.AbstractAggregateException;
import org.fuin.ddd4j.core.ExceptionData;
import org.fuin.objects4j.common.ToExceptionCapable;
import org.fuin.objects4j.common.ValueObject;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Base class for storing the data from a {@link AbstractAggregateException} for marshalling and allows recreating it after unmarshalling.
 * The idea is to transport an exception from the server to the client (without stack trace) and recreate it to be thrown on the client.
 *
 * @param <EX> Concrete type of wrapped exception.
 */
public abstract class AbstractAggregateExceptionData<EX extends AbstractAggregateException> implements ExceptionData<EX> {

    @Serial
    private static final long serialVersionUID = 1000L;

    @JsonbProperty("msg")
    private String message;

    @JsonbProperty("aggregate-type")
    private String aggregateType;

    @JsonbProperty("aggregate-id")
    private String aggregateId;

    /**
     * Constructor only for marshalling/unmarshalling.
     */
    protected AbstractAggregateExceptionData() {
        super();
    }

    /**
     * Constructor with exception.
     *
     * @param ex Exception to copy data from.
     */
    public AbstractAggregateExceptionData(final AbstractAggregateException ex) {
        super();
        Objects.requireNonNull(ex, "ex==null");
        this.message = ex.getMessage();
        this.aggregateType = ex.getType();
        this.aggregateId = ex.getId();
    }

    /**
     * Returns the exception message.
     *
     * @return Message.
     */
    public final String getMessage() {
        return message;
    }

    /**
     * Returns the type of the aggregate.
     *
     * @return Aggregate type.
     */
    public final String getAggregateType() {
        return aggregateType;
    }

    /**
     * Returns the unique aggregate identifier.
     *
     * @return Aggregate ID.
     */
    public final String getAggregateId() {
        return aggregateId;
    }

}
