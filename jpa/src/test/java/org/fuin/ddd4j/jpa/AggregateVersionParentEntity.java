/**
 * Copyright (C) 2013 Future Invent Informationsmanagement GmbH. All rights
 * reserved. <http://www.fuin.org/>
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
 * along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fuin.ddd4j.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.fuin.ddd4j.core.AggregateVersion;
import org.fuin.utils4j.TestOmitted;

@TestOmitted("Test class")
@Entity(name = "AGGREGATE_VERSION_PARENT")
public class AggregateVersionParentEntity {

    @Id
    @Column(name = "ID")
    private long id;

    @Convert(converter = AggregateVersionAttributeConverter.class)
    @Column(name = "VERSION", nullable = true)
    private AggregateVersion version;

    public AggregateVersionParentEntity() {
        super();
    }

    public AggregateVersionParentEntity(long id) {
        super();
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AggregateVersion getVersion() {
        return version;
    }

    public void setVersion(AggregateVersion amount) {
        this.version = amount;
    }

}
