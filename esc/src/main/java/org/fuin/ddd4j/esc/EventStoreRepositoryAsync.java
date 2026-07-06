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
import org.fuin.ddd4j.core.Ddd4JUtils;
import org.fuin.ddd4j.core.DomainEvent;
import org.fuin.ddd4j.core.ObjectSerDeserializer;
import org.fuin.ddd4j.core.RequiresPartialDecryption;
import org.fuin.ddd4j.core.RequiresPartialEncryption;
import org.fuin.ddd4j.core.TenantContext;
import org.fuin.esc.api.CommonEvent;
import org.fuin.esc.api.EventId;
import org.fuin.esc.api.EventStoreAsync;
import org.fuin.esc.api.ExpectedVersion;
import org.fuin.esc.api.SimpleCommonEvent;
import org.fuin.esc.api.SimpleTenantId;
import org.fuin.esc.api.StreamDeletedException;
import org.fuin.esc.api.StreamId;
import org.fuin.esc.api.StreamNotFoundException;
import org.fuin.esc.api.TypeName;
import org.fuin.esc.api.WrongExpectedVersionException;
import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.common.NotThreadSafe;
import org.fuin.objects4j.crypto.DecryptionFailedException;
import org.fuin.objects4j.crypto.EncryptedDataService;
import org.fuin.objects4j.crypto.EncryptionKeyIdUnknownException;
import org.fuin.objects4j.crypto.EncryptionKeyVersionUnknownException;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

/**
 * Asynchronous event store based repository. This class holds the actual repository logic (the synchronous
 * {@link EventStoreRepository} is a thin adapter that delegates to an instance of this class).
 *
 * @param <ID>        Type of the aggregate root identifier.
 * @param <AGGREGATE> Type of the aggregate root.
 */
