# Micrometer Tasks — ddd-4-java

Part of the cross-repo [Micrometer Instrumentation Roadmap](https://github.com/fuinorg/ddd-cqrs-4-java-example/blob/develop/micrometer-roadmap.md).
This is **upstream #2** — release after event-store-commons (it consumes the new `esc-bom`) and
before cqrs-4-java.

Micrometer stays **optional**: neutral modules (`core`, `esc`) see only the micrometer-free
`Ddd4jMetrics` facade (default `NOOP`); all `io.micrometer.*` code lives in the new opt-in
`micrometer` module.

## Phase 0 — Facade & module scaffolding

- [ ] Add micrometer-free facade `Ddd4jMetrics` (interface + `NOOP` singleton) in the `core`
      module, package `org.fuin.ddd4j.core`. Same surface as `EscMetrics`: `recordTime`,
      `increment`, `record`.
- [ ] Import `io.micrometer:micrometer-bom` in the parent
      [pom.xml](https://github.com/fuinorg/ddd-4-java/blob/develop/pom.xml)
      `<dependencyManagement>`. `core` and `esc` stay micrometer-free.
- [ ] Create new module `ddd-4-java/micrometer` (`ddd4j-micrometer`): depends on `micrometer-core`
      + `ddd-4-java-core` / `-esc`. Register it in the
      [bom/pom.xml](https://github.com/fuinorg/ddd-4-java/blob/develop/bom/pom.xml) and parent
      `<modules>`.
- [ ] Add an ArchUnit rule forbidding `io.micrometer` imports outside the `micrometer` module.

## Phase 1 — Decorator + aggregate seams (⭐ 20% set)

In the new `micrometer` module:

- [ ] ⭐ `MeteredRepository` — wrap `IEventStoreRepositoryAsync` / `Repository`
      ([Repository.java](https://github.com/fuinorg/ddd-4-java/blob/develop/core/src/main/java/org/fuin/ddd4j/core/Repository.java))
      → `ddd4j.aggregate.read|update|add|delete` Timer (tags `aggregate.type`, `outcome`).
- [ ] `MicrometerDdd4jMetrics implements Ddd4jMetrics` — adapter over a `MeterRegistry`.

In the neutral `esc` module (facade seams, default `Ddd4jMetrics.NOOP`):

- [ ] ⭐ [`EventStoreRepositoryAsync`](https://github.com/fuinorg/ddd-4-java/blob/develop/esc/src/main/java/org/fuin/ddd4j/esc/EventStoreRepositoryAsync.java):
      optional `Ddd4jMetrics` field:
  - `ddd4j.aggregate.cache` Counter (tag `result=hit|miss`) at the cache-lookup branches
    (~lines 123 / 137–143 — the hit/miss is already distinguished).
  - `ddd4j.aggregate.conflict` Counter on the `WrongExpectedVersionException` branch in
    `attempt(...)` (~line 215).
  - `ddd4j.aggregate.retries` DistributionSummary from `retryCount`.
  - `ddd4j.aggregate.add.exists` Counter in `add(...)` (~line 239).

## Phase 2 — Reflection dispatch (per-event replay hot path — gate carefully)

- [ ] [`MethodExecutor`](https://github.com/fuinorg/ddd-4-java/blob/develop/core/src/main/java/org/fuin/ddd4j/core/MethodExecutor.java):
      optional `Ddd4jMetrics`; `ddd4j.handler.lookup` Timer around `findDeclaredAnnotatedMethod`,
      `ddd4j.handler.missing` Counter.
  - **Pair with an optimization**: the reflective handler lookup is currently uncached and walks
    the class hierarchy on **every** event. Add a `ConcurrentHashMap<Class, Method>` cache; the
    timer proves the improvement.
- [ ] [`AbstractAggregateRoot`](https://github.com/fuinorg/ddd-4-java/blob/develop/core/src/main/java/org/fuin/ddd4j/core/AbstractAggregateRoot.java):
      `ddd4j.aggregate.events.applied` Counter (tag `event.type`) in `loadFromHistory` / `apply`.
      Per-event — prefer the aggregate-level `ddd4j.aggregate.read` timer as the default and keep
      this behind the opt-in module.

## Phase 3 — Serialization

- [ ] `MeteredObjectSerDeserializer` in the `micrometer` module — wrap
      [`ObjectSerDeserializer`](https://github.com/fuinorg/ddd-4-java/blob/develop/core/src/main/java/org/fuin/ddd4j/core/ObjectSerDeserializer.java)
      → `ddd4j.serde.serialize|deserialize` Timer (tag `contentType`, `operation`) +
      `ddd4j.serde.payload.bytes` DistributionSummary. Covers the jackson/jsonb/jaxb impls
      uniformly.

## Tests

- [ ] One `SimpleMeterRegistry` test per decorator/adapter (mirror the cqrs `*MetricsTest` pattern).
- [ ] Zero-impact test: neutral classes with `Ddd4jMetrics.NOOP` behave identically; ArchUnit
      confirms no `io.micrometer` leak into `core`/`esc`.
