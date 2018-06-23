/*
 * Copyright (c) 2016, 2018, Oracle and/or its affiliates. All rights reserved.
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

/**
 * @test
 * @summary Exercise initial transformation (ClassFileLoadHook)
 *  with CDS with Interface/Implementor pair
 * @library /test/lib /runtime/SharedArchiveFile /testlibrary/jvmti
 * @requires vm.cds
 * @requires vm.flavor != "minimal"
 * @requires !vm.graal.enabled
 * @modules java.base/jdk.internal.misc
 *          jdk.jartool/sun.tools.jar
 *          java.management
 *          java.instrument
 * @build TransformUtil TransformerAgent Interface Implementor
 * @run main/othervm TransformRelatedClasses Interface Implementor
 */

// Clarification on @requires declarations:
// CDS is not supported w/o the use of Compressed OOPs
// JVMTI's ClassFileLoadHook is not supported under minimal VM

// This test class uses TransformRelatedClasses to do its work.
// The goal of this test is to exercise transformation of related interface
// and its implementor in combination with CDS.
// The transformation is done via ClassFileLoadHook mechanism.
// Both superclass and subclass reside in the shared archive.
// The test consists of 4 test cases where transformation is applied
// to an interface and an implementor in a combinatorial manner.
// Please see TransformRelatedClasses.java for details.
