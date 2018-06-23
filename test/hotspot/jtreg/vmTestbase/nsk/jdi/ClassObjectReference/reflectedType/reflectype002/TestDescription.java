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
 * @summary converted from VM Testbase nsk/jdi/ClassObjectReference/reflectedType/reflectype002.
 * VM Testbase keywords: [jpda, jdi, nonconcurrent]
 * VM Testbase readme:
 * DESCRIPTION
 *         nsk/jdi/ClassObjectReference/reflectedType/reflectype002 test
 *         checks the reflectedType() method of ClassObjectReference interface
 *         of the com.sun.jdi package for UNLOADED class:
 *         the test loads a class, gets a ReferenceType instance for this
 *         class, then gets a ClassObjectReference instance, then enforces
 *         the class to be unloaded and calls the reflectedType() method -
 *         the com.sun.jdi.ObjectCollectedException should be thrown in this case.
 * COMMENTS
 *   Fixed test due to bug
 *     4463674: TEST_BUG: some JDI tests are timing dependent
 *   The test was modified to comply with new execution scheme to have separate
*    directory for precompiled classes:
 *           - reflectype002b class was moved in 'loadclass' subdirectory;
 *           - package name was added in reflectype002b class;
 *           - ${COMMON_CLASSES_LOCATION} instead of ${TESTDIR} in .cfg file;
 *           - ClassUnloader seekes for reflectype002b class in
 *             ${COMMON_CLASSES_LOCATION}/loadclass directory.
 *   4505735 equals002 and other tests fail with merlin
 *
 * @library /vmTestbase
 *          /test/lib
 * @run driver jdk.test.lib.FileInstaller . .
 * @build nsk.jdi.ClassObjectReference.reflectedType.reflectype002
 *        nsk.jdi.ClassObjectReference.reflectedType.reflectype002a
 *
 * @comment compile loadclassXX to bin/loadclassXX
 * @run driver nsk.share.ExtraClassesBuilder
 *      loadclass
 *
 * @run main/othervm PropertyResolvingWrapper
 *      nsk.jdi.ClassObjectReference.reflectedType.reflectype002
 *      -verbose
 *      -arch=${os.family}-${os.simpleArch}
 *      -waittime=5
 *      -debugee.vmkind=java
 *      -transport.address=dynamic
 *      "-debugee.vmkeys=${test.vm.opts} ${test.java.opts}" ./bin
 */

