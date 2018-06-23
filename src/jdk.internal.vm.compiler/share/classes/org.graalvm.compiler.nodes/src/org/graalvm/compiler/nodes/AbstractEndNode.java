/*
 * Copyright (c) 2011, Oracle and/or its affiliates. All rights reserved.
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
package org.graalvm.compiler.nodes;

import static org.graalvm.compiler.nodeinfo.NodeCycles.CYCLES_0;
import static org.graalvm.compiler.nodeinfo.NodeSize.SIZE_0;

import java.util.Collections;

import org.graalvm.compiler.core.common.type.StampFactory;
import org.graalvm.compiler.graph.Node;
import org.graalvm.compiler.graph.NodeClass;
import org.graalvm.compiler.nodeinfo.NodeInfo;
import org.graalvm.compiler.nodes.spi.LIRLowerable;
import org.graalvm.compiler.nodes.spi.NodeLIRBuilderTool;

@NodeInfo(cycles = CYCLES_0, size = SIZE_0)
public abstract class AbstractEndNode extends FixedNode implements LIRLowerable {

    public static final NodeClass<AbstractEndNode> TYPE = NodeClass.create(AbstractEndNode.class);

    protected AbstractEndNode(NodeClass<? extends AbstractEndNode> c) {
        super(c, StampFactory.forVoid());
    }

    @Override
    public void generate(NodeLIRBuilderTool gen) {
        gen.visitEndNode(this);
    }

    public AbstractMergeNode merge() {
        return (AbstractMergeNode) usages().first();
    }

    @Override
    public boolean verify() {
        assertTrue(getUsageCount() <= 1, "at most one usage");
        return super.verify();
    }

    @Override
    public Iterable<? extends Node> cfgSuccessors() {
        AbstractMergeNode merge = merge();
        if (merge != null) {
            return Collections.singletonList(merge);
        }
        return Collections.emptyList();
    }
}