@NotThreadSafe
public abstract class EventStoreRepositoryAsync<ID extends AggregateRootId, AGGREGATE extends AggregateRoot<ID>>
        implements IEventStoreRepositoryAsync<ID, AGGREGATE> {

    private static final String MAX_AGGREGATE_VERSION_EXCEEDED = "Exceeded maximum number of aggregate versions."
            + " The Event Store operates with 'long' versions but aggregates only can handle 'int' versions.";

    private static final Logger LOG = LoggerFactory.getLogger(EventStoreRepositoryAsync.class);

    private final EventStoreAsync eventStore;

    private final AggregateCache<AGGREGATE> noCache;

    @Nullable
    private final ObjectSerDeserializer serDeserializer;

    @Nullable
    private final EncryptedDataService encryptedDataService;

    /**
     * Constructor without encryption support. Events that implement {@link RequiresPartialEncryption} or
     * {@link RequiresPartialDecryption} cannot be handled and will cause an {@link IllegalStateException}.
     *
     * @param eventStore Asynchronous event store.
     */
    protected EventStoreRepositoryAsync(final EventStoreAsync eventStore) {
        this(eventStore, null, null);
    }

    /**
     * Constructor with partial encryption support.
     *
     * @param eventStore           Asynchronous event store.
     * @param serDeserializer      Serializes/deserializes the encrypted fields (may be {@code null} if no event
     *                             requires partial (de)encryption).
     * @param encryptedDataService Performs the actual encryption/decryption (may be {@code null} if no event
     *                             requires partial (de)encryption).
     */
    protected EventStoreRepositoryAsync(final EventStoreAsync eventStore,
                                        @Nullable final ObjectSerDeserializer serDeserializer,
                                        @Nullable final EncryptedDataService encryptedDataService) {
        super();
        Contract.requireArgNotNull("eventStore", eventStore);
        this.eventStore = eventStore;
        this.serDeserializer = serDeserializer;
        this.encryptedDataService = encryptedDataService;
        this.noCache = new AggregateNoCache<>();
    }

    @Override
    public final CompletableFuture<AGGREGATE> read(final ID aggregateId) {
        Contract.requireArgNotNull("aggregateId", aggregateId);
        AGGREGATE aggregate = getAggregateCache().get(aggregateId, null);
        if (aggregate == null) {
            LOG.debug("Aggregate {} not found in cache", aggregateId.asTypedString());
            aggregate = create();
        }
        return read(aggregate, aggregateId, Integer.MAX_VALUE);
    }

    @Override
    public final CompletableFuture<AGGREGATE> read(final ID aggregateId, @Nullable final Integer version) {
        Contract.requireArgNotNull("aggregateId", aggregateId);
        if (version == null) {
            return read(aggregateId);
        }
        AGGREGATE aggregate = getAggregateCache().get(aggregateId, version);
        if (aggregate == null) {
            aggregate = create();
        } else if (aggregate.getVersion() > version) {
            aggregate = create();
        } else if (aggregate.getVersion() == version) {
            return CompletableFuture.completedFuture(aggregate);
        }
        return read(aggregate, aggregateId, version);
    }

    private CompletableFuture<AGGREGATE> read(final AGGREGATE aggregate, final ID aggregateId,
                                             final int targetAggregateVersion) {
        requireNoUncommittedChanges(aggregate);
        final StreamId streamId = streamId(aggregateId);
        LOG.info("Read aggregate: stream={}, targetVersion={}", streamId, targetAggregateVersion);
        return readSlices(aggregate, aggregateId, streamId, targetAggregateVersion, aggregate.getVersion() + 1)
                .thenCompose(ignored -> {
                    if ((aggregate.getVersion() != targetAggregateVersion) && (targetAggregateVersion < Integer.MAX_VALUE)) {
                        return CompletableFuture.failedFuture(
                                new AggregateVersionNotFoundException(getAggregateType(), aggregateId, targetAggregateVersion));
                    }
                    getAggregateCache().put(aggregate.getId(), aggregate);
                    return CompletableFuture.completedFuture(aggregate);
                });
    }

    private CompletableFuture<Void> readSlices(final AGGREGATE aggregate, final ID aggregateId, final StreamId streamId,
                                               final int targetAggregateVersion, final int sliceStart) {
        final int readPageSize = getReadPageSize();
        final int sliceCount = (readPageSize <= targetAggregateVersion) ? readPageSize : (targetAggregateVersion - sliceStart + 1);
        LOG.debug("Read slice: streamId={}, sliceStart={}, sliceCount={}", streamId, sliceStart, sliceCount);
        return eventStore.readEventsForward(streamId, sliceStart, sliceCount)
                .thenCompose(currentSlice -> {
                    for (final CommonEvent commonEvent : currentSlice.getEvents()) {
                        aggregate.loadFromHistory(decryptIfRequired((DomainEvent<?>) commonEvent.getData()));
                    }
                    if ((aggregate.getVersion() != targetAggregateVersion) && !currentSlice.isEndOfStream()) {
                        return readSlices(aggregate, aggregateId, streamId, targetAggregateVersion,
                                intVersion(currentSlice.getNextEventNumber()));
                    }
                    return CompletableFuture.<Void>completedFuture(null);
                })
                .exceptionallyCompose(ex -> mapReadFailure(ex, aggregateId));
    }

    @Override
    public final CompletableFuture<Void> update(final AGGREGATE aggregate) {
        return update(aggregate, null, null);
    }

    @Override
    public final CompletableFuture<Void> update(final AGGREGATE aggregate, @Nullable final String metaType,
                                                @Nullable final Object metaData) {
        Contract.requireArgNotNull("aggregate", aggregate);
        final StreamId streamId = streamId(aggregate.getId());
        LOG.info("Update aggregate: streamId={}, version={}, nextVersion={}", streamId, aggregate.getVersion(),
                aggregate.getNextVersion());
        final List<CommonEvent> eventDataList = asCommonEvents(aggregate.getUncommittedChanges(), metaType, metaData);
        return attempt(aggregate, streamId, eventDataList, expectedVersion(aggregate), 0);
    }

    private CompletableFuture<Void> attempt(final AGGREGATE aggregate, final StreamId streamId,
                                            final List<CommonEvent> eventDataList, final long expectedVersion,
                                            final int retryCount) {
        return eventStore.appendToStream(streamId, expectedVersion, eventDataList)
                .thenCompose(next -> {
                    final int eventStoreNextVersion = intVersion(next);
                    if ((expectedVersion + eventDataList.size()) != eventStoreNextVersion) {
                        return CompletableFuture.<Void>failedFuture(new IllegalStateException(
                                "Aggregate next version is " + aggregate.getNextVersion() + " but event store's is "
                                        + eventStoreNextVersion));
                    }
                    aggregate.markChangesAsCommitted();
                    return CompletableFuture.<Void>completedFuture(null);
                })
                .exceptionallyCompose(ex -> {
                    final Throwable cause = unwrap(ex);
                    if (cause instanceof WrongExpectedVersionException wev) {
                        LOG.debug("Version conflict: id={}, expected={}, actual={}, retryCount={}",
                                aggregate.getId().asTypedString(), wev.getExpected(), wev.getActual(), retryCount);
                        return resolveConflicts(aggregate, integerVersion(wev.getActual()), retryCount)
                                .thenCompose(newExpected -> attempt(aggregate, streamId, eventDataList,
                                        newExpected.longValue(), retryCount + 1));
                    }
                    if ((cause instanceof StreamDeletedException) || (cause instanceof StreamNotFoundException)) {
                        return CompletableFuture.failedFuture(new AggregateNotFoundException(getAggregateType(), aggregate.getId()));
                    }
                    return CompletableFuture.failedFuture(cause);
                });
    }

    @Override
    public final CompletableFuture<Void> add(final AGGREGATE aggregate) {
        return add(aggregate, null, null);
    }

    @Override
    public final CompletableFuture<Void> add(final AGGREGATE aggregate, @Nullable final String metaType,
                                            @Nullable final Object metaData) {
        return update(aggregate, metaType, metaData).exceptionallyCompose(ex -> {
            final Throwable cause = unwrap(ex);
            if (cause instanceof AggregateVersionConflictException conflict) {
                return CompletableFuture.failedFuture(
                        new AggregateAlreadyExistsException(getAggregateType(), aggregate.getId(), conflict.getActual()));
            }
            if (cause instanceof AggregateNotFoundException) {
                return CompletableFuture.failedFuture(new IllegalStateException(cause));
            }
            return CompletableFuture.failedFuture(cause);
        });
    }

    private CompletableFuture<Integer> resolveConflicts(final AGGREGATE aggregate, final Integer actualVersion,
                                                        final int retryCount) {
        final CompletableFuture<Integer> latestVersionFuture;
        if (actualVersion == null || actualVersion < 0) {
            // TODO Remove workaround if event store returns latest version.
            // See https://github.com/EventStore/EventStore/issues/1052
            latestVersionFuture = read(aggregate.getId()).thenApply(AggregateRoot::getVersion);
        } else {
            latestVersionFuture = CompletableFuture.completedFuture(actualVersion);
        }
        return latestVersionFuture.thenCompose(latestVersion -> {
            if (retryCount == getMaxTryCount()) {
                return CompletableFuture.<Integer>failedFuture(new AggregateVersionConflictException(
                        getAggregateType(), aggregate.getId(), aggregate.getVersion(), latestVersion));
            }
            return readEvents(aggregate.getId(), aggregate.getVersion() + 1).thenCompose(unseenEvents -> {
                if (conflictsResolved(aggregate.getUncommittedChanges(), unseenEvents)) {
                    return CompletableFuture.completedFuture(latestVersion);
                }
                return CompletableFuture.<Integer>failedFuture(new AggregateVersionConflictException(
                        getAggregateType(), aggregate.getId(), aggregate.getVersion(), latestVersion));
            });
        });
    }

    @Override
    public final CompletableFuture<Void> delete(final ID aggregateId, @Nullable final Integer expectedVersion) {
        Contract.requireArgNotNull("aggregateId", aggregateId);
        final StreamId streamId = streamId(aggregateId);
        LOG.info("Delete aggregate: streamId={}, expectedVersion={}", streamId, expectedVersion);
        final CompletableFuture<Void> deletion = (expectedVersion == null)
                ? eventStore.deleteStream(streamId, false)
                : eventStore.deleteStream(streamId, expectedVersion.longValue(), false);
        return deletion.exceptionallyCompose(ex -> {
            final Throwable cause = unwrap(ex);
            if (cause instanceof WrongExpectedVersionException wev) {
                return CompletableFuture.failedFuture(new AggregateVersionConflictException(getAggregateType(),
                        aggregateId, integerVersion(wev.getExpected()), integerVersion(wev.getActual())));
            }
            if (cause instanceof StreamDeletedException) {
                LOG.debug("Aggregate {} was already deleted: {}", aggregateId, cause.getMessage());
                return CompletableFuture.completedFuture(null);
            }
            return CompletableFuture.failedFuture(cause);
        });
    }

    @Override
    public final CompletableFuture<List<DomainEvent<?>>> readEvents(final ID aggregateId, final int startVersion) {
        final StreamId streamId = streamId(aggregateId);
        LOG.info("Read events: streamId={}, startVersion={}", streamId, startVersion);
        return readEventsInto(aggregateId, streamId, new ArrayList<>(), startVersion);
    }

    private CompletableFuture<List<DomainEvent<?>>> readEventsInto(final ID aggregateId, final StreamId streamId,
                                                                  final List<DomainEvent<?>> list, final int sliceStart) {
        final int sliceCount = getReadPageSize();
        return eventStore.readEventsForward(streamId, sliceStart, sliceCount)
                .thenCompose(currentSlice -> {
                    for (final CommonEvent commonEvent : currentSlice.getEvents()) {
                        list.add(decryptIfRequired((DomainEvent<?>) commonEvent.getData()));
                    }
                    if (!currentSlice.isEndOfStream()) {
                        return readEventsInto(aggregateId, streamId, list, intVersion(currentSlice.getNextEventNumber()));
                    }
                    return CompletableFuture.completedFuture(list);
                })
                .exceptionallyCompose(ex -> {
                    final Throwable cause = unwrap(ex);
                    if (cause instanceof StreamNotFoundException) {
                        return CompletableFuture.failedFuture(new AggregateNotFoundException(getAggregateType(), aggregateId));
                    }
                    if (cause instanceof StreamDeletedException) {
                        return CompletableFuture.failedFuture(new AggregateDeletedException(getAggregateType(), aggregateId));
                    }
                    return CompletableFuture.failedFuture(cause);
                });
    }

    private <T> CompletableFuture<T> mapReadFailure(final Throwable ex, final ID aggregateId) {
        final Throwable cause = unwrap(ex);
        if (cause instanceof StreamNotFoundException) {
            return CompletableFuture.failedFuture(new AggregateNotFoundException(getAggregateType(), aggregateId));
        }
        if (cause instanceof StreamDeletedException) {
            return CompletableFuture.failedFuture(new AggregateDeletedException(getAggregateType(), aggregateId));
        }
        return CompletableFuture.failedFuture(cause);
    }

    private void requireNoUncommittedChanges(final AGGREGATE aggregate) {
        if (aggregate.hasUncommitedChanges()) {
            throw new IllegalArgumentException(
                    "The aggregate '" + getAggregateType() + "' (" + aggregate.getId() + ") has uncommitted changes");
        }
    }

    private List<CommonEvent> asCommonEvents(final List<DomainEvent<?>> events, @Nullable final String metaType,
                                             @Nullable final Object metaData) {
        final SimpleTenantId tenantId = getTenantContext()
                .map(TenantContext::getTenantId)
                .filter(Optional::isPresent)
                .map(tid -> new SimpleTenantId(tid.get().asString()))
                .orElse(null);
        final List<CommonEvent> list = new ArrayList<>();
        for (final DomainEvent<?> original : events) {
            final DomainEvent<?> event = encryptIfRequired(original);
            // Categories are derived from the original (logical) event so an encrypted-variant replacement
            // does not lose the category tags a projection selects on.
            final List<String> categories = new ArrayList<>(Ddd4JUtils.eventCategories(original));
            final SimpleCommonEvent sce;
            if (metaData == null) {
                sce = new SimpleCommonEvent(new EventId(event.getEventId().asBaseType()),
                        new TypeName(event.getEventType().asBaseType()), event, null, null, tenantId, categories);
            } else {
                if (metaType == null) {
                    throw new IllegalArgumentException("Argument 'metaType' cannot be null if 'metaData' is provided (non-null)");
                }
                sce = new SimpleCommonEvent(new EventId(event.getEventId().asBaseType()),
                        new TypeName(event.getEventType().asBaseType()), event, new TypeName(metaType), metaData, tenantId,
                        categories);
            }
            list.add(sce);
        }
        return list;
    }

    private DomainEvent<?> encryptIfRequired(final DomainEvent<?> event) {
        if (event instanceof RequiresPartialEncryption) {
            try {
                return (DomainEvent<?>) ((RequiresPartialEncryption<?, ?>) event).encrypt(requireSerDeserializer(), requireService());
            } catch (final EncryptionKeyIdUnknownException ex) {
                throw new RuntimeException("Failed to encrypt event " + event.getEventId(), ex);
            }
        }
        return event;
    }

    private DomainEvent<?> decryptIfRequired(final DomainEvent<?> event) {
        if (event instanceof RequiresPartialDecryption) {
            try {
                return (DomainEvent<?>) ((RequiresPartialDecryption<?, ?>) event).decrypt(requireSerDeserializer(), requireService());
            } catch (final EncryptionKeyVersionUnknownException | DecryptionFailedException | IOException ex) {
                throw new RuntimeException("Failed to decrypt event " + event.getEventId(), ex);
            }
        }
        return event;
    }

    private ObjectSerDeserializer requireSerDeserializer() {
        if (serDeserializer == null) {
            throw new IllegalStateException("An event requires partial (de)encryption, but this repository was created without an"
                    + " ObjectSerDeserializer. Use the constructor that accepts an ObjectSerDeserializer and an EncryptedDataService.");
        }
        return serDeserializer;
    }

    private EncryptedDataService requireService() {
        if (encryptedDataService == null) {
            throw new IllegalStateException("An event requires partial (de)encryption, but this repository was created without an"
                    + " EncryptedDataService. Use the constructor that accepts an ObjectSerDeserializer and an EncryptedDataService.");
        }
        return encryptedDataService;
    }

    private int expectedVersion(final AGGREGATE aggregate) {
        if (aggregate.getVersion() == -1) {
            return intVersion(ExpectedVersion.NO_OR_EMPTY_STREAM.getNo());
        }
        return aggregate.getVersion();
    }

    private int intVersion(final long version) {
        if (version > Integer.MAX_VALUE) {
            throw new IllegalStateException(MAX_AGGREGATE_VERSION_EXCEEDED);
        }
        return (int) version;
    }

    private int integerVersion(@Nullable final Long version) {
        if (version == null) {
            return Integer.MIN_VALUE;
        }
        if (version > Integer.MAX_VALUE) {
            throw new IllegalStateException(MAX_AGGREGATE_VERSION_EXCEEDED);
        }
        return version.intValue();
    }

    private StreamId streamId(final ID aggregateId) {
        return new AggregateStreamId(getAggregateType(), getIdParamName(), aggregateId);
    }

    private static Throwable unwrap(final Throwable throwable) {
        Throwable cause = throwable;
        while (((cause instanceof CompletionException) || (cause instanceof ExecutionException))
                && (cause.getCause() != null) && (cause.getCause() != cause)) {
            cause = cause.getCause();
        }
        return cause;
    }

    /**
     * Checks if the uncommitted changes conflict with unseen changes from the event store and tries to solve the
     * problem. May be overridden by concrete implementations. Returns {@code false} (unresolved) as default.
     *
     * @param uncommittedChanges Uncommitted changes.
     * @param unseenEvents       Unseen changes from the event store.
     * @return {@code true} if there are no conflicting changes, else {@code false}.
     */
    protected boolean conflictsResolved(final List<DomainEvent<?>> uncommittedChanges, final List<DomainEvent<?>> unseenEvents) {
        return false;
    }

    /**
     * Returns the number of tries to resolve a version conflict. May be overridden. Returns {@code 3} as default.
     *
     * @return Number of tries.
     */
    protected int getMaxTryCount() {
        return 3;
    }

    /**
     * Returns the aggregate cache. May be overridden. Returns no cache as default.
     *
     * @return Cache.
     */
    @NotNull
    protected AggregateCache<AGGREGATE> getAggregateCache() {
        return noCache;
    }

    /**
     * Returns the number of events to read in a slice. May be overridden. Returns {@code 100} as default.
     *
     * @return Page size.
     */
    public int getReadPageSize() {
        return 100;
    }

    /**
     * Returns the underlying asynchronous event store.
     *
     * @return Event store implementation.
     */
    @NotNull
    protected final EventStoreAsync getEventStore() {
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
