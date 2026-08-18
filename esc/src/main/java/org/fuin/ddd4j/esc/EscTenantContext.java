package org.fuin.ddd4j.esc;

import java.util.Objects;
import java.util.Optional;

import org.fuin.ddd4j.core.ThreadLocalTenantContext;
import org.fuin.esc.api.SimpleTenantId;
import org.fuin.esc.api.TenantId;
import org.fuin.objects4j.common.ThreadSafe;

/**
 * Presents the application's current tenant to the event store.
 *
 * <p>There are two {@code TenantContext} interfaces in this stack with the same name and the same single
 * method: {@link org.fuin.ddd4j.core.TenantContext}, which the application writes when a request arrives,
 * and {@link org.fuin.esc.api.TenantContext}, which the event store reads to prefix every stream it touches.
 * They are separate because the event store knows nothing about domain-driven design and the domain knows
 * nothing about streams - and until this class existed there was nothing to join them, so an application
 * that resolved its tenant correctly still wrote every tenant into the same streams.
 *
 * <p>That failure is silent, which is the reason this is a named class rather than a lambda at each call
 * site: the builder it is handed to defaults to a no-op context, so forgetting it looks exactly like
 * single-tenant operation right up to the point where one tenant reads another's events.
 *
 * <p>Give it to <em>every</em> store that talks to the event store - the event store itself and the
 * projection admin store both prefix names from it, and a projection created without a tenant sources from
 * every tenant's category at once.
 */
@ThreadSafe
public final class EscTenantContext implements org.fuin.esc.api.TenantContext {

    private final org.fuin.ddd4j.core.TenantContext delegate;

    /**
     * Constructor using the ambient thread-local context, which is where both runtimes in this stack keep
     * the current tenant.
     */
    public EscTenantContext() {
        this(new ThreadLocalTenantContext());
    }

    /**
     * Constructor with an explicit context, for an application that resolves its tenant some other way.
     *
     * @param delegate Context the application writes the current tenant to.
     */
    public EscTenantContext(final org.fuin.ddd4j.core.TenantContext delegate) {
        this.delegate = Objects.requireNonNull(delegate, "delegate==null");
    }

    @Override
    public Optional<TenantId> getTenantId() {
        return delegate.getTenantId().map(id -> new SimpleTenantId(id.asString()));
    }

    @Override
    public String toString() {
        return "EscTenantContext[" + delegate.getTenantId().map(Object::toString).orElse("no tenant") + "]";
    }

}
