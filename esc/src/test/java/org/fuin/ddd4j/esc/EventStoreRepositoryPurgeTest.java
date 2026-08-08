package org.fuin.ddd4j.esc;

import org.fuin.ddd4j.jsonbtestmodel.Vendor;
import org.fuin.ddd4j.jsonbtestmodel.VendorId;
import org.fuin.ddd4j.jsonbtestmodel.VendorKey;
import org.fuin.ddd4j.jsonbtestmodel.VendorName;
import org.fuin.esc.api.CommonEvent;
import org.fuin.esc.api.EventStoreAsync;
import org.fuin.esc.api.ExpectedVersion;
import org.fuin.esc.api.StreamEventsSlice;
import org.fuin.esc.api.StreamId;
import org.fuin.esc.api.StreamState;
import org.fuin.esc.mem.InMemoryEventStoreAsync;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests that "purge" removes an aggregate irrevocably while "delete" does not.
 * <p>
 * The difference is a single flag reaching the event store, but it decides whether the aggregate identifier can ever
 * be used again - so it is worth pinning down rather than trusting by inspection.
 */
public class EventStoreRepositoryPurgeTest {

    private InMemoryEventStoreAsync delegate;

    private RecordingEventStore eventStore;

    private VendorRepositoryAsync repo;

    @BeforeEach
    public void setup() {
        delegate = new InMemoryEventStoreAsync(Executors.newCachedThreadPool());
        delegate.open();
        eventStore = new RecordingEventStore(delegate);
        repo = new VendorRepositoryAsync(eventStore);
    }

    @AfterEach
    public void teardown() {
        delegate.close();
    }

    @Test
    public void testDeleteIsASoftDelete() throws Exception {

        // PREPARE
        final VendorId id = new VendorId(UUID.randomUUID());
        repo.update(newVendor(id)).get();

        // TEST
        repo.delete(id, null).get();

        // VERIFY
        assertThat(eventStore.deletedStream()).isNotNull();
        assertThat(eventStore.hardDelete()).isFalse();

    }

    /**
     * The point of the new operation: erasure, not a tidier delete. A tombstoned stream rejects every later append,
     * so the aggregate identifier is burned for good.
     */
    @Test
    public void testPurgeTombstonesTheStream() throws Exception {

        // PREPARE
        final VendorId id = new VendorId(UUID.randomUUID());
        repo.update(newVendor(id)).get();

        // TEST
        repo.purge(id, null).get();

        // VERIFY
        assertThat(eventStore.deletedStream()).isNotNull();
        assertThat(eventStore.hardDelete()).isTrue();

    }

    @Test
    public void testPurgeWithAnExpectedVersionAlsoTombstones() throws Exception {

        // PREPARE
        final VendorId id = new VendorId(UUID.randomUUID());
        final Vendor vendor = newVendor(id);
        repo.update(vendor).get();

        // TEST
        repo.purge(id, vendor.getVersion()).get();

        // VERIFY
        assertThat(eventStore.hardDelete()).isTrue();

    }

    /**
     * Soft delete first, erase later is the realistic order - take out of service, then remove for good.
     */
    @Test
    public void testPurgingAnAlreadyDeletedAggregateSucceeds() throws Exception {

        // PREPARE
        final VendorId id = new VendorId(UUID.randomUUID());
        repo.update(newVendor(id)).get();
        repo.delete(id, null).get();

        // TEST & VERIFY - the caller wants it gone and it is gone, so this is not an error
        repo.purge(id, null).get();

    }

    private static Vendor newVendor(final VendorId vendorId) throws Exception {
        return new Vendor(vendorId, new VendorKey("V00001"), new VendorName("Hazards International Inc."), key -> {
            // Do nothing
        });
    }

    /**
     * Forwards everything and remembers how the last stream deletion was asked for.
     */
    private static final class RecordingEventStore implements EventStoreAsync {

        private final EventStoreAsync delegate;

        private final AtomicBoolean hardDelete = new AtomicBoolean();

        private final AtomicReference<StreamId> deletedStream = new AtomicReference<>();

        private RecordingEventStore(final EventStoreAsync delegate) {
            this.delegate = delegate;
        }

        boolean hardDelete() {
            return hardDelete.get();
        }

        StreamId deletedStream() {
            return deletedStream.get();
        }

        @Override
        public CompletableFuture<Void> deleteStream(final StreamId streamId, final boolean hard) {
            hardDelete.set(hard);
            deletedStream.set(streamId);
            return delegate.deleteStream(streamId, hard);
        }

        @Override
        public CompletableFuture<Void> deleteStream(final StreamId streamId, final long expectedVersion,
                                                    final boolean hard) {
            hardDelete.set(hard);
            deletedStream.set(streamId);
            return delegate.deleteStream(streamId, expectedVersion, hard);
        }

        // Everything below is plain forwarding.

        @Override
        public CompletableFuture<Void> open() {
            return delegate.open();
        }

        @Override
        public void close() {
            delegate.close();
        }

        @Override
        public boolean isSupportsCreateStream() {
            return delegate.isSupportsCreateStream();
        }

        @Override
        public CompletableFuture<Void> createStream(final StreamId streamId) {
            return delegate.createStream(streamId);
        }

        @Override
        public CompletableFuture<Long> appendToStream(final StreamId streamId, final long expectedVersion,
                                                      final List<CommonEvent> events) {
            return delegate.appendToStream(streamId, expectedVersion, events);
        }

        @Override
        public CompletableFuture<Long> appendToStream(final StreamId streamId, final long expectedVersion,
                                                      final CommonEvent... events) {
            return delegate.appendToStream(streamId, expectedVersion, List.of(events));
        }

        @Override
        public CompletableFuture<Long> appendToStream(final StreamId streamId, final CommonEvent... events) {
            return delegate.appendToStream(streamId, ExpectedVersion.ANY.getNo(), List.of(events));
        }

        @Override
        public CompletableFuture<Long> appendToStream(final StreamId streamId, final List<CommonEvent> events) {
            return delegate.appendToStream(streamId, ExpectedVersion.ANY.getNo(), events);
        }

        @Override
        public CompletableFuture<StreamEventsSlice> readEventsForward(final StreamId streamId, final long start,
                                                                      final int count) {
            return delegate.readEventsForward(streamId, start, count);
        }

        @Override
        public CompletableFuture<StreamEventsSlice> readEventsBackward(final StreamId streamId, final long start,
                                                                       final int count) {
            return delegate.readEventsBackward(streamId, start, count);
        }

        @Override
        public CompletableFuture<CommonEvent> readEvent(final StreamId streamId, final long eventNumber) {
            return delegate.readEvent(streamId, eventNumber);
        }

        @Override
        public CompletableFuture<Boolean> streamExists(final StreamId streamId) {
            return delegate.streamExists(streamId);
        }

        @Override
        public CompletableFuture<StreamState> streamState(final StreamId streamId) {
            return delegate.streamState(streamId);
        }

    }

}
