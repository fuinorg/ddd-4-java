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
package org.fuin.ddd4j.core;

import org.fuin.objects4j.common.ThreadSafe;

/**
 * Marker implemented by a domain event that records that a subject's private data was
 * <b>crypto-shredded</b> - the encryption key for that subject was destroyed, so the personal fields in the
 * subject's stored events can no longer be decrypted and are redacted on read (see
 * {@link RequiresPartialEncryption} / {@link RequiresPartialDecryption}).
 * <p>
 * Crypto-shredding only makes the <em>source</em> events unreadable; it does not reach into the
 * <b>derived state</b> that downstream consumers already persisted (read-model rows, search indexes, caches,
 * process-manager state) while the key still existed. Deleting the aggregate stream does not help either: a
 * stream delete is a control-plane operation, not an ordered event delivered over a catch-up subscription, so
 * consumers never receive it. This marker is the in-band, ordered signal that closes that gap: append an event
 * implementing it after the key is forgotten, and every consumer sees it in stream order and purges the
 * subject's derived data:
 * <pre>
 * if (event instanceof RemovedPrivateData removed) {
 *     purgeDerivedData(removed.getSubjectId());
 * }
 * </pre>
 * The event itself must carry <b>no</b> personal data - it is a tombstone that is safe to retain indefinitely
 * and is itself the record that the erasure happened. Use it for the "keep the aggregate but strip the PII"
 * case (retaining the legally required non-personal history); use a full stream delete only when the whole
 * aggregate is to be removed.
 * <p>
 * All implementations are expected to be thread safe.
 */
@ThreadSafe
public interface RemovedPrivateData {

    /**
     * Returns the identifier of the subject (aggregate root) whose private data was removed. This is the same
     * identity that owned the destroyed encryption key, so downstream consumers can locate and purge the
     * derived data they hold for that subject.
     *
     * @return Subject / aggregate root identifier (never {@code null}).
     */
    AggregateRootId getSubjectId();

}
