/**
 * Copyright (C) 2015 Michael Schnell. All rights reserved. 
 * http://www.fuin.org/
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
 * along with this library. If not, see http://www.gnu.org/licenses/.
 */
package org.fuin.ddd4j.ddd;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.common.ThreadSafe;

/**
 * Lightweight utility class to execute a method using reflection.
 */
@ThreadSafe
public final class MethodExecutor {

    /**
     * Finds a declared method that has an annotation and optional parameters.
     * 
     * @param obj
     *            Object to inspect.
     * @param annotationType
     *            Expected annotation type.
     * @param argumentTypes
     *            Expected argument types, empty array or <code>null</code>.
     * @param arguments
     *            Arguments, empty array or <code>null</code>. Must be the same size as 'argumentTypes'.
     * 
     * @return Value returned by the method or <code>null</code>.
     * 
     * @param <T>
     *            Type of the return value.
     */
    public final <T> T invokeDeclaredAnnotatedMethod(@NotNull final Object obj, @NotNull final Class<? extends Annotation> annotationType,
            final Class<?>[] argumentTypes, final Object[] arguments) {

        Contract.requireArgNotNull("obj", obj);
        Contract.requireArgNotNull("annotationType", annotationType);
        requireValid(argumentTypes, arguments);

        final Method method = findDeclaredAnnotatedMethod(obj, annotationType, argumentTypes);
        if (method == null) {
            throw new IllegalArgumentException("Cannot find a method annotated with '" + annotationType.getSimpleName()
                    + "' and arguments '" + Arrays.asList(argumentTypes) + "' in class '" + obj.getClass().getName() + "'");
        }
        return invoke(method, obj, arguments);

    }

    /**
     * Finds a declared method on the instance or it's parents that has an annotation and optional parameters. The class
     * 
     * @param obj
     *            Object to inspect.
     * @param annotationType
     *            Expected annotation type.
     * @param expectedArgumentTypes
     *            Expected argument types, empty array or <code>null</code>.
     * 
     * @return Method or <code>null</code> if any of the expected parameters does not match.
     */
    public final Method findDeclaredAnnotatedMethod(@NotNull final Object obj, @NotNull final Class<? extends Annotation> annotationType,
            final Class<?>... expectedArgumentTypes) {

        Contract.requireArgNotNull("obj", obj);
        Contract.requireArgNotNull("annotationType", annotationType);

        final List<Method> methods = getDeclaredMethodsIncludingSuperClasses(obj.getClass(), AbstractAggregateRoot.class);
        for (final Method method : methods) {
            if (method.getAnnotation(annotationType) != null) {
                final Class<?>[] types = method.getParameterTypes();
                if (same(expectedArgumentTypes, types)) {
                    return method;
                }
            }
        }
        return null;

    }

    /**
     * Returns a list of declared methods from classes and super classes. The given stop classes will not be inspected.
     * 
     * @param clasz
     *            Class to inspect.
     * @param stopParents
     *            Parent classes to stop inspection or {@literal null} to stop at {@link Object}.
     * 
     * @return List of methods.
     */
    public final List<Method> getDeclaredMethodsIncludingSuperClasses(@NotNull final Class<?> clasz,
            @NotNull final Class<?>... stopParents) {
        Contract.requireArgNotNull("clasz", clasz);
        Contract.requireArgNotNull("stopParents", stopParents);

        final List<Class<?>> stopList = new ArrayList<>(Arrays.asList(stopParents));
        if (!stopList.contains(Object.class)) {
            stopList.add(Object.class);
        }

        final List<Method> list = new ArrayList<>();
        Class<?> toInspect = clasz;
        while (!stopList.contains(toInspect)) {
            final Method[] methods = toInspect.getDeclaredMethods();
            for (final Method method : methods) {
                list.add(method);
            }
            toInspect = toInspect.getSuperclass();
        }
        return list;
    }

    /**
     * Invokes a method with any number of arguments.
     * 
     * @param method
     *            Method to call.
     * @param target
     *            Object that contains the method.
     * @param args
     *            Arguments, empty array or <code>null</code>.
     * 
     * @return Value returned by the method or <code>null</code>.
     * 
     * @param <T>
     *            Type of the return value.
     */
    @SuppressWarnings("unchecked")
    public final <T> T invoke(@NotNull final Method method, @NotNull final Object target, final Object... args) {

        Contract.requireArgNotNull("method", method);
        Contract.requireArgNotNull("target", target);

        try {

            if (!method.canAccess(target)) {
                method.setAccessible(true);
            }
            return (T) method.invoke(target, args);
        } catch (final IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new RuntimeException(createInvokeErrMsg(target, method, args), ex);
        }
    }

    private String createInvokeErrMsg(final Object target, final Method method, final Object... args) {
        if ((args == null) || (args.length == 0)) {
            return "Failed to call method '" + method + "' on '" + target.getClass().getSimpleName();
        }
        return "Failed to call method '" + method + "' on '" + target.getClass().getSimpleName() + "' with arguments: "
                + Arrays.asList(args);
    }

    /**
     * Verifies if both arrays have the same length and types.
     * 
     * @param expected
     *            Expected types, empty array or <code>null</code>.
     * @param actual
     *            Actual types, empty array or <code>null</code>.
     * 
     * @return TRUE if both arguments match.
     */
    public final boolean same(final Class<?>[] expected, final Class<?>[] actual) {
        if (expected == null) {
            if (actual == null) {
                return true;
            }
            return false;
        }
        if (actual == null) {
            return false;
        }
        if (expected.length != actual.length) {
            return false;
        }
        for (int i = 0; i < expected.length; i++) {
            if (expected[0] != actual[i]) {
                return false;
            }
        }
        return true;
    }

    private void requireValid(final Class<?>[] argumentTypes, final Object[] arguments) {
        if ((argumentTypes == null) && (arguments != null)) {
            throw new IllegalArgumentException("Argument type array is null, but arguments array is not: " + Arrays.asList(arguments));
        }
        if ((arguments == null) && (argumentTypes != null)) {
            throw new IllegalArgumentException("Arguments array is null, but argument types array is not: " + Arrays.asList(argumentTypes));
        }
        if ((argumentTypes != null) && (argumentTypes.length != arguments.length)) {
            throw new IllegalArgumentException("Types and arguments have different length: Types=" + Arrays.asList(argumentTypes)
                    + ", Args=" + Arrays.asList(arguments));
        }
    }

}
