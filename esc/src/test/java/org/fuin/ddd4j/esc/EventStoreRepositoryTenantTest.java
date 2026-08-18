package org.fuin.ddd4j.esc;

import java.util.Optional;
import java.util.concurrent.Executors;

import org.fuin.ddd4j.core.TenantContext;
import org.fuin.ddd4j.core.TenantId;
import org.fuin.ddd4j.core.ThreadLocalTenantContext;
import org.fuin.esc.api.EventStore;
import org.fuin.esc.mem.InMemoryEventStore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests that a repository finds the current tenant without having been told where to look.
 *
 * <p>The concrete repositories in an application are generated into its sources once and never regenerated,
 * so anything they must declare for themselves is a line that will be missing from the tenth one somebody
 * writes by hand. A missing tenant does not fail there - it writes to another tenant's stream - which is why
 * the default lives in the base class rather than in what the generator emits.
 */
class EventStoreRepositoryTenantTest {

    private static final TenantId ACME = new TenantId("acme");

    @AfterEach
    void clearTenant() {
        new ThreadLocalTenantContext().clear();
    }

    @Test
    void theCurrentTenantIsFoundWithoutBeingDeclared() throws Exception {

        try (EventStore eventStore = new InMemoryEventStore(Executors.newCachedThreadPool())) {
            eventStore.open();
            final MyCustomerRepository testee = new MyCustomerRepository(eventStore, null, null);

            new ThreadLocalTenantContext().setTenantId(ACME);

            assertThat(testee.getTenantContext()).isPresent();
            assertThat(testee.getTenantContext().get().getTenantId()).contains(ACME);
        }
    }

    @Test
    void noTenantOnTheThreadReadsAsNoTenantAtAll() throws Exception {

        // The single-tenant case, and the reason this default is safe to apply everywhere: the caller does
        // .map(getTenantId).filter(isPresent), so a context holding nothing is treated exactly like no
        // context. Nothing changes for an application that never sets a tenant.
        try (EventStore eventStore = new InMemoryEventStore(Executors.newCachedThreadPool())) {
            eventStore.open();
            final MyCustomerRepository testee = new MyCustomerRepository(eventStore, null, null);

            assertThat(testee.getTenantContext()).isPresent();
            assertThat(testee.getTenantContext().get().getTenantId()).isEmpty();
        }
    }

    @Test
    void anExplicitContextWins() throws Exception {

        // An application that resolves its tenant some other way is not overruled by the default.
        try (EventStore eventStore = new InMemoryEventStore(Executors.newCachedThreadPool())) {
            eventStore.open();
            final TenantContext fixed = () -> Optional.of(new TenantId("beta"));
            final MyCustomerRepository testee = new MyCustomerRepository(eventStore, null, null, fixed);

            new ThreadLocalTenantContext().setTenantId(ACME);

            assertThat(testee.getTenantContext().get().getTenantId()).contains(new TenantId("beta"));
        }
    }

}
