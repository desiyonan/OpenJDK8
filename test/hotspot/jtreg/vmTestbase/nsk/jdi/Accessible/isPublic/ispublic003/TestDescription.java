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
 * @summary converted from VM Testbase nsk/jdi/Accessible/isPublic/ispublic003.
 * VM Testbase keywords: [quick, jpda, jdi]
 * VM Testbase readme:
 * DESCRIPTION:
 *  A test for isPublic() method of ReferenceType interface.
 *  The test checks if the method returns true any mirrored
 *  enum type which has public modifier.
 *  The test consists of a debugger program (ispublic003.java)
 *  and debuggee application (ispublic003a.java).
 *  Package name is nsk.jdi.Accessible.isPublic .
 *  The test works as follows.
 *  The debugger uses nsk.jdi.share framework classes to
 *  establish connection with debuggee. The debugger and debuggee
 *  synchronize with each other using special commands over
 *  communication channel provided by framework classes.
 *  Upon receiving the signal of readiness from debuggee,
 *  the debugger calls isPublic() method for each field
 *  of enum type declared in ispublic003a class.
 *  The test fails if false is returned once or more times.
 * COMMENTS:
 *
 * @library /vmTestbase
 *          /test/lib
 * @run driver jdk.test.lib.FileInstaller . .
 * @build nsk.jdi.Accessible.isPublic.ispublic003
 *        nsk.jdi.Accessible.isPublic.ispublic003a
 * @run main/othervm PropertyResolvingWrapper
 *      nsk.jdi.Accessible.isPublic.ispublic003
 *      -verbose
 *      -arch=${os.family}-${os.simpleArch}
 *      -waittime=5
 *      -debugee.vmkind=java
 *      -transport.address=dynamic
 *      "-debugee.vmkeys=${test.vm.opts} ${test.java.opts}"
 */

