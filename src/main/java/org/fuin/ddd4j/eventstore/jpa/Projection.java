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
package org.fuin.ddd4j.eventstore.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.fuin.objects4j.common.Contract;

/**
 * Projection.
 */
@Table(name = "PROJECTIONS")
@Entity
public class Projection {

    @Id
    @NotNull
    @Column(name = "NAME", nullable = false, updatable = false, length = 250)
    private String name;
    
    /**
     * Protected default constructor for JPA.
     */
    protected Projection() {
	super();
    }

    /**
     * Constructor with all mandatory data.
     * 
     * @param name
     *            Unique name for the projection.
     */
    public Projection(@NotNull final String name) {
	super();
	Contract.requireArgNotNull("name", name);
	this.name = name;
    }
    

    // CHECKSTYLE:OFF Generated code
    @Override
    public final int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	return result;
    }

    @Override
    public final boolean equals(final Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Projection other = (Projection) obj;
	if (name == null) {
	    if (other.name != null)
		return false;
	} else if (!name.equals(other.name))
	    return false;
	return true;
    }
    // CHECKSTYLE:ON

    @Override
    public final String toString() {
        return name;
    }
    
}
