/**
 * Copyright (C) 2015 Michael Schnell. All rights reserved.
 * http://www.fuin.org/
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see http://www.gnu.org/licenses/.
 */
package org.fuin.ddd4j.esc;

import jakarta.validation.constraints.NotNull;
import org.fuin.ddd4j.core.AggregateAlreadyExistsException;
import org.fuin.ddd4j.core.AggregateCache;
import org.fuin.ddd4j.core.AggregateDeletedException;
import org.fuin.ddd4j.core.AggregateNoCache;
import org.fuin.ddd4j.core.AggregateNotFoundException;
import org.fuin.ddd4j.core.AggregateRoot;
import org.fuin.ddd4j.core.AggregateRootId;
import org.fuin.ddd4j.core.AggregateVersionConflictException;
import org.fuin.ddd4j.core.AggregateVersionNotFoundException;
import org.fuin.ddd4j.core.DomainEvent;
import org.fuin.ddd4j.core.EntityType;
import org.fuin.ddd4j.core.ObjectSerDeserializer;
import org.fuin.ddd4j.core.RequiresPartialDecryption;
import org.fuin.ddd4j.core.RequiresPartialEncryption;
import org.fuin.ddd4j.core.TenantContext;
import org.fuin.ddd4j.core.ThreadLocalTenantContext;
import org.fuin.esc.api.DelegatingAsyncEventStore;
import org.fuin.esc.api.EventStore;
import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.common.NotThreadSafe;
import org.fuin.objects4j.crypto.EncryptedDataService;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

/**
 * Event store based repository. This synchronous class is a thin adapter that delegates to an
 * {@link EventStoreRepositoryAsync} (which holds the actual logic) and blocks on the returned futures. The
 * supplied synchronous {@link EventStore} is exposed to the async core through a
 * {@link DelegatingAsyncEventStore} with a same-thread executor, so calls run synchronously on the caller
 * thread - behaviour and exceptions are identical to a direct synchronous implementation.
 *
 * @param <ID>        Type of the aggregate root identifier.
 * @param <AGGREGATE> Type of the aggregate root.
 */
