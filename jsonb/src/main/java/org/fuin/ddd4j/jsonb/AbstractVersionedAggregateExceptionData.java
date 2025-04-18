package org.fuin.ddd4j.jsonb;

import jakarta.json.bind.annotation.JsonbProperty;
import org.fuin.ddd4j.core.AbstractVersionedAggregateException;

import java.io.Serial;

/**
 * Base class for storing the data from a {@link AbstractVersionedAggregateException} for marshalling and allows recreating it after unmarshalling.
 * The idea is to transport an exception from the server to the client (without stack trace) and recreate it to be thrown on the client.
 */
public abstract class AbstractVersionedAggregateExceptionData<EX extends AbstractVersionedAggregateException> extends AbstractAggregateExceptionData<EX> {

    @Serial
    private static final long serialVersionUID = 1000L;

    @JsonbProperty("version")
    private int version;

    /**
     * Constructor only for marshalling/unmarshalling.
     */
    protected AbstractVersionedAggregateExceptionData() {
        super();
    }

    /**
     * Constructor with exception.
     *
     * @param ex Exception to copy data from.
     */
    public AbstractVersionedAggregateExceptionData(final AbstractVersionedAggregateException ex) {
        super(ex);
        this.version = ex.getVersion();
    }

    /**
     * Returns the aggregate version that caused the problem.
     *
     * @return Aggregate version.
     */
    public final int getVersion() {
        return version;
    }

}
