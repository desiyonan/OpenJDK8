/*
 * Copyright (c) 2018, Oracle and/or its affiliates. All rights reserved.
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


/*
 * @test
 *
 * @summary converted from VM Testbase nsk/jvmti/scenarios/capability/CM01/cm01t019.
 * VM Testbase keywords: [quick, jpda, jvmti, onload_only_caps, noras, redefine]
 * VM Testbase readme:
 * DESCRIPTION
 *     The test is for CM01 scenario of "capability management".
 *     This test checks capability can_get_source_debug_extension
 *     and correspondent functions:
 *         GetSourceDebugExtension
 *     Testcases:
 *       1. Check if GetPotentialCapabilities returns the capability
 *       2. Add the capability during Live phase
 *       3. Check if GetCapabilities returns the capability
 *       4. Check that only correspondent functions work and functions of
 *          other capabilities return JVMTI_ERROR_MUST_POSSESS_CAPABILITY
 *       5. Relinquish the capability during Live phase
 *       6. Check if GetCapabilities does not return the capability
 *       7. Check that correspondent to relinquished capability functions
 *          return JVMTI_ERROR_MUST_POSSESS_CAPABILITY
 *       8. Add back the capability and check with GetCapabilities
 *       9. Check if VM exits well with the capability has not been relinquished
 * COMMENTS
 *     The test updated to match new JVMTI spec 0.2.90:
 *     - replace tested capability can_get_exception_info
 *       with one of the new capability can_get_source_debug_extension
 *     - remove function checkExceptionInfoFunctions()
 *       because of removed capability can_get_exception_info
 *     - extend function checkSourceInfoFunctions()
 *       by adding check for GetSourceFileName()
 *     - change signatures of heap iteration callbacks
 *       used in function checkHeapFunctions()
 *     - change signature of agentProc function
 *       and save JNIEnv pointer now passed as argument.
 *     Fixed according to 4960375 bug.
 *         The test updated to match new JVMTI spec 0.2.94: changed signature
 *         of heap iteration callbacks used in checkHeapFunctions().
 *     In addition to the previous fix made alive
 *     checkGetCurrentThreadCpuTime() and checkGetThreadCpuTime().
 *     Fixed according to bug 6277019.
 *       Remove function IsMethodObsolete
 *       because of removed capability can_redefine_classes
 *
 * @library /vmTestbase
 *          /test/lib
 * @run driver jdk.test.lib.FileInstaller . .
 * @run main/othervm/native
 *      -agentlib:cm01t019=-waittime=5
 *      nsk.jvmti.scenarios.capability.CM01.cm01t019
 */

