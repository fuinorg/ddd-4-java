package org.fuin.ddd4j.core;

import org.fuin.utils4j.TestOmitted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Container for the tenant based on a {@link ThreadLocal}.
 */
@TestOmitted("Trivial")
public class ThreadLocalTenantContext implements WritableTenantContext {

    private static final Logger LOG = LoggerFactory.getLogger(ThreadLocalTenantContext.class);

    private static final ThreadLocal<TenantId> CURRENT = new InheritableThreadLocal<>();

    @Override
    public TenantId getTenantId() {
        return CURRENT.get();
    }

    @Override
    public void setTenantId(TenantId tenantId) {
        LOG.debug("Tenant: {}", tenantId.name());
        CURRENT.set(tenantId);
    }

    @Override
    public void clear() {
        CURRENT.remove();
        LOG.debug("Cleared tenant");
    }

}