@NotThreadSafe
public abstract class EventStoreRepository<ID extends AggregateRootId, AGGREGATE extends AggregateRoot<ID>>
        implements IEventStoreRepository<ID, AGGREGATE> {

    private final EventStore eventStore;

    private final AggregateCache<AGGREGATE> noCache;

    private final EventStoreRepositoryAsync<ID, AGGREGATE> delegate;

    private final TenantContext tenantContext;

    /**
     * Constructor without encryption support. Events that implement {@link RequiresPartialEncryption} or
     * {@link RequiresPartialDecryption} cannot be handled and will cause an {@link IllegalStateException}.
     *
     * @param eventStore Event store.
     */
    protected EventStoreRepository(final EventStore eventStore) {
        this(eventStore, null, null);
    }

    /**
     * Constructor with partial encryption support. Events that implement {@link RequiresPartialEncryption} are replaced by
     * their encrypted variant before they are appended to the event store, and events that implement
     * {@link RequiresPartialDecryption} are replaced by their decrypted variant after they are read from the event store.
     *
     * @param eventStore           Event store.
     * @param serDeserializer      Serializes/deserializes the encrypted fields (may be {@code null} if no event requires
     *                             partial (de)encryption).
     * @param encryptedDataService Performs the actual encryption/decryption (may be {@code null} if no event requires
     *                             partial (de)encryption).
     */
    protected EventStoreRepository(final EventStore eventStore, @Nullable final ObjectSerDeserializer serDeserializer,
                                   @Nullable final EncryptedDataService encryptedDataService) {
        this(eventStore, serDeserializer, encryptedDataService, new ThreadLocalTenantContext());
    }

    /**
     * Constructor taking the tenant context explicitly, for an application that does not keep the current
     * tenant on the thread.
     *
     * @param eventStore           Event store.
     * @param serDeserializer      Serializes/deserializes the encrypted fields (may be {@code null}).
     * @param encryptedDataService Performs the actual encryption/decryption (may be {@code null}).
     * @param tenantContext        Context the tenant is read from when events are written.
     */
    protected EventStoreRepository(final EventStore eventStore, @Nullable final ObjectSerDeserializer serDeserializer,
                                   @Nullable final EncryptedDataService encryptedDataService,
                                   final TenantContext tenantContext) {
        super();
        Contract.requireArgNotNull("tenantContext", tenantContext);
        this.tenantContext = tenantContext;
        Contract.requireArgNotNull("eventStore", eventStore);
        this.eventStore = eventStore;
        this.noCache = new AggregateNoCache<>();
        this.delegate = new EventStoreRepositoryAsync<ID, AGGREGATE>(
                new DelegatingAsyncEventStore(Runnable::run, eventStore), serDeserializer, encryptedDataService) {
            @Override
            protected String getIdParamName() {
                return EventStoreRepository.this.getIdParamName();
            }

            @Override
            public Class<AGGREGATE> getAggregateClass() {
                return EventStoreRepository.this.getAggregateClass();
            }

            @Override
            public EntityType getAggregateType() {
                return EventStoreRepository.this.getAggregateType();
            }

            @Override
            public AGGREGATE create() {
                return EventStoreRepository.this.create();
            }

            @Override
            public Optional<TenantContext> getTenantContext() {
                return EventStoreRepository.this.getTenantContext();
            }

            @Override
            protected boolean conflictsResolved(final List<DomainEvent<?>> uncommittedChanges,
                                                final List<DomainEvent<?>> unseenEvents) {
                return EventStoreRepository.this.conflictsResolved(uncommittedChanges, unseenEvents);
            }

            @Override
            protected int getMaxTryCount() {
                return EventStoreRepository.this.getMaxTryCount();
            }

            @Override
            public int getReadPageSize() {
                return EventStoreRepository.this.getReadPageSize();
            }

            @Override
            protected AggregateCache<AGGREGATE> getAggregateCache() {
                return EventStoreRepository.this.getAggregateCache();
            }
        };
    }

    /**
     * Returns the context the tenant is read from.
     * <p>
     * Defaults to the ambient {@link ThreadLocalTenantContext}, which is what makes a generated repository
     * tenant-aware without being told to be. That matters because the concrete repositories are written into
     * an application's sources once and never regenerated, so anything they have to declare for themselves is
     * a line that will be missing from the tenth one somebody adds - and a missing tenant here does not fail,
     * it silently writes another tenant's stream.
     * <p>
     * Harmless when there is no multitenancy: with no tenant on the thread the context yields empty, and the
     * caller treats "context with no tenant" exactly as it treats "no context". An application that resolves
     * its tenant some other way passes its own through the constructor.
     *
     * @return Context holding the current tenant, never empty - the tenant inside it may be.
     */
    @Override
    public Optional<TenantContext> getTenantContext() {
        return Optional.of(tenantContext);
    }

    @Override
    public final AGGREGATE read(final ID aggregateId) throws AggregateNotFoundException, AggregateDeletedException {
        try {
            return delegate.read(aggregateId).join();
        } catch (final CompletionException ex) {
            final Throwable cause = unwrap(ex);
            if (cause instanceof AggregateNotFoundException e) {
                throw e;
            }
            if (cause instanceof AggregateDeletedException e) {
                throw e;
            }
            throw asUnchecked(cause);
        }
    }

    @Override
    public final AGGREGATE read(final ID aggregateId, @Nullable final Integer version)
            throws AggregateNotFoundException, AggregateDeletedException, AggregateVersionNotFoundException {
        try {
            return delegate.read(aggregateId, version).join();
        } catch (final CompletionException ex) {
            final Throwable cause = unwrap(ex);
            if (cause instanceof AggregateNotFoundException e) {
                throw e;
            }
            if (cause instanceof AggregateDeletedException e) {
                throw e;
            }
            if (cause instanceof AggregateVersionNotFoundException e) {
                throw e;
            }
            throw asUnchecked(cause);
        }
    }

    @Override
    public final void update(final AGGREGATE aggregate)
            throws AggregateVersionConflictException, AggregateNotFoundException, AggregateDeletedException {
        update(aggregate, null, null);
    }

    @Override
    public final void update(final AGGREGATE aggregate, @Nullable final String metaType, @Nullable final Object metaData)
            throws AggregateVersionConflictException, AggregateNotFoundException, AggregateDeletedException {
        try {
            delegate.update(aggregate, metaType, metaData).join();
        } catch (final CompletionException ex) {
            final Throwable cause = unwrap(ex);
            if (cause instanceof AggregateVersionConflictException e) {
                throw e;
            }
            if (cause instanceof AggregateNotFoundException e) {
                throw e;
            }
            if (cause instanceof AggregateDeletedException e) {
                throw e;
            }
            throw asUnchecked(cause);
        }
    }

    @Override
    public void add(final AGGREGATE aggregate) throws AggregateAlreadyExistsException, AggregateDeletedException {
        add(aggregate, null, null);
    }

    @Override
    public void add(final AGGREGATE aggregate, @Nullable final String metaType, @Nullable final Object metaData)
            throws AggregateAlreadyExistsException, AggregateDeletedException {
        try {
            delegate.add(aggregate, metaType, metaData).join();
        } catch (final CompletionException ex) {
            final Throwable cause = unwrap(ex);
            if (cause instanceof AggregateAlreadyExistsException e) {
                throw e;
            }
            if (cause instanceof AggregateDeletedException e) {
                throw e;
            }
            throw asUnchecked(cause);
        }
    }

    @Override
    public final void delete(final ID aggregateId, @Nullable final Integer expectedVersion) throws AggregateVersionConflictException {
        try {
            delegate.delete(aggregateId, expectedVersion).join();
        } catch (final CompletionException ex) {
            final Throwable cause = unwrap(ex);
            if (cause instanceof AggregateVersionConflictException e) {
                throw e;
            }
            throw asUnchecked(cause);
        }
    }

    @Override
    public final void purge(final ID aggregateId, @Nullable final Integer expectedVersion) throws AggregateVersionConflictException {
        try {
            delegate.purge(aggregateId, expectedVersion).join();
        } catch (final CompletionException ex) {
            final Throwable cause = unwrap(ex);
            if (cause instanceof AggregateVersionConflictException e) {
                throw e;
            }
            throw asUnchecked(cause);
        }
    }

    @Override
    public List<DomainEvent<?>> readEvents(final ID aggregateId, final int startVersion)
            throws AggregateNotFoundException, AggregateDeletedException {
        try {
            return delegate.readEvents(aggregateId, startVersion).join();
        } catch (final CompletionException ex) {
            final Throwable cause = unwrap(ex);
            if (cause instanceof AggregateNotFoundException e) {
                throw e;
            }
            if (cause instanceof AggregateDeletedException e) {
                throw e;
            }
            throw asUnchecked(cause);
        }
    }

    private static Throwable unwrap(final Throwable throwable) {
        Throwable cause = throwable;
        while (((cause instanceof CompletionException) || (cause instanceof ExecutionException))
                && (cause.getCause() != null) && (cause.getCause() != cause)) {
            cause = cause.getCause();
        }
        return cause;
    }

    private static RuntimeException asUnchecked(final Throwable cause) {
        if (cause instanceof RuntimeException re) {
            return re;
        }
        if (cause instanceof Error err) {
            throw err;
        }
        return new RuntimeException(cause);
    }

    /**
     * Checks if the uncommitted changes conflicts with unseen changes from the event store and tries to solve the problem. This method may
     * be overwritten by concrete implementation. Returns FALSE as default if not overwritten in subclasses.
     *
     * @param uncommittedChanges Uncommitted changes.
     * @param unseenEvents       Unseen changes from the event store.
     * @return TRUE if there are no conflicting changes, else FALSE (conflict couldn't be resolved).
     */
    protected boolean conflictsResolved(final List<DomainEvent<?>> uncommittedChanges, final List<DomainEvent<?>> unseenEvents) {
        return false;
    }

    /**
     * Returns the number of tries that should be done to resolve a version conflict. This method may be overwritten by concrete
     * implementation. Returns <code>3</code> as default if not overwritten in subclasses.
     *
     * @return Number of tries.
     */
    protected int getMaxTryCount() {
        return 3;
    }

    /**
     * Returns the aggregate cache. This method may be overwritten by concrete implementation. Returns no cache as default if not
     * overwritten in subclasses.
     *
     * @return Cache.
     */
    @NotNull
    protected AggregateCache<AGGREGATE> getAggregateCache() {
        return noCache;
    }

    /**
     * Returns the number of events to read in a slice. This method may be overwritten by concrete implementation. Returns <code>100</code>
     * as default if not overwritten in subclasses.
     *
     * @return Page size.
     */
    public int getReadPageSize() {
        return 100;
    }

    /**
     * Returns the underlying event store.
     *
     * @return Event store implementation.
     */
    @NotNull
    protected final EventStore getEventStore() {
        return eventStore;
    }

    /**
     * Returns the parameter name for the unique identifier.
     *
     * @return Name to be used as parameter.
     */
    @NotNull
    protected abstract String getIdParamName();

}
