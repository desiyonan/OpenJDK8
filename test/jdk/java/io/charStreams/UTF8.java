/*
 * Copyright (c) 1997, Oracle and/or its affiliates. All rights reserved.
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

/* @test
   @bug 4059684
   @summary Simple heartbeat test of the UTF8 byte->char converter
 */

import java.io.*;

public class UTF8 {

    static String test
    = "This is a simple\ntest of the UTF8\r\nbyte-to-char and char-to-byte\nconverters.";

    public static void main(String[] args) throws IOException {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        Writer out = new OutputStreamWriter(bo, "UTF8");
        out.write(test);
        out.close();

        Reader in
            = new InputStreamReader(new ByteArrayInputStream(bo.toByteArray()),
                                    "UTF8");

        StringBuffer sb = new StringBuffer();
        char buf[] = new char[1000];
        int n;
        while ((n = in.read(buf, 0, buf.length)) >= 0) {
            sb.append(buf, 0, n);
            System.err.println(n);
        }
        if (! sb.toString().equals(test)) {
            System.err.println("In: [" + test + "]");
            System.err.println("Out: [" + sb.toString() + "]");
            throw new RuntimeException("Output does not match input");
        }

    }

}
