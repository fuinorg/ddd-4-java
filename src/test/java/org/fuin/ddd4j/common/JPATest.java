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
package org.fuin.ddd4j.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * Base class for unit tests with JPA entity manager.
 */
// CHECKSTYLE:OFF
public abstract class JPATest extends BaseTest {

	private static EntityManagerFactory emf;

	private static EntityManager em;

	private static Connection connection;

	@BeforeClass
	public static void beforeClass() throws Exception {
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			connection = DriverManager.getConnection(
					"jdbc:hsqldb:mem:unit-testing-jpa", "sa", "");
			emf = Persistence.createEntityManagerFactory("testPU");
			em = emf.createEntityManager();
		} catch (final SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	@AfterClass
	public static void afterClass() {
		if (em != null) {
			em.close();
		}
		if (emf != null) {
			emf.close();
		}
		try {
			if (connection != null) {
				connection.createStatement().execute("SHUTDOWN");
			}
		} catch (final SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Returns the entity manager.
	 * 
	 * @return Test entity manager.
	 */
	protected static EntityManager getEm() {
		return em;
	}

	/**
	 * Starts a transaction.
	 */
	protected final void beginTransaction() {
		getEm().getTransaction().begin();
	}

	/**
	 * Commits the current transaction.
	 */
	protected final void commitTransaction() {
		if (getEm().getTransaction().getRollbackOnly()) {
			getEm().getTransaction().rollback();
		} else {
			getEm().getTransaction().commit();
		}
	}

	/**
	 * Rolls back the current transaction (if active.
	 */
	protected final void rollbackTransaction() {
		if (getEm().getTransaction().isActive()) {
			getEm().getTransaction().rollback();
		}
	}

}
// CHECKSTYLE:ON
