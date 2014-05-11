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

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.fuin.objects4j.common.Immutable;

/**
 * Configuration data required to create a projection in the database.
 */
@Immutable
@XmlRootElement(name = "projection")
public class ProjectionConfig implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @XmlAttribute(name = "name")
    private String name;

    @XmlElement(name = "trigger-sql")
    private String triggerSql;

    @XmlElement(name = "insert-sql")
    private String insertSql;

    /**
     * Constructor for de-serialization.
     */
    protected ProjectionConfig() {
	super();
    }

    /**
     * Constructor with all mandatory data.
     * 
     * @param name
     *            Name of the projection.
     * @param triggerSql
     *            SQL used to create the trigger that populates the projection.
     * @param insertSql
     *            SQL used to initially populate the projection.
     */
    public ProjectionConfig(@NotNull final String name,
	    @NotNull final String triggerSql, @NotNull final String insertSql) {
	super();
	this.name = name;
	this.triggerSql = triggerSql;
	this.insertSql = insertSql;
    }

    /**
     * Returns the name of the projection.
     * 
     * @return Unique name.
     */
    public final String getName() {
	return name;
    }

    /**
     * Returns the SQL used to create the trigger that populates the projection.
     * 
     * @return Trigger SQL.
     */
    public final String getTriggerSql() {
	return triggerSql;
    }

    /**
     * Returns the SQL used to initially populate the projection.
     * 
     * @return Insert SQL.
     */
    public final String getInsertSql() {
	return insertSql;
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
	ProjectionConfig other = (ProjectionConfig) obj;
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
