package org.fuin.ddd4j.jackson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.fuin.ddd4j.core.AbstractAggregateException;
import org.fuin.ddd4j.core.ExceptionData;

import java.io.Serial;
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

    @JsonProperty("msg")
    private String message;

    @JsonProperty("aggregate-type")
    private String aggregateType;

    @JsonProperty("aggregate-id")
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
    @JsonIgnore
    public final String getMessage() {
        return message;
    }

    /**
     * Returns the type of the aggregate.
     *
     * @return Aggregate type.
     */
    @JsonIgnore
    public final String getAggregateType() {
        return aggregateType;
    }

    /**
     * Returns the unique aggregate identifier.
     *
     * @return Aggregate ID.
     */
    @JsonIgnore
    public final String getAggregateId() {
        return aggregateId;
    }

}
