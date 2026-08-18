package org.fuin.ddd4j.esc;

import java.util.Optional;

import org.fuin.ddd4j.core.TenantId;
import org.fuin.ddd4j.core.ThreadLocalTenantContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link EscTenantContext}.
 */
class EscTenantContextTest {

    private static final TenantId ACME = new TenantId("acme");

    @AfterEach
    void clearTenant() {
        new ThreadLocalTenantContext().clear();
    }

    @Test
    void theDomainsTenantReachesTheEventStore() {

        new ThreadLocalTenantContext().setTenantId(ACME);

        final Optional<org.fuin.esc.api.TenantId> actual = new EscTenantContext().getTenantId();

        assertThat(actual).isPresent();
        // Compared by asString, as org.fuin.esc.api.TenantId's own documentation requires.
        assertThat(actual.get().asString()).isEqualTo("acme");
    }

    @Test
    void noTenantStaysNoTenant() {

        // Not an empty string, and not a fabricated default: the event store must see "no tenant" so that it
        // leaves stream names unprefixed, which is what keeps a single-tenant application working.
        assertThat(new EscTenantContext().getTenantId()).isEmpty();
    }

    @Test
    void theCurrentTenantIsReadPerCallRatherThanCaptured() {

        // The context is handed to a store once, at startup, and consulted on every operation from then on -
        // so it has to follow the thread it is called on. Capturing the tenant at construction would pin the
        // whole application to whichever tenant happened to be current while the beans were built.
        final EscTenantContext testee = new EscTenantContext();

        assertThat(testee.getTenantId()).isEmpty();

        new ThreadLocalTenantContext().setTenantId(ACME);
        assertThat(testee.getTenantId().get().asString()).isEqualTo("acme");

        new ThreadLocalTenantContext().setTenantId(new TenantId("beta"));
        assertThat(testee.getTenantId().get().asString()).isEqualTo("beta");

        new ThreadLocalTenantContext().clear();
        assertThat(testee.getTenantId()).isEmpty();
    }

    @Test
    void anExplicitDelegateIsUsedInstead() {

        final EscTenantContext testee = new EscTenantContext(() -> Optional.of(new TenantId("beta")));

        new ThreadLocalTenantContext().setTenantId(ACME);

        assertThat(testee.getTenantId().get().asString()).isEqualTo("beta");
    }

    @Test
    void aMissingDelegateIsRefusedRatherThanTreatedAsNoTenant() {

        assertThatThrownBy(() -> new EscTenantContext(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("delegate");
    }

}
