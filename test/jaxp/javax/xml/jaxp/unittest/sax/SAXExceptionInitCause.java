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
 * @bug 6857903
 * @summary The initCause() incorrectly initialize the cause in
 * SAXException class when used with SAXException(String)
 * constructor.
 * @run testng/othervm sax.SAXExceptionInitCause
 * @author aleksej.efimov@oracle.com
 */

package sax;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

public class SAXExceptionInitCause {

    @Test
    public void testOwnSerializationNoCause() throws Exception {
        SAXException noCauseException = new SAXException(SAX_MESSAGE);
        SAXException deserializedException;
        byte[] serialSAX;

        serialSAX = pickleException(noCauseException);
        deserializedException = unpickleException(serialSAX);

        Assert.assertNull(deserializedException.getCause());
        Assert.assertEquals(deserializedException.getMessage(), SAX_MESSAGE);
    }

    @Test
    public void testSerializationWithCause() throws Exception {
        SAXException withCauseException = new SAXException(SAX_MESSAGE);
        withCauseException.initCause(new Exception(SAX_CAUSE_MESSAGE));
        SAXException deserializedException;
        byte[] serialSAX;

        serialSAX = pickleException(withCauseException);
        deserializedException = unpickleException(serialSAX);

        Assert.assertNotNull(deserializedException.getCause());
        Assert.assertEquals(deserializedException.getMessage(), SAX_MESSAGE);
        Assert.assertEquals(deserializedException.getCause().getMessage(), SAX_CAUSE_MESSAGE);
    }

    @Test
    public void testCauseInitByCtor() throws Exception {
        // Check that constructor properly initializes cause
        Exception cause = new Exception(SAX_CAUSE_MESSAGE);
        SAXException exception = new SAXException(cause);
        Assert.assertSame(exception.getCause(), cause);
        Assert.assertSame(exception.getException(), cause);
    }

    @Test
    public void testCauseInitWithException() {
        // Check that initCause properly initializes cause
        SAXException exception = new SAXException();
        Exception cause = new Exception(SAX_CAUSE_MESSAGE);
        exception.initCause(cause);
        Assert.assertSame(exception.getCause(), cause);
        Assert.assertSame(exception.getException(), cause);
    }

