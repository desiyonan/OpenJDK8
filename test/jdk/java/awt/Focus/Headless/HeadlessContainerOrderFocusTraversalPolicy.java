/*
 * Copyright (c) 2007, 2014, Oracle and/or its affiliates. All rights reserved.
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

import java.awt.*;

/*
 * @test
 * @summary Check that ContainerOrderFocusTraversalPolicy constructor and
 *          methods do not throw unexpected exceptions in headless mode
 * @run main/othervm -Djava.awt.headless=true HeadlessContainerOrderFocusTraversalPolicy
 */

public class HeadlessContainerOrderFocusTraversalPolicy {

    public static void main(String args[]) {
        ContainerOrderFocusTraversalPolicy cot = new ContainerOrderFocusTraversalPolicy();

        Container c = new Container();
        Component cb1;
        Component cb2;
        Component cb3;

        c.setFocusCycleRoot(true);
        c.setFocusTraversalPolicy(cot);
        c.add(cb1 = new Component(){});
        c.add(cb2 = new Component(){});
        c.add(cb3 = new Component(){});

        cot.getComponentAfter(c, cb1);
        cot.getComponentAfter(c, cb2);
        cot.getComponentAfter(c, cb3);

        cot.getComponentBefore(c, cb1);
        cot.getComponentBefore(c, cb2);
        cot.getComponentBefore(c, cb3);

        cot.getFirstComponent(c);

        cot.getLastComponent(c);

        cot.getDefaultComponent(c);
        cot.setImplicitDownCycleTraversal(true);
        cot.setImplicitDownCycleTraversal(false);
        cot.getImplicitDownCycleTraversal();
    }
}
