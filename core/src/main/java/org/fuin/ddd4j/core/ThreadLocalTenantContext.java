package org.fuin.ddd4j.core;

import org.fuin.utils4j.TestOmitted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Optional;

/**
 * Container for the tenant based on a {@link ThreadLocal}.
 */
@TestOmitted("Trivial")
public class ThreadLocalTenantContext implements WritableTenantContext {

    private static final Logger LOG = LoggerFactory.getLogger(ThreadLocalTenantContext.class);

    private
    static final ThreadLocal<TenantId> CURRENT = new InheritableThreadLocal<>();

    private static final String TENANT_ID_KEY = "tenantId";

    @Override
    public Optional<TenantId> getTenantId() {
        return Optional.ofNullable(CURRENT.get());
    }

    @Override
    public void setTenantId(TenantId tenantId) {
        LOG.debug("Tenant: {}", tenantId.name());
        CURRENT.set(tenantId);
        MDC.put(TENANT_ID_KEY, tenantId.name());
    }

    @Override
    public void clear() {
        CURRENT.remove();
        MDC.remove("tenantId");
        LOG.debug("Cleared tenant");
    }

}