package org.fuin.ddd4j.esc;

import org.fuin.ddd4j.jsonbtestmodel.Vendor;
import org.fuin.ddd4j.jsonbtestmodel.VendorId;
import org.fuin.ddd4j.jsonbtestmodel.VendorKey;
import org.fuin.ddd4j.jsonbtestmodel.VendorName;
import org.fuin.esc.api.CommonEvent;
import org.fuin.esc.api.EscConnectionException;
import org.fuin.esc.api.EventStoreAsync;
import org.fuin.esc.api.ExpectedVersion;
import org.fuin.esc.api.StreamEventsSlice;
import org.fuin.esc.api.StreamId;
import org.fuin.esc.api.StreamState;
import org.fuin.esc.api.WrongExpectedVersionException;
import org.fuin.esc.mem.InMemoryEventStoreAsync;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests that the repository rides out a store that cannot be reached, and that the connectivity budget is
 * kept apart from the version-conflict budget.
 */
public class EventStoreRepositoryConnectivityTest {

    private InMemoryEventStoreAsync delegate;

    private FlakyEventStore eventStore;

    private VendorRepositoryAsync repo;

    @BeforeEach
    public void setup() {
        delegate = new InMemoryEventStoreAsync(Executors.newCachedThreadPool());
        delegate.open();
        eventStore = new FlakyEventStore(delegate);
        repo = new VendorRepositoryAsync(eventStore);
    }

    @AfterEach
    public void teardown() {
        delegate.close();
    }

    private static Vendor newVendor(final VendorId vendorId) throws Exception {
        return new Vendor(vendorId, new VendorKey("V00001"), new VendorName("Hazards International Inc."), key -> {
            // Do nothing
        });
    }

    @Test
    public void testAppendRidesOutATransientFailure() throws Exception {

        // PREPARE: the store is unreachable for the first two attempts
        final VendorId vendorId = new VendorId();
        final Vendor vendor = newVendor(vendorId);
        eventStore.failAppends(2);

        // TEST
        repo.update(vendor).get();

        // VERIFY: the aggregate was saved, and exactly once - a repeated append re-sends the same expected
        // version, so a duplicate could only appear if the retry changed it
        assertThat(eventStore.appendAttempts()).isEqualTo(3);
        assertThat(delegate.readEventsForward(streamId(vendorId), 0, 100).get().getEvents()).hasSize(1);
    }

    @Test
    public void testReadRidesOutATransientFailure() throws Exception {

        // PREPARE
        final VendorId vendorId = new VendorId();
        repo.update(newVendor(vendorId)).get();
        eventStore.failReads(2);

        // TEST & VERIFY: reads are idempotent, so repeating them is always safe
        assertThat(repo.read(vendorId).get().getId()).isEqualTo(vendorId);
        assertThat(eventStore.readAttempts()).isEqualTo(3);
    }

    @Test
    public void testGivesUpOnceTheConnectivityBudgetIsUsed() throws Exception {

        // PREPARE: the store never comes back
        final VendorId vendorId = new VendorId();
        final Vendor vendor = newVendor(vendorId);
        eventStore.failAppends(100);

        // TEST & VERIFY: the typed transient failure reaches the caller rather than being retried forever
        assertThatThrownBy(() -> repo.update(vendor).get())
                .isInstanceOf(ExecutionException.class)
                .hasCauseInstanceOf(EscConnectionException.class);

        // 1 initial attempt plus the 3 repeats the default budget allows
        assertThat(eventStore.appendAttempts()).isEqualTo(4);
    }

    @Test
    public void testABusinessAnswerIsNotRetried() throws Exception {

        // PREPARE: a version conflict is an answer, not a connectivity problem
        final VendorId vendorId = new VendorId();
        repo.update(newVendor(vendorId)).get();
        eventStore.failAppendsWith(new WrongExpectedVersionException(streamId(vendorId), 99L, 0L));

        // TEST
        final Vendor other = newVendor(vendorId);
        assertThatThrownBy(() -> repo.update(other).get()).isInstanceOf(ExecutionException.class);

        // VERIFY: the connectivity retry did not fire - a conflict is handled by the conflict path, which
        // has its own budget, so the two must not consume each other
        assertThat(eventStore.appendAttempts()).isLessThanOrEqualTo(2);
    }

    private static AggregateStreamId streamId(final VendorId vendorId) {
        return new AggregateStreamId(VendorId.TYPE, "vendorId", vendorId);
    }

    /**
     * Event store that can be made to fail a given number of times, so a transient outage can be observed
     * without a container.
     */
    private static final class FlakyEventStore implements EventStoreAsync {

        private final EventStoreAsync delegate;

        private final AtomicInteger appendAttempts = new AtomicInteger();

        private final AtomicInteger readAttempts = new AtomicInteger();

        private final AtomicInteger appendFailures = new AtomicInteger();

        private final AtomicInteger readFailures = new AtomicInteger();

        private RuntimeException appendFailure;

        private FlakyEventStore(final EventStoreAsync delegate) {
            this.delegate = delegate;
        }

        void failAppends(final int count) {
            appendFailures.set(count);
            appendFailure = null;
        }

        void failAppendsWith(final RuntimeException failure) {
            appendFailures.set(Integer.MAX_VALUE);
            appendFailure = failure;
        }

        void failReads(final int count) {
            readFailures.set(count);
        }

        int appendAttempts() {
            return appendAttempts.get();
        }

        int readAttempts() {
            return readAttempts.get();
        }

        @Override
        public CompletableFuture<Long> appendToStream(final StreamId streamId, final long expectedVersion,
                                                      final List<CommonEvent> events) {
            appendAttempts.incrementAndGet();
            if (appendFailures.getAndUpdate(n -> n > 0 ? n - 1 : 0) > 0) {
                return CompletableFuture.failedFuture(appendFailure != null ? appendFailure
                        : new EscConnectionException("Could not reach the store", null));
            }
            return delegate.appendToStream(streamId, expectedVersion, events);
        }

        @Override
        public CompletableFuture<StreamEventsSlice> readEventsForward(final StreamId streamId, final long start,
                                                                      final int count) {
            readAttempts.incrementAndGet();
            if (readFailures.getAndUpdate(n -> n > 0 ? n - 1 : 0) > 0) {
                return CompletableFuture.failedFuture(new EscConnectionException("Could not reach the store", null));
            }
            return delegate.readEventsForward(streamId, start, count);
        }

        // Everything below is plain forwarding - only append and read forward are made to fail.

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
                                                      final CommonEvent... events) {
            return appendToStream(streamId, expectedVersion, List.of(events));
        }

        @Override
        public CompletableFuture<Long> appendToStream(final StreamId streamId, final CommonEvent... events) {
            return appendToStream(streamId, ExpectedVersion.ANY.getNo(), List.of(events));
        }

        @Override
        public CompletableFuture<Long> appendToStream(final StreamId streamId, final List<CommonEvent> events) {
            return appendToStream(streamId, ExpectedVersion.ANY.getNo(), events);
        }

        @Override
        public CompletableFuture<Void> deleteStream(final StreamId streamId, final long expectedVersion,
                                                    final boolean hardDelete) {
            return delegate.deleteStream(streamId, expectedVersion, hardDelete);
        }

        @Override
        public CompletableFuture<Void> deleteStream(final StreamId streamId, final boolean hardDelete) {
            return delegate.deleteStream(streamId, hardDelete);
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
