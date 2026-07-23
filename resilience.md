# Resilience — what is implemented

How the aggregate repository behaves when the event store or the key service cannot be reached.

`ddd-4-java-esc` sits between the domain and `event-store-commons`. Like esc it is framework-neutral and
carries no fault-tolerance framework. Its job is to **keep two different kinds of failure apart** — somebody
else wrote first, versus nobody could be asked — and to make both classifiable by the layers above.

## Two independent retry budgets

A version conflict and a connectivity failure look nothing alike, so they are counted separately.

| | What it means | Budget | Configure with |
|---|---|---|---|
| Version conflict | Somebody else wrote first; the aggregate has to be rebuilt and the change re-applied | 3 tries | `getMaxTryCount()` |
| Store unreachable | Nobody could be asked at all | 3 repeats | `getMaxConnectionRetries()`, `getConnectionRetryBackoff()` |

Sharing one counter would let a flapping connection exhaust the budget a genuine conflict storm needs, and
the logs would read as a conflict storm when the real problem is the network.

The connectivity delay is short and jittered by default — 50 ms doubling to 1 s. This sits on the command
path with a caller waiting, so it is meant to ride out a blip, not to wait out an outage; the jitter keeps
concurrent commands that hit the same outage from retrying in lockstep. Set `getMaxConnectionRetries()` to
`0` to switch connectivity retries off entirely.

Only `EscConnectionException` is repeated. A business answer — a version conflict, a missing stream, a
deleted stream — is returned immediately, because retrying cannot change it.

## Which calls are repeated

**Reads** (`read`, `readEvents`, and the slice loops behind them) are repeated on a connectivity failure.
Reads are idempotent, so this is always safe, and without it a single blip halfway through loading a large
aggregate aborts the whole load.

**Appends** are repeated too, and this is safe for one specific reason: the retry re-sends the **same
expected version**. An append that did get through is therefore rejected by the store as a version conflict
rather than applied a second time, and that conflict is then handled by the normal conflict-resolution path.
A retry that recomputed the expected version would duplicate events.

## Encryption failures

Events implementing `RequiresPartialEncryption` / `RequiresPartialDecryption` call an external key service on
every save and load. Two very different things can go wrong there, and they must never be confused:

- **The key service cannot be reached** — reported as `EncryptionServiceConnectionException`, which extends
  `EscConnectionException`. Transient: the same event encrypts or decrypts fine once the vault is back, so a
  caller may retry.
- **The key is gone** — crypto-shredding. The field is **redacted** rather than reported as a failure at all,
  so the aggregate still loads with the private data removed. Nothing here can be mistaken for a transient
  problem, and no retry policy will ever be triggered for data that can never come back.

Classification walks the cause chain for an `IOException` or a `TimeoutException`, which is what a vault
client reports when the service is unreachable, refused the connection or timed out. A client that classifies
its own failures can throw an `EscConnectionException` itself; those pass through untouched.

# What is deliberately absent

**No fault-tolerance framework.** This module provides the *mechanism* — a connectivity-aware retry loop —
and the *classification*. Circuit breakers, bulkheads and rate limits are policy, and belong to the framework
integration layers and the applications, which know what an acceptable wait and failure rate are. Putting a
breaker here as well would double-retry against the policy the caller already applies.

**No aggregate-cache fallback.** Serving the last cached aggregate when the store is unavailable was
considered and rejected. This repository is used mainly for command handling, where acting on a stale
aggregate is how lost writes and wrong decisions happen. That leaves a narrow read-only niche, which an
application can serve with its own cache and its own explicit staleness contract.

**No timeout around the key service calls.** The key service client's own connect and read timeout is the
right place for it: a timeout applied here could not abort the call anyway, only stop waiting for it.