    @Test
    public void testCauseInitWithThrowable() {
        // Check that if cause is initialized with Throwable instead of Exception
        // then getException returns 'null'
        SAXException exception = new SAXException();
        Throwable cause = new Throwable(SAX_CAUSE_MESSAGE);
        exception.initCause(cause);
        Assert.assertSame(exception.getCause(),cause);
        Assert.assertNull(exception.getException());
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testInitCauseTwice() {
        SAXException exception = new SAXException(new Exception(SAX_CAUSE_MESSAGE));
        // Expecting IllegalStateException at this point
        exception.initCause(new Exception(SAX_CAUSE_MESSAGE));
    }

    @Test
    public void testLegacySerialCtor() throws Exception {
        SAXException saxException8 = unpickleException(JDK8_SET_WITH_CTOR_ONLY);
        Assert.assertNotNull(saxException8.getCause());
        Assert.assertNotNull(saxException8.getException());
    }

    @Test
    public void testLegacySerialCtorAndInit() throws Exception {
        SAXException saxException8 = unpickleException(JDK8_SET_WITH_CTOR_AND_INIT);
        Assert.assertNotNull(saxException8.getCause());
        Assert.assertNotNull(saxException8.getException());
    }

    @Test
    public void testLegacySerialInitCause() throws Exception {
        SAXException saxException8 = unpickleException(JDK8_WITH_INIT_ONLY);
        Assert.assertNotNull(saxException8.getCause());
        Assert.assertNotNull(saxException8.getException());
    }

    @Test
    public void testLegacySerialNothingSet() throws Exception {
        SAXException saxException8 = unpickleException(JDK8_NOTHING_SET);
        Assert.assertNull(saxException8.getCause());
        Assert.assertNull(saxException8.getException());
    }

    @Test(expectedExceptions = InvalidClassException.class)
    public void testReadObjectIllegalStateException() throws Exception {
        SAXException saxException8 = unpickleException(JDK8_CHECK_ILLEGAL_STATE_EXCEPTION);
    }

    // Serialize SAXException to byte array
    private static byte[] pickleException(SAXException saxException) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ObjectOutputStream saxExceptionOOS = new ObjectOutputStream(bos)) {
            saxExceptionOOS.writeObject(saxException);
        }
        return bos.toByteArray();
    }

    //Deserialize SAXException with byte array as serial data source
    private static SAXException unpickleException(byte[] ser)
            throws IOException, ClassNotFoundException {
        SAXException saxException;
        ByteArrayInputStream bis = new ByteArrayInputStream(ser);
        try (ObjectInputStream saxExceptionOIS = new ObjectInputStream(bis)) {
            saxException = (SAXException) saxExceptionOIS.readObject();
        }
        return saxException;
    }

    private static String SAX_MESSAGE = "SAXException message";
    private static String SAX_CAUSE_MESSAGE = "SAXException cause message";

    /* This is a serial form of ordinary SAXException serialized
     * by the following JDK8 code:
     *   ByteArrayOutputStream fser = new ByteArrayOutputStream();
     *   ObjectOutputStream oos = new ObjectOutputStream(fser);
     *   oos.writeObject(new SAXException(new Exception("Only exception field is set.")));
     *   oos.close();
     */
    private static final byte[] JDK8_SET_WITH_CTOR_ONLY = {
            -84, -19, 0, 5, 115, 114, 0, 24, 111, 114, 103, 46, 120, 109, 108, 46, 115, 97, 120, 46, 83, 65, 88, 69, 120,
            99, 101, 112, 116, 105, 111, 110, 8, 24, 23, 45, 87, -89, -2, 32, 2, 0, 1, 76, 0, 9, 101, 120, 99, 101, 112,
            116, 105, 111, 110, 116, 0, 21, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 69, 120, 99, 101, 112, 116,
            105, 111, 110, 59, 120, 114, 0, 19, 106, 97, 118, 97, 46, 108, 97, 110, 103, 46, 69, 120, 99, 101, 112, 116,
            105, 111, 110, -48, -3, 31, 62, 26, 59, 28, -60, 2, 0, 0, 120, 114, 0, 19, 106, 97, 118, 97, 46, 108, 97, 110,
            103, 46, 84, 104, 114, 111, 119, 97, 98, 108, 101, -43, -58, 53, 39, 57, 119, -72, -53, 3, 0, 4, 76, 0, 5, 99,
            97, 117, 115, 101, 116, 0, 21, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 84, 104, 114, 111, 119, 97, 98,
            108, 101, 59, 76, 0, 13, 100, 101, 116, 97, 105, 108, 77, 101, 115, 115, 97, 103, 101, 116, 0, 18, 76, 106, 97,
            118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 91, 0, 10, 115, 116, 97, 99, 107, 84, 114,
            97, 99, 101, 116, 0, 30, 91, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 97, 99, 107, 84, 114, 97,
            99, 101, 69, 108, 101, 109, 101, 110, 116, 59, 76, 0, 20, 115, 117, 112, 112, 114, 101, 115, 115, 101, 100, 69,
            120, 99, 101, 112, 116, 105, 111, 110, 115, 116, 0, 16, 76, 106, 97, 118, 97, 47, 117, 116, 105, 108, 47, 76,
            105, 115, 116, 59, 120, 112, 113, 0, 126, 0, 8, 112, 117, 114, 0, 30, 91, 76, 106, 97, 118, 97, 46, 108, 97,
            110, 103, 46, 83, 116, 97, 99, 107, 84, 114, 97, 99, 101, 69, 108, 101, 109, 101, 110, 116, 59, 2, 70, 42, 60,
            60, -3, 34, 57, 2, 0, 0, 120, 112, 0, 0, 0, 2, 115, 114, 0, 27, 106, 97, 118, 97, 46, 108, 97, 110, 103, 46,
            83, 116, 97, 99, 107, 84, 114, 97, 99, 101, 69, 108, 101, 109, 101, 110, 116, 97, 9, -59, -102, 38, 54, -35,
            -123, 2, 0, 4, 73, 0, 10, 108, 105, 110, 101, 78, 117, 109, 98, 101, 114, 76, 0, 14, 100, 101, 99, 108, 97,
            114, 105, 110, 103, 67, 108, 97, 115, 115, 113, 0, 126, 0, 5, 76, 0, 8, 102, 105, 108, 101, 78, 97, 109, 101,
            113, 0, 126, 0, 5, 76, 0, 10, 109, 101, 116, 104, 111, 100, 78, 97, 109, 101, 113, 0, 126, 0, 5, 120, 112, 0,
            0, 0, 26, 116, 0, 8, 71, 101, 110, 101, 114, 97, 116, 101, 116, 0, 13, 71, 101, 110, 101, 114, 97, 116, 101,
            46, 106, 97, 118, 97, 116, 0, 15, 103, 101, 110, 101, 114, 97, 116, 101, 67, 97, 115, 101, 79, 110, 101, 115,
            113, 0, 126, 0, 11, 0, 0, 0, 10, 113, 0, 126, 0, 13, 113, 0, 126, 0, 14, 116, 0, 4, 109, 97, 105, 110, 115,
            114, 0, 38, 106, 97, 118, 97, 46, 117, 116, 105, 108, 46, 67, 111, 108, 108, 101, 99, 116, 105, 111, 110, 115,
            36, 85, 110, 109, 111, 100, 105, 102, 105, 97, 98, 108, 101, 76, 105, 115, 116, -4, 15, 37, 49, -75, -20,
            -114, 16, 2, 0, 1, 76, 0, 4, 108, 105, 115, 116, 113, 0, 126, 0, 7, 120, 114, 0, 44, 106, 97, 118, 97, 46,
            117, 116, 105, 108, 46, 67, 111, 108, 108, 101, 99, 116, 105, 111, 110, 115, 36, 85, 110, 109, 111, 100, 105,
            102, 105, 97, 98, 108, 101, 67, 111, 108, 108, 101, 99, 116, 105, 111, 110, 25, 66, 0, -128, -53, 94, -9, 30,
            2, 0, 1, 76, 0, 1, 99, 116, 0, 22, 76, 106, 97, 118, 97, 47, 117, 116, 105, 108, 47, 67, 111, 108, 108, 101,
            99, 116, 105, 111, 110, 59, 120, 112, 115, 114, 0, 19, 106, 97, 118, 97, 46, 117, 116, 105, 108, 46, 65, 114,
            114, 97, 121, 76, 105, 115, 116, 120, -127, -46, 29, -103, -57, 97, -99, 3, 0, 1, 73, 0, 4, 115, 105, 122, 101,
            120, 112, 0, 0, 0, 0, 119, 4, 0, 0, 0, 0, 120, 113, 0, 126, 0, 23, 120, 115, 113, 0, 126, 0, 2, 113, 0, 126, 0,
            24, 116, 0, 28, 79, 110, 108, 121, 32, 101, 120, 99, 101, 112, 116, 105, 111, 110, 32, 102, 105, 101, 108, 100,
            32, 105, 115, 32, 115, 101, 116, 46, 117, 113, 0, 126, 0, 9, 0, 0, 0, 2, 115, 113, 0, 126, 0, 11, 0, 0, 0, 26,
            113, 0, 126, 0, 13, 113, 0, 126, 0, 14, 113, 0, 126, 0, 15, 115, 113, 0, 126, 0, 11, 0, 0, 0, 10, 113, 0, 126,
            0, 13, 113, 0, 126, 0, 14, 113, 0, 126, 0, 17, 113, 0, 126, 0, 21, 120
    };

    /* This is a serial form of SAXException with two causes serialized
     * by the following JDK8 code:
     *   ByteArrayOutputStream fser = new ByteArrayOutputStream();
     *   ObjectOutputStream oos = new ObjectOutputStream(fser);
     *   oos.writeObject(new SAXException(new Exception("Exception and cause fields are set"))
     *                                    .initCause(new Exception("Cause field")));
     *   oos.close();
     */
    private static final byte[] JDK8_SET_WITH_CTOR_AND_INIT = {
            -84, -19, 0, 5, 115, 114, 0, 24, 111, 114, 103, 46, 120, 109, 108, 46, 115, 97, 120, 46, 83, 65, 88, 69, 120,
            99, 101, 112, 116, 105, 111, 110, 8, 24, 23, 45, 87, -89, -2, 32, 2, 0, 1, 76, 0, 9, 101, 120, 99, 101, 112,
            116, 105, 111, 110, 116, 0, 21, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 69, 120, 99, 101, 112, 116,
            105, 111, 110, 59, 120, 114, 0, 19, 106, 97, 118, 97, 46, 108, 97, 110, 103, 46, 69, 120, 99, 101, 112, 116,
            105, 111, 110, -48, -3, 31, 62, 26, 59, 28, -60, 2, 0, 0, 120, 114, 0, 19, 106, 97, 118, 97, 46, 108, 97, 110,
            103, 46, 84, 104, 114, 111, 119, 97, 98, 108, 101, -43, -58, 53, 39, 57, 119, -72, -53, 3, 0, 4, 76, 0, 5, 99,
            97, 117, 115, 101, 116, 0, 21, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 84, 104, 114, 111, 119, 97, 98,
            108, 101, 59, 76, 0, 13, 100, 101, 116, 97, 105, 108, 77, 101, 115, 115, 97, 103, 101, 116, 0, 18, 76, 106, 97,
            118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 91, 0, 10, 115, 116, 97, 99, 107, 84, 114,
            97, 99, 101, 116, 0, 30, 91, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 97, 99, 107, 84, 114, 97,
            99, 101, 69, 108, 101, 109, 101, 110, 116, 59, 76, 0, 20, 115, 117, 112, 112, 114, 101, 115, 115, 101, 100, 69,
            120, 99, 101, 112, 116, 105, 111, 110, 115, 116, 0, 16, 76, 106, 97, 118, 97, 47, 117, 116, 105, 108, 47, 76,
            105, 115, 116, 59, 120, 112, 115, 113, 0, 126, 0, 2, 113, 0, 126, 0, 9, 116, 0, 11, 67, 97, 117, 115, 101, 32,
            102, 105, 101, 108, 100, 117, 114, 0, 30, 91, 76, 106, 97, 118, 97, 46, 108, 97, 110, 103, 46, 83, 116, 97, 99,
            107, 84, 114, 97, 99, 101, 69, 108, 101, 109, 101, 110, 116, 59, 2, 70, 42, 60, 60, -3, 34, 57, 2, 0, 0, 120,
            112, 0, 0, 0, 2, 115, 114, 0, 27, 106, 97, 118, 97, 46, 108, 97, 110, 103, 46, 83, 116, 97, 99, 107, 84, 114,
            97, 99, 101, 69, 108, 101, 109, 101, 110, 116, 97, 9, -59, -102, 38, 54, -35, -123, 2, 0, 4, 73, 0, 10, 108,
            105, 110, 101, 78, 117, 109, 98, 101, 114, 76, 0, 14, 100, 101, 99, 108, 97, 114, 105, 110, 103, 67, 108, 97,
            115, 115, 113, 0, 126, 0, 5, 76, 0, 8, 102, 105, 108, 101, 78, 97, 109, 101, 113, 0, 126, 0, 5, 76, 0, 10, 109,
            101, 116, 104, 111, 100, 78, 97, 109, 101, 113, 0, 126, 0, 5, 120, 112, 0, 0, 0, 34, 116, 0, 8, 71, 101, 110,
            101, 114, 97, 116, 101, 116, 0, 13, 71, 101, 110, 101, 114, 97, 116, 101, 46, 106, 97, 118, 97, 116, 0, 15,
            103, 101, 110, 101, 114, 97, 116, 101, 67, 97, 115, 101, 84, 119, 111, 115, 113, 0, 126, 0, 13, 0, 0, 0, 11,
            113, 0, 126, 0, 15, 113, 0, 126, 0, 16, 116, 0, 4, 109, 97, 105, 110, 115, 114, 0, 38, 106, 97, 118, 97, 46,
            117, 116, 105, 108, 46, 67, 111, 108, 108, 101, 99, 116, 105, 111, 110, 115, 36, 85, 110, 109, 111, 100, 105,
            102, 105, 97, 98, 108, 101, 76, 105, 115, 116, -4, 15, 37, 49, -75, -20, -114, 16, 2, 0, 1, 76, 0, 4, 108, 105,
            115, 116, 113, 0, 126, 0, 7, 120, 114, 0, 44, 106, 97, 118, 97, 46, 117, 116, 105, 108, 46, 67, 111, 108, 108,
            101, 99, 116, 105, 111, 110, 115, 36, 85, 110, 109, 111, 100, 105, 102, 105, 97, 98, 108, 101, 67, 111, 108,
            108, 101, 99, 116, 105, 111, 110, 25, 66, 0, -128, -53, 94, -9, 30, 2, 0, 1, 76, 0, 1, 99, 116, 0, 22, 76,
            106, 97, 118, 97, 47, 117, 116, 105, 108, 47, 67, 111, 108, 108, 101, 99, 116, 105, 111, 110, 59, 120, 112,
            115, 114, 0, 19, 106, 97, 118, 97, 46, 117, 116, 105, 108, 46, 65, 114, 114, 97, 121, 76, 105, 115, 116, 120,
            -127, -46, 29, -103, -57, 97, -99, 3, 0, 1, 73, 0, 4, 115, 105, 122, 101, 120, 112, 0, 0, 0, 0, 119, 4, 0, 0,
            0, 0, 120, 113, 0, 126, 0, 25, 120, 112, 117, 113, 0, 126, 0, 11, 0, 0, 0, 2, 115, 113, 0, 126, 0, 13, 0, 0,
            0, 34, 113, 0, 126, 0, 15, 113, 0, 126, 0, 16, 113, 0, 126, 0, 17, 115, 113, 0, 126, 0, 13, 0, 0, 0, 11, 113,
            0, 126, 0, 15, 113, 0, 126, 0, 16, 113, 0, 126, 0, 19, 113, 0, 126, 0, 23, 120, 115, 113, 0, 126, 0, 2, 113,
            0, 126, 0, 29, 116, 0, 34, 69, 120, 99, 101, 112, 116, 105, 111, 110, 32, 97, 110, 100, 32, 99, 97, 117, 115,
            101, 32, 102, 105, 101, 108, 100, 115, 32, 97, 114, 101, 32, 115, 101, 116, 117, 113, 0, 126, 0, 11, 0, 0, 0,
            2, 115, 113, 0, 126, 0, 13, 0, 0, 0, 34, 113, 0, 126, 0, 15, 113, 0, 126, 0, 16, 113, 0, 126, 0, 17, 115, 113,
            0, 126, 0, 13, 0, 0, 0, 11, 113, 0, 126, 0, 15, 113, 0, 126, 0, 16, 113, 0, 126, 0, 19, 113, 0, 126, 0, 23, 120
    };

    /*
     *  ByteArrayOutputStream fser = new ByteArrayOutputStream();
     *  ObjectOutputStream oos = new ObjectOutputStream(fser);
     *  oos.writeObject(new SAXException("SAXException message").initCause(new Exception("cause field")));
     *  oos.close();
     */
    private static final byte[] JDK8_WITH_INIT_ONLY = {
            -84, -19, 0, 5, 115, 114, 0, 24, 111, 114, 103, 46, 120, 109, 108, 46, 115, 97, 120, 46, 83, 65, 88, 69, 120,
            99, 101, 112, 116, 105, 111, 110, 8, 24, 23, 45, 87, -89, -2, 32, 2, 0, 1, 76, 0, 9, 101, 120, 99, 101, 112,
            116, 105, 111, 110, 116, 0, 21, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 69, 120, 99, 101, 112, 116,
            105, 111, 110, 59, 120, 114, 0, 19, 106, 97, 118, 97, 46, 108, 97, 110, 103, 46, 69, 120, 99, 101, 112, 116,
            105, 111, 110, -48, -3, 31, 62, 26, 59, 28, -60, 2, 0, 0, 120, 114, 0, 19, 106, 97, 118, 97, 46, 108, 97, 110,
            103, 46, 84, 104, 114, 111, 119, 97, 98, 108, 101, -43, -58, 53, 39, 57, 119, -72, -53, 3, 0, 4, 76, 0, 5, 99,
            97, 117, 115, 101, 116, 0, 21, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 84, 104, 114, 111, 119, 97,
            98, 108, 101, 59, 76, 0, 13, 100, 101, 116, 97, 105, 108, 77, 101, 115, 115, 97, 103, 101, 116, 0, 18, 76, 106,
            97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 91, 0, 10, 115, 116, 97, 99, 107, 84,
            114, 97, 99, 101, 116, 0, 30, 91, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 97, 99, 107, 84,
            114, 97, 99, 101, 69, 108, 101, 109, 101, 110, 116, 59, 76, 0, 20, 115, 117, 112, 112, 114, 101, 115, 115,
            101, 100, 69, 120, 99, 101, 112, 116, 105, 111, 110, 115, 116, 0, 16, 76, 106, 97, 118, 97, 47, 117, 116, 105,
            108, 47, 76, 105, 115, 116, 59, 120, 112, 115, 113, 0, 126, 0, 2, 113, 0, 126, 0, 9, 116, 0, 11, 99, 97, 117,
            115, 101, 32, 102, 105, 101, 108, 100, 117, 114, 0, 30, 91, 76, 106, 97, 118, 97, 46, 108, 97, 110, 103, 46,
            83, 116, 97, 99, 107, 84, 114, 97, 99, 101, 69, 108, 101, 109, 101, 110, 116, 59, 2, 70, 42, 60, 60, -3, 34,
            57, 2, 0, 0, 120, 112, 0, 0, 0, 2, 115, 114, 0, 27, 106, 97, 118, 97, 46, 108, 97, 110, 103, 46, 83, 116, 97,
            99, 107, 84, 114, 97, 99, 101, 69, 108, 101, 109, 101, 110, 116, 97, 9, -59, -102, 38, 54, -35, -123, 2, 0, 4,
            73, 0, 10, 108, 105, 110, 101, 78, 117, 109, 98, 101, 114, 76, 0, 14, 100, 101, 99, 108, 97, 114, 105, 110, 103,
            67, 108, 97, 115, 115, 113, 0, 126, 0, 5, 76, 0, 8, 102, 105, 108, 101, 78, 97, 109, 101, 113, 0, 126, 0, 5, 76,
            0, 10, 109, 101, 116, 104, 111, 100, 78, 97, 109, 101, 113, 0, 126, 0, 5, 120, 112, 0, 0, 0, 42, 116, 0, 8, 71,
            101, 110, 101, 114, 97, 116, 101, 116, 0, 13, 71, 101, 110, 101, 114, 97, 116, 101, 46, 106, 97, 118, 97, 116,
            0, 17, 103, 101, 110, 101, 114, 97, 116, 101, 67, 97, 115, 101, 84, 104, 114, 101, 101, 115, 113, 0, 126, 0,
            13, 0, 0, 0, 12, 113, 0, 126, 0, 15, 113, 0, 126, 0, 16, 116, 0, 4, 109, 97, 105, 110, 115, 114, 0, 38, 106,
            97, 118, 97, 46, 117, 116, 105, 108, 46, 67, 111, 108, 108, 101, 99, 116, 105, 111, 110, 115, 36, 85, 110, 109,
            111, 100, 105, 102, 105, 97, 98, 108, 101, 76, 105, 115, 116, -4, 15, 37, 49, -75, -20, -114, 16, 2, 0, 1, 76,
            0, 4, 108, 105, 115, 116, 113, 0, 126, 0, 7, 120, 114, 0, 44, 106, 97, 118, 97, 46, 117, 116, 105, 108, 46, 67,
            111, 108, 108, 101, 99, 116, 105, 111, 110, 115, 36, 85, 110, 109, 111, 100, 105, 102, 105, 97, 98, 108, 101,
            67, 111, 108, 108, 101, 99, 116, 105, 111, 110, 25, 66, 0, -128, -53, 94, -9, 30, 2, 0, 1, 76, 0, 1, 99, 116,
            0, 22, 76, 106, 97, 118, 97, 47, 117, 116, 105, 108, 47, 67, 111, 108, 108, 101, 99, 116, 105, 111, 110, 59,
            120, 112, 115, 114, 0, 19, 106, 97, 118, 97, 46, 117, 116, 105, 108, 46, 65, 114, 114, 97, 121, 76, 105, 115,
            116, 120, -127, -46, 29, -103, -57, 97, -99, 3, 0, 1, 73, 0, 4, 115, 105, 122, 101, 120, 112, 0, 0, 0, 0, 119,
            4, 0, 0, 0, 0, 120, 113, 0, 126, 0, 25, 120, 116, 0, 20, 83, 65, 88, 69, 120, 99, 101, 112, 116, 105, 111, 110,
            32, 109, 101, 115, 115, 97, 103, 101, 117, 113, 0, 126, 0, 11, 0, 0, 0, 2, 115, 113, 0, 126, 0, 13, 0, 0, 0, 42,
            113, 0, 126, 0, 15, 113, 0, 126, 0, 16, 113, 0, 126, 0, 17, 115, 113, 0, 126, 0, 13, 0, 0, 0, 12, 113, 0, 126, 0,
            15, 113, 0, 126, 0, 16, 113, 0, 126, 0, 19, 113, 0, 126, 0, 23, 120, 112
    };

    /*
     *  ByteArrayOutputStream fser = new ByteArrayOutputStream();
     *  ObjectOutputStream oos = new ObjectOutputStream(fser);
     *  oos.writeObject(new SAXException("No cause and exception set"));
     *  oos.close();
     */
    private static final byte[] JDK8_NOTHING_SET = {
            -84, -19, 0, 5, 115, 114, 0, 24, 111, 114, 103, 46, 120, 109, 108, 46, 115, 97, 120, 46, 83, 65, 88, 69, 120,
            99, 101, 112, 116, 105, 111, 110, 8, 24, 23, 45, 87, -89, -2, 32, 2, 0, 1, 76, 0, 9, 101, 120, 99, 101, 112,
            116, 105, 111, 110, 116, 0, 21, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 69, 120, 99, 101, 112, 116,
            105, 111, 110, 59, 120, 114, 0, 19, 106, 97, 118, 97, 46, 108, 97, 110, 103, 46, 69, 120, 99, 101, 112, 116,
            105, 111, 110, -48, -3, 31, 62, 26, 59, 28, -60, 2, 0, 0, 120, 114, 0, 19, 106, 97, 118, 97, 46, 108, 97, 110,
            103, 46, 84, 104, 114, 111, 119, 97, 98, 108, 101, -43, -58, 53, 39, 57, 119, -72, -53, 3, 0, 4, 76, 0, 5, 99,
            97, 117, 115, 101, 116, 0, 21, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 84, 104, 114, 111, 119, 97, 98,
            108, 101, 59, 76, 0, 13, 100, 101, 116, 97, 105, 108, 77, 101, 115, 115, 97, 103, 101, 116, 0, 18, 76, 106, 97,
            118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 91, 0, 10, 115, 116, 97, 99, 107, 84, 114,
            97, 99, 101, 116, 0, 30, 91, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 97, 99, 107, 84, 114,
            97, 99, 101, 69, 108, 101, 109, 101, 110, 116, 59, 76, 0, 20, 115, 117, 112, 112, 114, 101, 115, 115, 101, 100,
            69, 120, 99, 101, 112, 116, 105, 111, 110, 115, 116, 0, 16, 76, 106, 97, 118, 97, 47, 117, 116, 105, 108, 47,
            76, 105, 115, 116, 59, 120, 112, 113, 0, 126, 0, 8, 116, 0, 26, 78, 111, 32, 99, 97, 117, 115, 101, 32, 97, 110,
            100, 32, 101, 120, 99, 101, 112, 116, 105, 111, 110, 32, 115, 101, 116, 117, 114, 0, 30, 91, 76, 106, 97, 118,
            97, 46, 108, 97, 110, 103, 46, 83, 116, 97, 99, 107, 84, 114, 97, 99, 101, 69, 108, 101, 109, 101, 110, 116, 59,
            2, 70, 42, 60, 60, -3, 34, 57, 2, 0, 0, 120, 112, 0, 0, 0, 2, 115, 114, 0, 27, 106, 97, 118, 97, 46, 108, 97,
            110, 103, 46, 83, 116, 97, 99, 107, 84, 114, 97, 99, 101, 69, 108, 101, 109, 101, 110, 116, 97, 9, -59, -102,
            38, 54, -35, -123, 2, 0, 4, 73, 0, 10, 108, 105, 110, 101, 78, 117, 109, 98, 101, 114, 76, 0, 14, 100, 101, 99,
            108, 97, 114, 105, 110, 103, 67, 108, 97, 115, 115, 113, 0, 126, 0, 5, 76, 0, 8, 102, 105, 108, 101, 78, 97,
            109, 101, 113, 0, 126, 0, 5, 76, 0, 10, 109, 101, 116, 104, 111, 100, 78, 97, 109, 101, 113, 0, 126, 0, 5, 120,
            112, 0, 0, 0, 50, 116, 0, 8, 71, 101, 110, 101, 114, 97, 116, 101, 116, 0, 13, 71, 101, 110, 101, 114, 97, 116,
            101, 46, 106, 97, 118, 97, 116, 0, 16, 103, 101, 110, 101, 114, 97, 116, 101, 67, 97, 115, 101, 70, 111, 117,
            114, 115, 113, 0, 126, 0, 12, 0, 0, 0, 13, 113, 0, 126, 0, 14, 113, 0, 126, 0, 15, 116, 0, 4, 109, 97, 105, 110,
            115, 114, 0, 38, 106, 97, 118, 97, 46, 117, 116, 105, 108, 46, 67, 111, 108, 108, 101, 99, 116, 105, 111, 110,
            115, 36, 85, 110, 109, 111, 100, 105, 102, 105, 97, 98, 108, 101, 76, 105, 115, 116, -4, 15, 37, 49, -75, -20,
            -114, 16, 2, 0, 1, 76, 0, 4, 108, 105, 115, 116, 113, 0, 126, 0, 7, 120, 114, 0, 44, 106, 97, 118, 97, 46, 117,
            116, 105, 108, 46, 67, 111, 108, 108, 101, 99, 116, 105, 111, 110, 115, 36, 85, 110, 109, 111, 100, 105, 102,
            105, 97, 98, 108, 101, 67, 111, 108, 108, 101, 99, 116, 105, 111, 110, 25, 66, 0, -128, -53, 94, -9, 30, 2, 0,
            1, 76, 0, 1, 99, 116, 0, 22, 76, 106, 97, 118, 97, 47, 117, 116, 105, 108, 47, 67, 111, 108, 108, 101, 99, 116,
            105, 111, 110, 59, 120, 112, 115, 114, 0, 19, 106, 97, 118, 97, 46, 117, 116, 105, 108, 46, 65, 114, 114, 97,
            121, 76, 105, 115, 116, 120, -127, -46, 29, -103, -57, 97, -99, 3, 0, 1, 73, 0, 4, 115, 105, 122, 101, 120, 112,
            0, 0, 0, 0, 119, 4, 0, 0, 0, 0, 120, 113, 0, 126, 0, 24, 120, 112
    };

    /*
     *  ByteArrayOutputStream fser = new ByteArrayOutputStream();
     *  ObjectOutputStream oos = new ObjectOutputStream(fser);
     *  SAXException se = new SAXException(new Exception());
     *  se.initCause(null);
     *  oos.writeObject(se);
     *  oos.close();
     */
    private static final byte[] JDK8_CHECK_ILLEGAL_STATE_EXCEPTION = {
            -84, -19, 0, 5, 115, 114, 0, 24, 111, 114, 103, 46, 120, 109, 108, 46, 115, 97, 120, 46, 83, 65, 88, 69, 120,
            99, 101, 112, 116, 105, 111, 110, 8, 24, 23, 45, 87, -89, -2, 32, 2, 0, 1, 76, 0, 9, 101, 120, 99, 101, 112,
            116, 105, 111, 110, 116, 0, 21, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 69, 120, 99, 101, 112, 116,
            105, 111, 110, 59, 120, 114, 0, 19, 106, 97, 118, 97, 46, 108, 97, 110, 103, 46, 69, 120, 99, 101, 112, 116,
            105, 111, 110, -48, -3, 31, 62, 26, 59, 28, -60, 2, 0, 0, 120, 114, 0, 19, 106, 97, 118, 97, 46, 108, 97, 110,
            103, 46, 84, 104, 114, 111, 119, 97, 98, 108, 101, -43, -58, 53, 39, 57, 119, -72, -53, 3, 0, 4, 76, 0, 5, 99,
            97, 117, 115, 101, 116, 0, 21, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 84, 104, 114, 111, 119, 97, 98,
            108, 101, 59, 76, 0, 13, 100, 101, 116, 97, 105, 108, 77, 101, 115, 115, 97, 103, 101, 116, 0, 18, 76, 106, 97,
            118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 91, 0, 10, 115, 116, 97, 99, 107, 84, 114,
            97, 99, 101, 116, 0, 30, 91, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 97, 99, 107, 84, 114,
            97, 99, 101, 69, 108, 101, 109, 101, 110, 116, 59, 76, 0, 20, 115, 117, 112, 112, 114, 101, 115, 115, 101, 100,
            69, 120, 99, 101, 112, 116, 105, 111, 110, 115, 116, 0, 16, 76, 106, 97, 118, 97, 47, 117, 116, 105, 108, 47,
            76, 105, 115, 116, 59, 120, 112, 112, 112, 117, 114, 0, 30, 91, 76, 106, 97, 118, 97, 46, 108, 97, 110, 103,
            46, 83, 116, 97, 99, 107, 84, 114, 97, 99, 101, 69, 108, 101, 109, 101, 110, 116, 59, 2, 70, 42, 60, 60, -3,
            34, 57, 2, 0, 0, 120, 112, 0, 0, 0, 2, 115, 114, 0, 27, 106, 97, 118, 97, 46, 108, 97, 110, 103, 46, 83, 116,
            97, 99, 107, 84, 114, 97, 99, 101, 69, 108, 101, 109, 101, 110, 116, 97, 9, -59, -102, 38, 54, -35, -123, 2, 0,
            4, 73, 0, 10, 108, 105, 110, 101, 78, 117, 109, 98, 101, 114, 76, 0, 14, 100, 101, 99, 108, 97, 114, 105, 110,
            103, 67, 108, 97, 115, 115, 113, 0, 126, 0, 5, 76, 0, 8, 102, 105, 108, 101, 78, 97, 109, 101, 113, 0, 126, 0,
            5, 76, 0, 10, 109, 101, 116, 104, 111, 100, 78, 97, 109, 101, 113, 0, 126, 0, 5, 120, 112, 0, 0, 0, 60, 116, 0,
            8, 71, 101, 110, 101, 114, 97, 116, 101, 116, 0, 13, 71, 101, 110, 101, 114, 97, 116, 101, 46, 106, 97, 118,
            97, 116, 0, 16, 103, 101, 110, 101, 114, 97, 116, 101, 67, 97, 115, 101, 70, 105, 118, 101, 115, 113, 0, 126,
            0, 11, 0, 0, 0, 14, 113, 0, 126, 0, 13, 113, 0, 126, 0, 14, 116, 0, 4, 109, 97, 105, 110, 115, 114, 0, 38, 106,
            97, 118, 97, 46, 117, 116, 105, 108, 46, 67, 111, 108, 108, 101, 99, 116, 105, 111, 110, 115, 36, 85, 110, 109,
            111, 100, 105, 102, 105, 97, 98, 108, 101, 76, 105, 115, 116, -4, 15, 37, 49, -75, -20, -114, 16, 2, 0, 1, 76,
            0, 4, 108, 105, 115, 116, 113, 0, 126, 0, 7, 120, 114, 0, 44, 106, 97, 118, 97, 46, 117, 116, 105, 108, 46, 67,
            111, 108, 108, 101, 99, 116, 105, 111, 110, 115, 36, 85, 110, 109, 111, 100, 105, 102, 105, 97, 98, 108, 101,
            67, 111, 108, 108, 101, 99, 116, 105, 111, 110, 25, 66, 0, -128, -53, 94, -9, 30, 2, 0, 1, 76, 0, 1, 99, 116,
            0, 22, 76, 106, 97, 118, 97, 47, 117, 116, 105, 108, 47, 67, 111, 108, 108, 101, 99, 116, 105, 111, 110, 59,
            120, 112, 115, 114, 0, 19, 106, 97, 118, 97, 46, 117, 116, 105, 108, 46, 65, 114, 114, 97, 121, 76, 105, 115,
            116, 120, -127, -46, 29, -103, -57, 97, -99, 3, 0, 1, 73, 0, 4, 115, 105, 122, 101, 120, 112, 0, 0, 0, 0, 119,
            4, 0, 0, 0, 0, 120, 113, 0, 126, 0, 23, 120, 115, 113, 0, 126, 0, 2, 113, 0, 126, 0, 24, 112, 117, 113, 0, 126,
            0, 9, 0, 0, 0, 2, 115, 113, 0, 126, 0, 11, 0, 0, 0, 60, 113, 0, 126, 0, 13, 113, 0, 126, 0, 14, 113, 0, 126, 0,
            15, 115, 113, 0, 126, 0, 11, 0, 0, 0, 14, 113, 0, 126, 0, 13, 113, 0, 126, 0, 14, 113, 0, 126, 0, 17, 113, 0,
            126, 0, 21, 120
    };
}
