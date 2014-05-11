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

import java.net.URL;
import java.util.Iterator;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.fuin.objects4j.common.Contract;
import org.fuin.utils4j.Utils4J;

/**
 * Configures the projections on startup that are defined in
 * META-INF/projections/.
 */
@ApplicationScoped
public class ProjectionConfigurator {

    @Resource
    private EntityManager em;

    /**
     * Default constructor used by CDI.
     */
    public ProjectionConfigurator() {
	super();
    }

    /**
     * Constructor with all mandatory data.
     * 
     * @param em
     *            Entity manager.
     */
    public ProjectionConfigurator(@NotNull final EntityManager em) {
	super();
	Contract.requireArgNotNull("em", em);
	this.em = em;
    }

    /**
     * Reads the configurations and executes the SQL.
     */
    @PostConstruct
    public void execute() {
	final URL url = this.getClass().getResource(
		"/META-INF/projections/projections.properties");
	if (url != null) {
	    final Properties props = Utils4J.loadProperties(url);
	    final Iterator<Object> it = props.keySet().iterator();
	    while (it.hasNext()) {
		final String key = (String) it.next();
		final String value = (String) props.get(key);
		execute(value);
	    }
	}
    }

    private void execute(final String configResourceName) {
	final ProjectionConfig config = readConfig(configResourceName);
	final Query triggerQuery = em.createNativeQuery(config.getTriggerSql());
	triggerQuery.executeUpdate();
	final Query insertQuery = em.createNativeQuery(config.getInsertSql());
	insertQuery.executeUpdate();
	final Projection projection = new Projection(config.getName());
	em.merge(projection);
    }

    private ProjectionConfig readConfig(final String configResourceName) {
	try {
	    final JAXBContext ctx = JAXBContext
		    .newInstance(ProjectionConfig.class);
	    final Unmarshaller unmarshaller = ctx.createUnmarshaller();
	    return (ProjectionConfig) unmarshaller.unmarshal(this.getClass()
		    .getResource(configResourceName));
	} catch (final JAXBException ex) {
	    throw new RuntimeException(
		    "Error reading projection configuration '"
			    + configResourceName + "'", ex);
	}
    }

}
