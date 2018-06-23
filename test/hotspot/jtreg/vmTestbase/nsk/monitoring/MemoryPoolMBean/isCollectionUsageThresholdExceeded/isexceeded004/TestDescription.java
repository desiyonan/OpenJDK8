/*
 * Copyright (c) 2017, 2018, Oracle and/or its affiliates. All rights reserved.
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
 * @summary converted from VM Testbase nsk/monitoring/MemoryPoolMBean/isCollectionUsageThresholdExceeded/isexceeded004.
 * VM Testbase keywords: [quick, monitoring]
 * VM Testbase readme:
 * DESCRIPTION
 *     The test checks that
 *         MemoryPoolMBean.isCollectionUsageThresholdExceeded()
 *     returns correct results, if the pool supports collection usage thresholds.
 *     The test sets a threshold that is greater than used value, allocates 100K,
 *     and checks that getCollectionUsageThreshold(), getUsed(),
 *     isCollectionUsageThresholdExceeded() do not contradict each other, i.e.:
 *         1. if used value is greater or equal than threshold, then
 *            isCollectionUsageThresholdExceeded() is expected to return true;
 *         2. if used value is less than threshold, then
 *            isCollectionUsageThresholdExceeded() is expected to return false.
 *     If the collection usage thresholds are not supported,
 *     UnsupportedOperationException is expected to be thrown by the method.
 *     The test implements access to the metrics via default MBean server proxy.
 * COMMENT
 *     Fixed the bug
 *     4989235 TEST: The spec is updated accoring to 4982289, 4985742
 *
 * @library /vmTestbase
 *          /test/lib
 * @run driver jdk.test.lib.FileInstaller . .
 * @run main/othervm
 *      nsk.monitoring.MemoryPoolMBean.isCollectionUsageThresholdExceeded.isexceeded001
 *      -testMode=proxy
 */

