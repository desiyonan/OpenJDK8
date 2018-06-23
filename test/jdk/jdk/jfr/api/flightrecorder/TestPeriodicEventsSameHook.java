/*
 * Copyright (c) 2018, Oracle and/or its affiliates. All rights reserved.
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

package jdk.jfr.api.flightrecorder;

import jdk.jfr.Event;
import jdk.jfr.FlightRecorder;

/*
 * @test
 * @summary Check that an IllegalArgumentException is thrown if event is added twice
 * @key jfr
 * @library /test/lib
 * @run main/othervm jdk.jfr.api.flightrecorder.TestPeriodicEventsSameHook
 */
public class TestPeriodicEventsSameHook {

    private static class MyEvent extends Event {
    }

    private static class MyHook implements Runnable {
        @Override
        public void run() {
        }
    }

    public static void main(String[] args) throws Exception {
        MyHook hook = new MyHook();
        FlightRecorder.addPeriodicEvent(MyEvent.class, hook);
        try {
            FlightRecorder.addPeriodicEvent(MyEvent.class, hook);
            throw new Exception("Expected IllegalArgumentException when adding same hook twice");
        } catch (IllegalArgumentException iae) {
            if (!iae.getMessage().equals("Hook has already been added")) {
                throw new Exception("Expected IllegalArgumentException with message 'Hook has already been added'");
            }
        }
    }
}
