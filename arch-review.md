# ddd-4-java — Architecture Review (Event Sourcing focus)

**Scope:** event sourcing and DDD building blocks in `ddd-4-java` (`org.fuin.ddd4j`, v0.8.0-SNAPSHOT).
**Method:** static review of all modules (`core`, `esc`, `jackson`, `jaxb`, `jsonb`, `jpa`, `junit`, test models) and their ArchUnit rules.
**Companion:** see [cqrs-4-java/arch-review.md](https://github.com/fuinorg/cqrs-4-java/tree/develop/arch-review.md) (CQRS/projections/process-managers on top of this) 
and [event-store-commons/arch-review.md](https://github.com/fuinorg/event-store-commons/blob/develop/arch-review.md) (the `esc` event-store abstraction this delegates to). 
Cross-cutting items — chiefly event versioning — are intentionally shared across the three, with the **root cause/fix placed at the lowest layer that owns it** (usually `esc`).

---

## 1. What this project is

`ddd-4-java` is the **write-side event-sourcing engine**: DDD tactical building blocks (aggregates, entities, value-object identifiers, domain events) plus an event-store-backed repository. It does **not** implement storage itself — it adapts the external **esc** (Event Store Commons, `org.fuin.esc:esc-api`) abstraction, which can be backed in-memory (`esc-mem`) or by EventStoreDB (`esgrpc`).

```
core   → DDD/ES model: AggregateRoot, Entity, DomainEvent, identifiers, exceptions, @ApplyEvent
esc    → EventStoreRepository(Async): load = replay, save = append-with-expected-version, retries, encryption
jackson/jsonb/jaxb → three pluggable event (de)serialization stacks
jpa    → persistence annotations
junit  → ArchUnit rules + test fixtures
```

---

## 2. Event-sourcing design as built

- **Event model** (`core/.../Event`, `DomainEvent<ID>`): every event carries `EventId`, `EventType` (string name, ≤255), `ZonedDateTime`, and nullable **`correlationId`/`causationId`**; domain events add `EntityIdPath`, `entityId`, and `aggregateVersion`. Good metadata foundation for message-graph reconstruction.
- **Aggregate** (`AbstractAggregateRoot`): `version` starts at `-1`; `apply(event)` finds an `@ApplyEvent` method by event type (reflection, cached via `MethodExecutor`) and buffers into `uncommittedChanges`; `loadFromHistory(...)` replays and increments version; child entities are reached through `EntityIdPath` + `@ChildEntityLocator`. `getNextVersion() = version + uncommitted.size()`.
- **Repository** (`esc/.../EventStoreRepositoryAsync` + sync facade): load = read stream in pages (`getReadPageSize()`) → `loadFromHistory`; save = uncommitted → `CommonEvent` → `appendToStream(expectedVersion, …)`; **optimistic concurrency** via `WrongExpectedVersionException` → `resolveConflicts()` → `conflictsResolved(uncommitted, unseen)` hook → bounded retry (`getMaxTryCount()`). Stream id = `AggregateStreamId(type, paramName, id)` → `"Type-<uuid>"`. Async-first (`CompletableFuture`) with a thread-blocking sync wrapper.
- **Streams = aggregate instances.** One stream per aggregate; no built-in cross-aggregate streams.
- **Encryption** (`RequiresPartialEncryption`/`RequiresPartialDecryption` + `ObjectSerDeserializer` + `EncryptedDataService`): per-field encryption transparently applied on append / reversed on read. A genuine differentiator.
- **Multi-tenancy:** `TenantId` (record), `TenantContext` (thread-local), `SimpleTenantId` scoping at the store.

### 2a. Baseline already provided by `esc` (don't re-invent)
Several capabilities live in `org.fuin.esc:esc-api` (0.10.0) that both `ddd-4-java` and `cqrs-4-java` build on — this review only flags what is *missing on top of* that baseline:
- **Versioned serialization substrate:** `CommonEvent` carries a `SerializedDataType` + `EnhancedMimeType`, and `EnhancedMimeType` has explicit **`version`** and **`encoding`** parameters. `Deserializer.unmarshal(data, type, mimeType)` receives the version, and `DeserializerRegistry.getDeserializer(type, mimeType)` is **keyed by `(type, version)`** — so a distinct deserializer can be registered per event version. `SerializedDataTypeRegistry` maps `SerializedDataType → Class`. A generic `Converter<S,T>` interface also exists.
- **Subscriptions / catch-up:** `SubscribableEventStore(Async)` + `Subscription` (`InMemorySubscription` in `esc-mem`).
- **Projection admin & server-side projections:** `ProjectionAdminEventStore`, `ProjectionJavaScriptBuilder`.
- **Serializer/deserializer registries** with builders, per format (`esc-jackson`/`-jsonb`/`-jaxb`).

---

## 3. Strengths

1. **Clean, well-bounded model.** ArchUnit enforces no upward deps, a tight allow-list per module, and a thread-safety annotation on every class. The model is small, immutable, and value-object-centric.
2. **Correct ES core.** Replay-to-rebuild, append-with-expected-version, and an explicit **conflict-resolution hook** (most frameworks omit this) are all present and tested round-trip against `esc-mem`.
3. **Pluggable serialization** (Jackson/JSON-B/JAX-B) behind `ObjectSerDeserializer`, with a `SerializedDataType` ⇄ `EventType` convention.
4. **Encryption / crypto-shredding primitives** already exist — rare and valuable for GDPR-style "delete by forgetting the key".
5. **Sync + async** repositories from one implementation.
6. **Compile-time safety:** JSpecify + NullAway + Error Prone.

---

## 4. Gaps & risks (event-sourcing lens)

> Ordered roughly by impact on real ES deployments.

### 4.1 Event versioning: substrate exists in `esc`, but the **upcaster chain is not wired**
*Corrected after re-checking `esc`.* The serialization **substrate is already there** (see §2a): `EnhancedMimeType` carries a `version`, and `DeserializerRegistry.getDeserializer(type, mimeType)` is keyed by `(type, version)` — so you **can register a distinct deserializer per event version**. What is missing on top of that baseline:
- **No automatic upcasting pipeline.** `esc` exposes a generic `Converter<S,T>` interface, but it is **unused** — there is no `ConverterRegistry` and nothing in the read path chains `v1→v2→…→vN` so that aggregates/projections see a single *current* version. Today each version deserializes to its own representation and the application must branch. (For HTTP/integration negotiation you'd also want the reverse `vN→v1` direction.)
- **`ddd-4-java` does not exploit the versioned mime type.** `EventStoreRepositoryAsync` builds the stored type from `new TypeName(event.getEventType().asBaseType())` only — there is no version dimension threaded through, so the per-version deserializer capability isn't actually used by aggregate replay.
- **Defensive tolerance is off by default.** The Jackson event base classes (`AbstractEvent`, `AbstractDomainEvent`) **lack** `@JsonIgnoreProperties(ignoreUnknown = true)` (only exception DTOs have it), so additive fields can break older readers unless a global mapper feature is set. Minor next to the upcaster gap, but a cheap additive-safety win.

### 4.2 Partial replay exists (`AggregateCache`); a *durable* snapshot does not
*Refined after re-checking the read path.* Loads are **not** hard-wired to replay from 0. Both `read(id)` and `read(id, version)` consult `AggregateCache.get(...)`, and on a hit `readSlices()` replays only the **tail** from `cachedVersion + 1`, then re-caches (`EventStoreRepositoryAsync` ll. 121–162). So a snapshot-like partial-replay seam already exists. The real gaps are:
- The only **shipped** impl is `AggregateNoCache` (a no-op) → out of the box there is no caching and loads do replay from 0.
- The abstraction is **in-memory / whole-object memoization** (keyed by id+tenant), so any impl is **cold after restart/redeploy** — the *first* load of an aggregate still replays the full stream. A *durable* snapshot store would replay only the tail even on a cold first load. (`update()` also doesn't refresh the cache, and entries are mutable aggregate instances — invalidation/sharing need care.)
- `esc` has no `Snapshot*` type either.

Net: fine for hot aggregates in steady state; the gap is a **durable** snapshot for cold starts / long-tail aggregates / very long streams — lower severity than first stated.

### 4.3 Reflection-based dispatch on the hot path
`@ApplyEvent` / `@ChildEntityLocator` resolve by reflection (cached). Acceptable, but on high-throughput replay it is measurable, and it pushes "did you wire the handler?" errors to runtime.

### 4.4 Correlation/causation are carried but not orchestrated
The fields exist on every event, but there is no helper/context that **automatically copies `correlationId` and sets `causationId = triggering message id`** when an aggregate reacts to an inbound command/event. Without that, the causal graph the metadata is designed for is only as good as each call site's discipline.

### 4.5 Three serialization stacks = triple maintenance
Jackson, JSON-B and JAX-B each re-implement `AbstractEvent`/`AbstractDomainEvent` and adapters. Flexible, but every model/versioning change must be made (and tested) three times.

### 4.6 Deletion / retention primitives are thin
`Repository.delete(id, expectedVersion)` deletes a stream, but there is no first-class support for **public/private stream split** or **crypto-shred + retention** as a pattern, even though the encryption building blocks are present.

---

## 5. Recommendations — where to evolve, and how

### P1 — Complete the event-versioning story (build on esc, don't duplicate it)
The substrate (`EnhancedMimeType.version`, version-keyed `DeserializerRegistry`, `Converter`) already lives in `esc`. The best home for the missing **upcaster chain is `esc` itself** (see `event-store-commons/arch-review.md` §P1); `ddd-4-java` should then *use* it. Concretely:
- **(a) Thread the version through writes/reads.** Have `EventStoreRepositoryAsync` stamp the event's schema version into the `EnhancedMimeType` on append, and rely on the `(type, version)`-keyed deserializer on read — so the capability esc already offers is actually exercised.
- **(b) Apply an upcaster chain after deserialization.** Once esc provides a `ConverterRegistry`/upcaster chain (or as an interim `ddd-4-java`-local registry), compose `v1→…→vN` so aggregate replay and projections see one current version. Support skip-converters and reverse (`vN→v1`) for HTTP negotiation.
- **(c) Make events additive-safe now (cheap, independent).** Add `@JsonIgnoreProperties(ignoreUnknown = true)` to `AbstractEvent`/`AbstractDomainEvent` (+ JSON-B equivalent) or set it centrally in `Ddd4JacksonModule`. Backward-compatible, no API change — ship immediately.
- Document the rule: *a "new version" must be convertible from the old; if it isn't, it's a new event, not a new version.*

### P2 — Optional, pluggable snapshots
Define `SnapshotStore<ID, AGG>` + a `Snapshotable` aggregate hook (serialize/restore state at version N). On load: restore latest snapshot ≤ target, replay the tail. Keep it **opt-in** and **rebuild-not-upgrade** on snapshot schema change (delete + replay), and keep old snapshots briefly to allow software downgrade. Trigger by event-count threshold. (Reuse the existing `AggregateCache` seam.)

### P3 — Correlation/causation propagation helper
Add a small `MessageContext` (current correlation id + causation id) that the repository/aggregate uses to auto-stamp produced events (`copy correlationId`, `causationId = inbound message id`). This turns the existing metadata into a usable, queryable causal graph with zero per-call-site effort.

### P4 — First-class privacy/retention pattern
Promote the encryption primitives into a documented **crypto-shredding** recipe and an optional **public/private stream split** helper (`User-x-public` / `User-x-private`), with a `RemovedPrivateData` marker event so downstream consumers also purge. This is directly enabled by what already exists and is a strong compliance selling point.

### P5 — Reduce serialization triplication
Extract the shared event-field contract once and generate/share the three adapter sets, or designate Jackson as primary and treat JSON-B/JAX-B as thin secondaries. Lowers the cost of every future model/versioning change.

### P6 — Optional non-reflective dispatch
Offer a registration-based (or annotation-processor-generated) event-apply dispatch as an alternative to reflection for hot-path/native-image scenarios; keep `@ApplyEvent` as the default.

---

## 6. Roadmap snapshot

| Priority | Item | Effort | Risk if skipped |
|---|---|---|---|
| **P1** | Use esc's versioned mime type + apply upcaster chain (esc-owned); events additive-safe | M | High — can't evolve events without lock-step deploys |
| **P2** | Pluggable durable snapshots | M | Medium — load latency on long streams |
| **P3** | Correlation/causation auto-propagation | S | Medium — causal graph stays manual/unreliable |
| **P4** | Crypto-shredding + public/private streams as a pattern | S–M | Medium — GDPR/retention left to apps |
| **P5** | De-duplicate the 3 serialization stacks | M | Low — maintenance drag |
| **P6** | Non-reflective dispatch option | S–M | Low — throughput/native-image |

**Already strong, keep as-is:** the aggregate/repository core, optimistic-concurrency + conflict-resolution hook, encryption primitives, multi-tenancy, ArchUnit governance, sync+async duality.

---
*Generated as an architectural review; no code was modified. File paths reference `org.fuin.ddd4j` modules under this repository.*
