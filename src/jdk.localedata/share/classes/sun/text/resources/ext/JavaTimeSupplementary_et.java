/*
 * Copyright (c) 2013, 2016, Oracle and/or its affiliates. All rights reserved.
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

/*
 * COPYRIGHT AND PERMISSION NOTICE
 *
 * Copyright (C) 1991-2016 Unicode, Inc. All rights reserved.
 * Distributed under the Terms of Use in
 * http://www.unicode.org/copyright.html.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of the Unicode data files and any associated documentation
 * (the "Data Files") or Unicode software and any associated documentation
 * (the "Software") to deal in the Data Files or Software
 * without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, and/or sell copies of
 * the Data Files or Software, and to permit persons to whom the Data Files
 * or Software are furnished to do so, provided that
 * (a) this copyright and permission notice appear with all copies
 * of the Data Files or Software,
 * (b) this copyright and permission notice appear in associated
 * documentation, and
 * (c) there is clear notice in each modified Data File or in the Software
 * as well as in the documentation associated with the Data File(s) or
 * Software that the data or software has been modified.
 *
 * THE DATA FILES AND SOFTWARE ARE PROVIDED "AS IS", WITHOUT WARRANTY OF
 * ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT OF THIRD PARTY RIGHTS.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR HOLDERS INCLUDED IN THIS
 * NOTICE BE LIABLE FOR ANY CLAIM, OR ANY SPECIAL INDIRECT OR CONSEQUENTIAL
 * DAMAGES, OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE,
 * DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER
 * TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
 * PERFORMANCE OF THE DATA FILES OR SOFTWARE.
 *
 * Except as contained in this notice, the name of a copyright holder
 * shall not be used in advertising or otherwise to promote the sale,
 * use or other dealings in these Data Files or Software without prior
 * written authorization of the copyright holder.
 */

//  Note: this file has been generated by a tool.

package sun.text.resources.ext;

import sun.util.resources.OpenListResourceBundle;

public class JavaTimeSupplementary_et extends OpenListResourceBundle {
    @Override
    protected final Object[][] getContents() {
        final String[] sharedQuarterAbbreviations = {
            "K1",
            "K2",
            "K3",
            "K4",
        };

        final String[] sharedQuarterNames = {
            "1. kvartal",
            "2. kvartal",
            "3. kvartal",
            "4. kvartal",
        };

        final String[] sharedDatePatterns = {
            "EEEE, d. MMMM y GGGG",
            "d. MMMM y GGGG",
            "dd.MM.y GGGG",
            "dd.MM.y G",
        };

        final String[] sharedDayNarrows = {
            "P",
            "E",
            "T",
            "K",
            "N",
            "R",
            "L",
        };

        final String[] sharedDayNames = {
            "p\u00fchap\u00e4ev",
            "esmasp\u00e4ev",
            "teisip\u00e4ev",
            "kolmap\u00e4ev",
            "neljap\u00e4ev",
            "reede",
            "laup\u00e4ev",
        };

        final String[] sharedTimePatterns = {
            "H:mm.ss zzzz",
            "H:mm.ss z",
            "H:mm.ss",
            "H:mm",
        };

        final String[] sharedNarrowAmPmMarkers = {
            "a",
            "p",
        };

        final String[] sharedJavaTimeDatePatterns = {
            "EEEE, d. MMMM y G",
            "d. MMMM y G",
            "dd.MM.y G",
            "dd.MM.y GGGGG",
        };

        return new Object[][] {
            { "QuarterAbbreviations",
                sharedQuarterAbbreviations },
            { "QuarterNames",
                sharedQuarterNames },
            { "calendarname.buddhist",
                "budistlik kalender" },
            { "calendarname.gregorian",
                "Gregoriuse kalender" },
            { "calendarname.gregory",
                "Gregoriuse kalender" },
            { "calendarname.islamic",
                "islamikalender" },
            { "calendarname.islamic-civil",
                "islami ilmalik kalender" },
            { "calendarname.japanese",
                "Jaapani kalender" },
            { "calendarname.roc",
                "Hiina Vabariigi kalender" },
            { "field.dayperiod",
                "enne/p\u00e4rast l\u00f5unat" },
            { "field.era",
                "ajastu" },
            { "field.hour",
                "tund" },
            { "field.minute",
                "minut" },
            { "field.month",
                "kuu" },
            { "field.second",
                "sekund" },
            { "field.week",
                "n\u00e4dal" },
            { "field.weekday",
                "n\u00e4dalap\u00e4ev" },
            { "field.year",
                "aasta" },
            { "field.zone",
                "ajav\u00f6\u00f6nd" },
            { "islamic.AmPmMarkers",
                new String[] {
                    "AM",
                    "PM",
                }
            },
            { "islamic.DatePatterns",
                sharedDatePatterns },
            { "islamic.DayAbbreviations",
                sharedDayNarrows },
            { "islamic.DayNames",
                sharedDayNames },
            { "islamic.DayNarrows",
                sharedDayNarrows },
            { "islamic.QuarterAbbreviations",
                sharedQuarterAbbreviations },
            { "islamic.QuarterNames",
                sharedQuarterNames },
            { "islamic.TimePatterns",
                sharedTimePatterns },
            { "islamic.narrow.AmPmMarkers",
                sharedNarrowAmPmMarkers },
            { "java.time.buddhist.DatePatterns",
                sharedJavaTimeDatePatterns },
            { "java.time.islamic.DatePatterns",
                sharedJavaTimeDatePatterns },
            { "java.time.japanese.DatePatterns",
                sharedJavaTimeDatePatterns },
            { "java.time.long.Eras",
                new String[] {
                    "enne Kristust",
                    "p\u00e4rast Kristust",
                }
            },
            { "java.time.roc.DatePatterns",
                sharedJavaTimeDatePatterns },
            { "java.time.short.Eras",
                new String[] {
                    "e.m.a.",
                    "m.a.j.",
                }
            },
            { "roc.DatePatterns",
                sharedDatePatterns },
            { "roc.DayAbbreviations",
                sharedDayNarrows },
            { "roc.DayNames",
                sharedDayNames },
            { "roc.DayNarrows",
                sharedDayNarrows },
            { "roc.MonthAbbreviations",
                new String[] {
                    "jaan",
                    "veebr",
                    "m\u00e4rts",
                    "apr",
                    "mai",
                    "juuni",
                    "juuli",
                    "aug",
                    "sept",
                    "okt",
                    "nov",
                    "dets",
                    "",
                }
            },
            { "roc.MonthNames",
                new String[] {
                    "jaanuar",
                    "veebruar",
                    "m\u00e4rts",
                    "aprill",
                    "mai",
                    "juuni",
                    "juuli",
                    "august",
                    "september",
                    "oktoober",
                    "november",
                    "detsember",
                    "",
                }
            },
            { "roc.MonthNarrows",
                new String[] {
                    "J",
                    "V",
                    "M",
                    "A",
                    "M",
                    "J",
                    "J",
                    "A",
                    "S",
                    "O",
                    "N",
                    "D",
                    "",
                }
            },
            { "roc.QuarterAbbreviations",
                sharedQuarterAbbreviations },
            { "roc.QuarterNames",
                sharedQuarterNames },
            { "roc.TimePatterns",
                sharedTimePatterns },
            { "roc.narrow.AmPmMarkers",
                sharedNarrowAmPmMarkers },
            { "timezone.hourFormat",
                "+HH:mm;\u2212HH:mm" },
        };
    }
}
