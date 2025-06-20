package org.fuin.ddd4j.core;

import javax.annotation.concurrent.ThreadSafe;

/**
 * A context where the current tenant can be set and reset.
 * <p>
 * <b>CAUTION:</b> Implementation must be thread-safe!
 * </p>
 */
@ThreadSafe
public interface WritableTenantContext extends TenantContext {

    /**
     * Sets the current unique tenant identifier.
     *
     * @param tenantId Tenant ID to use.
     */
    void setTenantId(TenantId tenantId);

    /**
     * Clears the tenant ID in the context.
     */
    void clear();

}
