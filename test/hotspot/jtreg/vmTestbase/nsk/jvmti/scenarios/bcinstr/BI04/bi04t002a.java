/*
 * Copyright (c) 2004, 2018, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
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
package nsk.jvmti.scenarios.bcinstr.BI04;

public class bi04t002a {

    public final static int TOTAL_INSTRUMENTED_METHODS = 4;

    public final static int INSTR_EQUALS = 0;
    public final static int INSTR_TOSTRING = 1;
    public final static int INSTR_WAIT_JI = 2;
    public final static int INSTR_WAIT = 3;

    static int invocationsCount[] = new int[TOTAL_INSTRUMENTED_METHODS];

    public static void instrInvoke(int idx) {
        invocationsCount[idx]++;
    }

}
