package org.fuin.ddd4j.core;

import javax.annotation.concurrent.ThreadSafe;

/**
 * Helper to return the current tenant.
 * <p>
 * <b>CAUTION:</b> Implementation must be thread-safe!
 * </p>
 */
@ThreadSafe
public interface TenantContext {

    /**
     * Returns the current unique tenant identifier.
     *
     * @return Tenant ID.
     */
    TenantId getTenantId();

}
