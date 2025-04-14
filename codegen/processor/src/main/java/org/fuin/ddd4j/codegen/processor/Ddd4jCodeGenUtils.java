/**
 * Copyright (C) 2020 Michael Schnell. All rights reserved. http://www.fuin.org/
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.fuin.ddd4j.codegen.processor;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.util.function.Supplier;

/**
 * Utilities for the package.
 */
final class Ddd4jCodeGenUtils {

    private Ddd4jCodeGenUtils() {
        throw new UnsupportedOperationException("It is not allowed to create an instance of a utility class");
    }

    /**
     * Creates a velocity engine.
     * 
     * @return New already configured correctly instance.
     */
    public static VelocityEngine createVelocityEngine() {
        final VelocityEngine ve = new VelocityEngine();
        ve.addProperty("resource.loaders", "class");
        ve.addProperty("resource.loader.class.class", ClasspathResourceLoader.class.getName());
        ve.init();
        return ve;
    }

    /**
     * Execute some code using a different context class loader for the current thread.<br>
     * <br>
     * This allows to fix a problem while using Velocity within Eclipse. <br>
     * <code>org.apache.velocity.exception.VelocityException: The specified class for ResourceManager (org.apache.velocity.runtime.resource.ResourceManagerImpl) does not implement org.apache.velocity.runtime.resource.ResourceManager; Velocity is not initialized correctly.</code>
     * 
     * @param runnable
     *            Code to run with different classloader.
     * @param classLoader
     *            Classloader to use.
     * @return Result of execution.
     */
    public static boolean runWithContextClassLoader(final Supplier<Boolean> runnable, final ClassLoader classLoader) {
        final Thread current = Thread.currentThread();
        final ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            current.setContextClassLoader(classLoader);
            return runnable.get();
        } finally {
            current.setContextClassLoader(originalClassLoader);
        }
    }

}
