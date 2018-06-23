/*
 * Copyright (c) 2010, 2013, Oracle and/or its affiliates. All rights reserved.
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
 * Edit strings test
 *
 * @test
 * @option -scripting
 * @run
 */

# scripting mode - shell style line comment works..

var t = "normal";
print("a ${t} string");

// JavaScript style line comment works too..
var i = 0;
print("line ${i++}\nline ${i++}");

var c = 3;
print(<<EOD + "!!!!")
Here is a long sentence
that may extend over ${c}
lines.
EOD

c = 4;
print(<<<EOD + "!!!!")
Here is a long sentence
that may extend over ${c}
lines.
EOD

eval(<<BRAINTEASER);
print("This is executed how");
BRAINTEASER

print(<<HTML);
    <html>
        <head>
            <title>Testing</title>
        </head>
        <body>
            <p>This is a test.<p>
        </body>
    </html>
HTML

var x = 1
<<
3;
print(x);

var y = <<EOD;
There we go
EOD y = "No we don't";
print(y);


print(readFully(__FILE__));
