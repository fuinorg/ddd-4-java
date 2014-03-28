/**
 * Copyright (C) 2013 Future Invent Informationsmanagement GmbH. All rights
 * reserved. <http://www.fuin.org/>
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fuin.ddd4j.ddd;

/**
 * An object that is not defined by its attributes, but rather by a thread of
 * continuity and its identity.
 * 
 * @param <ID>
 *            Entity ID type.
 */
public interface Entity<ID extends EntityId> {

	/**
	 * Returns the unique type.
	 * 
	 * @return Type of the entity that is unique in the context.
	 */
	public EntityType getType();

	/**
	 * Returns the unique entity identifier.
	 * 
	 * @return Identifier.
	 */
	public ID getId();

}
