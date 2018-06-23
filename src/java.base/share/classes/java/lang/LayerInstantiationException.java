/*
 * Copyright (c) 2015, 2017, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package java.lang;

/**
 * Thrown when creating a {@linkplain ModuleLayer module layer} fails.
 *
 * @see ModuleLayer
 * @since 9
 * @spec JPMS
 */
public class LayerInstantiationException extends RuntimeException {
    private static final long serialVersionUID = -906239691613568347L;

    /**
     * Constructs a {@code LayerInstantiationException} with no detail message.
     */
    public LayerInstantiationException() {
    }

    /**
     * Constructs a {@code LayerInstantiationException} with the given detail
     * message.
     *
     * @param msg
     *        The detail message; can be {@code null}
     */
    public LayerInstantiationException(String msg) {
        super(msg);
    }

    /**
     * Constructs a {@code LayerInstantiationException} with the given cause.
     *
     * @param cause
     *        The cause; can be {@code null}
     */
    public LayerInstantiationException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a {@code LayerInstantiationException} with the given detail
     * message and cause.
     *
     * @param msg
     *        The detail message; can be {@code null}
     * @param cause
     *        The cause; can be {@code null}
     */
    public LayerInstantiationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

