/**
 * Copyright (C) 2015 Michael Schnell. All rights reserved. http://www.fuin.org/
 * <p>
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License along with this library. If not, see
 * http://www.gnu.org/licenses/.
 */
@NullMarked
package org.fuin.ddd4j.esc;

import org.jspecify.annotations.NullMarked;

/**
 * Event store based repository implementation.
 * <p>
 * <b>Event schema versioning.</b> Version threading is owned by the event store's serialization layer, not by
 * the repository. When the store's {@code DeserializerRegistry} is wrapped with an
 * {@link org.fuin.esc.api.UpcastingDeserializerRegistry}, every stored event is lifted to its latest version
 * right after it is deserialized, so the {@link org.fuin.ddd4j.esc.EventStoreRepository} (and its asynchronous
 * counterpart) stay version-agnostic: a {@code …v1} event stream folds into a {@code …v2} aggregate on replay
 * without any repository-side branching. Applications declare their aggregate-event up-casters as
 * {@link org.fuin.ddd4j.esc.ConverterRegistration} instances and fold them into a
 * {@link org.fuin.esc.api.ConverterRegistry} via
 * {@link org.fuin.ddd4j.esc.ConverterRegistration#toRegistry(java.util.Collection)}, which is then used to
 * build the {@link org.fuin.esc.api.UpcastingDeserializerRegistry} passed to the event store.
 * <p>
 * The governing rule: <em>a new version of an event must be convertible from the old one; if it is not, it is
 * a new event type, not a new version.</em>
 */
