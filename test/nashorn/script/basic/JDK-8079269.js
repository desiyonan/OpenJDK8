/*
 * Copyright (c) 2015, Oracle and/or its affiliates. All rights reserved.
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
 * JDK-8079269: Optimistic rewrite in object literal causes ArrayIndexOutOfBoundsException
 *
 * @test
 * @run
 */

// m must be in scope so it's accessed with optimistic getters on scope
var m = 1; 

(function() {
    return { 
        p0: m, 
        p1: m = "foo",
        p2: m
    }
})();

var n = 1; 

// Test the spill object creator too
(function() {
    return { 
        p0: n, 
        p1: n = "foo",
        p2: n,
        p3: n,
        p4: n,
        p5: n,
        p6: n,
        p7: n,
        p8: n,
        p9: n,
        p10: n,
        p11: n,
        p12: n,
        p13: n,
        p14: n,
        p15: n,
        p16: n,
        p17: n,
        p18: n,
        p19: n,
        p20: n,
        p21: n,
        p22: n,
        p23: n,
        p24: n,
        p25: n,
        p26: n,
        p27: n,
        p28: n,
        p29: n,
        p30: n,
        p31: n,
        p32: n,
        p33: n,
        p34: n,
        p35: n,
        p36: n,
        p37: n,
        p38: n,
        p39: n,
        p40: n,
        p41: n,
        p42: n,
        p43: n,
        p44: n,
        p45: n,
        p46: n,
        p47: n,
        p48: n,
        p49: n,
        p50: n,
        p51: n,
        p52: n,
        p53: n,
        p54: n,
        p55: n,
        p56: n,
        p57: n,
        p58: n,
        p59: n,
        p60: n,
        p61: n,
        p62: n,
        p63: n,
        p64: n,
        p65: n,
        p66: n,
        p67: n,
        p68: n,
        p69: n,
        p70: n,
        p71: n,
        p72: n,
        p73: n,
        p74: n,
        p75: n,
        p76: n,
        p77: n,
        p78: n,
        p79: n,
        p80: n,
        p81: n,
        p82: n,
        p83: n,
        p84: n,
        p85: n,
        p86: n,
        p87: n,
        p88: n,
        p89: n,
        p90: n,
        p91: n,
        p92: n,
        p93: n,
        p94: n,
        p95: n,
        p96: n,
        p97: n,
        p98: n,
        p99: n,
        p100: n,
        p101: n,
        p102: n,
        p103: n,
        p104: n,
        p105: n,
        p106: n,
        p107: n,
        p108: n,
        p109: n,
        p110: n,
        p111: n,
        p112: n,
        p113: n,
        p114: n,
        p115: n,
        p116: n,
        p117: n,
        p118: n,
        p119: n,
        p120: n,
        p121: n,
        p122: n,
        p123: n,
        p124: n,
        p125: n,
        p126: n,
        p127: n,
        p128: n,
        p129: n,
        p130: n,
        p131: n,
        p132: n,
        p133: n,
        p134: n,
        p135: n,
        p136: n,
        p137: n,
        p138: n,
        p139: n,
        p140: n,
        p141: n,
        p142: n,
        p143: n,
        p144: n,
        p145: n,
        p146: n,
        p147: n,
        p148: n,
        p149: n,
        p150: n,
        p151: n,
        p152: n,
        p153: n,
        p154: n,
        p155: n,
        p156: n,
        p157: n,
        p158: n,
        p159: n,
        p160: n,
        p161: n,
        p162: n,
        p163: n,
        p164: n,
        p165: n,
        p166: n,
        p167: n,
        p168: n,
        p169: n,
        p170: n,
        p171: n,
        p172: n,
        p173: n,
        p174: n,
        p175: n,
        p176: n,
        p177: n,
        p178: n,
        p179: n,
        p180: n,
        p181: n,
        p182: n,
        p183: n,
        p184: n,
        p185: n,
        p186: n,
        p187: n,
        p188: n,
        p189: n,
        p190: n,
        p191: n,
        p192: n,
        p193: n,
        p194: n,
        p195: n,
        p196: n,
        p197: n,
        p198: n,
        p199: n,
        p200: n,
        p201: n,
        p202: n,
        p203: n,
        p204: n,
        p205: n,
        p206: n,
        p207: n,
        p208: n,
        p209: n,
        p210: n,
        p211: n,
        p212: n,
        p213: n,
        p214: n,
        p215: n,
        p216: n,
        p217: n,
        p218: n,
        p219: n,
        p220: n,
        p221: n,
        p222: n,
        p223: n,
        p224: n,
        p225: n,
        p226: n,
        p227: n,
        p228: n,
        p229: n,
        p230: n,
        p231: n,
        p232: n,
        p233: n,
        p234: n,
        p235: n,
        p236: n,
        p237: n,
        p238: n,
        p239: n,
        p240: n,
        p241: n,
        p242: n,
        p243: n,
        p244: n,
        p245: n,
        p246: n,
        p247: n,
        p248: n,
        p249: n,
        p250: n,
        p251: n,
        p252: n,
        p253: n,
        p254: n,
        p255: n,
        p256: n,
        p257: n,
        p258: n,
        p259: n
    }
})();

// No output; as long as it completes without
// ArrayIndexOutOfBoundsException in the OSR continuation handler, it's
// a success.
