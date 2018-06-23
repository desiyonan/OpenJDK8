#!/bin/ksh

#
# Copyright (c) 2003, 2012, Oracle and/or its affiliates. All rights reserved.
# DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
#
# This code is free software; you can redistribute it and/or modify it
# under the terms of the GNU General Public License version 2 only, as
# published by the Free Software Foundation.
#
# This code is distributed in the hope that it will be useful, but WITHOUT
# ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
# FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
# version 2 for more details (a copy is included in the LICENSE file that
# accompanied this code).
#
# You should have received a copy of the GNU General Public License version
# 2 along with this work; if not, write to the Free Software Foundation,
# Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
#
# Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
# or visit www.oracle.com if you need additional information or have any
# questions.
#  
#

# This script is used to run consistency check of Serviceabilty Agent
# after making any libproc.so changes. Prints "PASSED" or "FAILED" in
# standard output.

usage() {
    echo "usage: $0"
    echo "   set SA_JAVA to be the java executable from JDK 1.5"
    exit 1   
}

if [ "$1" == "-help" ]; then
    usage
fi

if [ "x$SA_JAVA" = "x" ]; then
   SA_JAVA=java
fi

STARTDIR=`dirname $0`

# create java process with test case
tmp=/tmp/libproctest
rm -f $tmp
$SA_JAVA -d64 -classpath $STARTDIR LibprocTest > $tmp &
pid=$!
while [ ! -s $tmp ] ; do
  # Kludge alert!
  sleep 2
done

# dump core
gcore $pid
kill -9 $pid

OPTIONS="-Djava.library.path=$STARTDIR/../src/os/solaris/proc/sparcv9:$STARTDIR/../solaris/sparcv9"

# run libproc client
$SA_JAVA -d64 -showversion ${OPTIONS} -cp $STARTDIR/../../build/classes::$STARTDIR/../sa.jar:$STARTDIR LibprocClient x core.$pid

# delete core
rm -f core.$pid
