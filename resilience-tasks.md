# Resilience Tasks — ddd-4-java

What is implemented, and what was deliberately left out, is described in [resilience.md](resilience.md).
Only open points are listed here.

**Nothing is scheduled.** The active work is in
[cqrs-4-java](https://github.com/fuinorg/cqrs-4-java/blob/develop/resilience-tasks.md).

---

## Next

Nothing.

---

## Nice to have

- **Fault-injection test against a real event store.** The connectivity retry is covered by unit tests with
  an event store that fails a controllable number of times. An integration test that cuts a real KurrentDB
  mid-save and mid-load would additionally prove the behaviour against the real gRPC client, in the way the
  event-store-commons and cqrs-4-java integration tests do.

- **Per-event degradation for key service outages.** A vault blip currently fails the whole aggregate load or
  save. Failing only the affected event — skip-and-mark rather than fail-fast — would let an aggregate load
  with one field missing instead of not at all. This needs a decision about what a partially loaded aggregate
  means to the domain before it can be implemented, which is why it is not simply a code change.
