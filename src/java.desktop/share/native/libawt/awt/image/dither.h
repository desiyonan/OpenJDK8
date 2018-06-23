/*
 * Copyright (c) 2001, 2018, Oracle and/or its affiliates. All rights reserved.
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

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>

#include "colordata.h"
#include "jni.h"

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT extern sgn_ordered_dither_array std_img_oda_red;
JNIEXPORT extern sgn_ordered_dither_array std_img_oda_green;
JNIEXPORT extern sgn_ordered_dither_array std_img_oda_blue;
JNIEXPORT extern int std_odas_computed;

JNIEXPORT void JNICALL
make_dither_arrays(int cmapsize, ColorData *cData);

JNIEXPORT void JNICALL
initInverseGrayLut(int* prgb, int rgbsize, ColorData* cData);

/*
 * state info needed for breadth-first recursion of color cube from
 * initial palette entries within the cube
 */

typedef struct {
    unsigned int depth;
    unsigned int maxDepth;

    unsigned char *usedFlags;
    unsigned int  activeEntries;
    unsigned short *rgb;
    unsigned char *indices;
    unsigned char *iLUT;
} CubeStateInfo;

#define INSERTNEW(state, rgb, index) do {                           \
        if (!state.usedFlags[rgb]) {                                \
            state.usedFlags[rgb] = 1;                               \
            state.iLUT[rgb] = index;                                \
            state.rgb[state.activeEntries] = rgb;                   \
            state.indices[state.activeEntries] = index;             \
            state.activeEntries++;                                  \
        }                                                           \
} while (0);


#define ACTIVATE(code, mask, delta, state, index) do {              \
    if (((rgb & mask) + delta) <= mask) {                           \
        rgb += delta;                                               \
        INSERTNEW(state, rgb, index);                               \
        rgb -= delta;                                               \
    }                                                               \
    if ((rgb & mask) >= delta) {                                    \
        rgb -= delta;                                               \
        INSERTNEW(state, rgb, index);                               \
        rgb += delta;                                               \
    }                                                               \
} while (0);

#ifdef __cplusplus
} /* extern "C" */
#endif
