# Resilience Tasks — ddd-4-java

Part of the [Resilience Roadmap](https://github.com/fuinorg/ddd-cqrs-4-java-example/blob/develop/resilience-roadmap.md). This is the **aggregate
repository** layer (`ddd-4-java-esc`) sitting between the domain and `event-store-commons`. It is
framework-neutral, so like esc it stays free of SmallRye FT / Resilience4j by default; its job is to
**separate connectivity retries from version-conflict retries** and to make the crypto/vault calls
fault-tolerant-ready.

Depends on **event-store-commons Phase 0** (the `EscConnectionException` typed transient exception).

Legend: `[ ]` todo · **S1** (event store) · **S5** (crypto/vault).

---

## Phase 2 — Repository connectivity resilience — **S1**

### R1. Split the retry budget: version-conflict vs connectivity
File: `esc/src/main/java/org/fuin/ddd4j/esc/EventStoreRepositoryAsync.java`
(`attempt` ~188–227, `resolveConflicts` ~250–273, `getMaxTryCount` ~465, `conflictsResolved` ~456).
- [ ] Today `attempt(...)` retries **only** `WrongExpectedVersionException` (optimistic concurrency);
      an `EscConnectionException` (once esc F2 lands) falls through and is returned raw with no retry.
      Add a **separate connectivity-retry path**: on `EscConnectionException`, retry with exponential
      backoff + jitter under an independent budget (do not consume the version-conflict `getMaxTryCount`).
- [ ] Add `getMaxConnectionRetries()` / backoff config (overridable, like `getMaxTryCount()`), defaulting
      conservatively. Keep the two budgets independent so a flapping connection can't be misread as a
      "conflict storm".
- [ ] Ensure append-retry idempotency: connectivity retry re-sends the same `expectedVersion`; a duplicate
      is rejected by the store as a version conflict → then handled by the existing conflict path. Add a
      test.

### R2. Read-path retry
File: same (`read` ~120–162, `readSlices` ~164–181, `mapReadFailure` ~329–338; `EventStoreRepository`
sync adapter).
- [ ] The load path (`readSlices` looping `eventStore.readEventsForward(...)`) has **no retry** — a
      transient read failure aborts the whole load. Wrap the per-slice read with connectivity retry+backoff
      (reads are idempotent, so this is safe). `mapReadFailure` must pass `EscConnectionException` through as
      transient (not convert it to `AggregateNotFoundException`).

### R3. Optional aggregate cache as read fallback
File: `getAggregateCache()` ~475 (+ `core` `AggregateCache`).
- [ ] Where a stale read is acceptable, allow a **Fallback** to the last cached aggregate snapshot when the
      store is unavailable (opt-in per repository; document the staleness semantics). Do not use for
      command writes.

---

## Phase 5 — Crypto / key-vault resilience — **S5**

### C1. Make encrypt/decrypt vault calls fault-tolerant-ready
File: `esc/.../EventStoreRepositoryAsync.java`
(`encryptIfRequired` ~373–382, `decryptIfRequired` ~384–393; field `encryptedDataService` ~88).
- [ ] `encryptIfRequired`/`decryptIfRequired` call `objects4j` `EncryptedDataService` (Vault/OpenBao
      behind it) for every `RequiresPartialEncryption`/`RequiresPartialDecryption` event. Today a vault
      outage surfacing as `IOException`/timeout is wrapped in a bare `RuntimeException` and **aborts the
      whole aggregate load/save**. Bound each key call with a **timeout**, and surface vault-connectivity
      failures as a typed transient exception so the caller (or the framework layer) can apply
      retry/CB/fallback.
- [ ] **Fallback granularity:** a vault blip should fail (or defer) the *single* affected event, not the
      entire batch — decide and implement the degrade behavior (skip-and-mark vs fail-fast) explicitly.
- [ ] Keep the crypto-shredding invariant: a genuinely *destroyed* key (business "key gone") must remain a
      permanent, non-retried failure (`EncryptionKeyVersionUnknownException`), distinct from a transient
      "vault unreachable".

### C2. Coordinate with esc-crypto
- [ ] Align with `event-store-commons` `EncryptingEventStore` (esc S5): decide whether the resilience policy
      lives at the store decorator (esc) or the repository (here). Recommendation: **classification/timeout
      in esc-crypto; retry/CB/fallback policy here or in the app layer.**

---

## Notes for the framework layers (no code here — cross-reference)
- The actual **Retry/CircuitBreaker/Bulkhead** *policy* for S1/S5 is applied by the callers:
  Quarkus command/query modules via SmallRye FT, Spring modules via Resilience4j
  (see [cqrs-4-java tasks](https://github.com/fuinorg/cqrs-4-java/blob/develop/resilience-tasks.md)). `ddd-4-java` provides the **mechanism**
  (connectivity-aware retry loop) and **classification** (typed transient exceptions) so those policies
  compose cleanly and don't double-retry.
- Do **not** add a fault-tolerance framework dependency to `ddd-4-java-core`/`-esc`. (Optional turnkey path:
  Resilience4j *core* programmatic decoration behind a builder flag, mirroring esc's optional section.)

---

## Testing
- [ ] Concurrency test: connectivity retries do not inflate the version-conflict count and vice versa
      (independent budgets, R1).
- [ ] Fault-injection: pause the eventstore mid-save/mid-load and assert connectivity retry+backoff then
      success on recovery; assert no duplicate append (R1/R2).
- [ ] Crypto: stub `EncryptedDataService` to time out / throw connectivity errors and assert the typed
      transient exception + chosen fallback granularity (C1); assert a destroyed-key error stays permanent.
