/*
 * Copyright (c) 2003, 2018, Oracle and/or its affiliates. All rights reserved.
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

#include <stdio.h>
#include <string.h>
#include "jvmti.h"
#include "agent_common.h"
#include "JVMTITools.h"

#ifdef __cplusplus
extern "C" {
#endif

#ifndef JNI_ENV_ARG

#ifdef __cplusplus
#define JNI_ENV_ARG(x, y) y
#define JNI_ENV_PTR(x) x
#else
#define JNI_ENV_ARG(x,y) x, y
#define JNI_ENV_PTR(x) (*x)
#endif

#endif

#define PASSED 0
#define STATUS_FAILED 2

typedef struct {
    char *cls;
    char *name;
    char *sig;
} frame_info;

static jvmtiEnv *jvmti = NULL;
static jvmtiCapabilities caps;
static jvmtiEventCallbacks callbacks;
static jint result = PASSED;
static jboolean printdump = JNI_FALSE;
static jmethodID mid;
static jbyteArray classBytes;
static frame_info frames[] = {
    {"Lnsk/jvmti/GetStackTrace/getstacktr007$TestThread;", "checkPoint", "()V"},
    {"Lnsk/jvmti/GetStackTrace/getstacktr007$TestThread;", "chain4", "()V"},
    {"Lnsk/jvmti/GetStackTrace/getstacktr007$TestThread;", "chain3", "()V"},
    {"Lnsk/jvmti/GetStackTrace/getstacktr007$TestThread;", "chain2", "()V"},
    {"Lnsk/jvmti/GetStackTrace/getstacktr007$TestThread;", "chain1", "()V"},
    {"Lnsk/jvmti/GetStackTrace/getstacktr007$TestThread;", "run", "()V"},
};

#define NUMBER_OF_FRAMES ((int) (sizeof(frames)/sizeof(frame_info)))

void check(jvmtiEnv *jvmti_env, jthread thr) {
    jvmtiError err;
    jvmtiFrameInfo f[NUMBER_OF_FRAMES + 1];
    jclass callerClass;
    char *sigClass, *name, *sig, *generic;
    jint i, count;

    err = (*jvmti_env)->GetStackTrace(jvmti_env, thr,
        0, NUMBER_OF_FRAMES + 1, f, &count);
    if (err != JVMTI_ERROR_NONE) {
        printf("(GetStackTrace) unexpected error: %s (%d)\n",
               TranslateError(err), err);
        result = STATUS_FAILED;
        return;
    }
    if (count != NUMBER_OF_FRAMES) {
        printf("Wrong frame count, expected: %d, actual: %d\n",
               NUMBER_OF_FRAMES, count);
        result = STATUS_FAILED;
    }

    if (printdump == JNI_TRUE) {
            printf(">>>   frame count: %d\n", count);
    }
    for (i = 0; i < count; i++) {
        if (printdump == JNI_TRUE) {
            printf(">>> checking frame#%d ...\n", i);
        }
        err = (*jvmti_env)->GetMethodDeclaringClass(jvmti_env, f[i].method,
            &callerClass);
        if (err != JVMTI_ERROR_NONE) {
            printf("(GetMethodDeclaringClass#%d) unexpected error: %s (%d)\n",
                   i, TranslateError(err), err);
            result = STATUS_FAILED;
            continue;
        }
        err = (*jvmti_env)->GetClassSignature(jvmti_env, callerClass,
            &sigClass, &generic);
        if (err != JVMTI_ERROR_NONE) {
            printf("(GetClassSignature#%d) unexpected error: %s (%d)\n",
                   i, TranslateError(err), err);
            result = STATUS_FAILED;
            continue;
        }
        err = (*jvmti_env)->GetMethodName(jvmti_env, f[i].method,
            &name, &sig, &generic);
        if (err != JVMTI_ERROR_NONE) {
            printf("(GetMethodName#%d) unexpected error: %s (%d)\n",
                   i, TranslateError(err), err);
            result = STATUS_FAILED;
            continue;
        }
        if (printdump == JNI_TRUE) {
            printf(">>>   class:  \"%s\"\n", sigClass);
            printf(">>>   method: \"%s%s\"\n", name, sig);
        }
        if (i < NUMBER_OF_FRAMES) {
            if (sigClass == NULL || strcmp(sigClass, frames[i].cls) != 0) {
                printf("(frame#%d) wrong class sig: \"%s\", expected: \"%s\"\n",
                       i, sigClass, frames[i].cls);
                result = STATUS_FAILED;
            }
            if (name == NULL || strcmp(name, frames[i].name) != 0) {
                printf("(frame#%d) wrong method name: \"%s\", expected: \"%s\"\n",
                       i, name, frames[i].name);
                result = STATUS_FAILED;
            }
            if (sig == NULL || strcmp(sig, frames[i].sig) != 0) {
                printf("(frame#%d) wrong method sig: \"%s\", expected: \"%s\"\n",
                       i, sig, frames[i].sig);
                result = STATUS_FAILED;
            }
        }
    }
}

void JNICALL Breakpoint(jvmtiEnv *jvmti_env, JNIEnv *env,
        jthread thr, jmethodID method, jlocation location) {
    jvmtiError err;
    jclass klass;
    jvmtiClassDefinition classDef;

    if (mid != method) {
        printf("ERROR: don't know where we get called from");
        result = STATUS_FAILED;
        return;
    }

    if (!caps.can_redefine_classes) {
        printf("Redefine Classes is not implemented\n");
        return;
    }

    if (classBytes == NULL) {
        printf("ERROR: don't have any bytes");
        result = STATUS_FAILED;
        return;
    }

    err = (*jvmti)->GetMethodDeclaringClass(jvmti, method, &klass);
    if (err != JVMTI_ERROR_NONE) {
        printf("(GetMethodDeclaringClass(bp) unexpected error: %s (%d)\n",
               TranslateError(err), err);
        result = STATUS_FAILED;
        return;
    }

    if (printdump == JNI_TRUE) {
        printf(">>> redefining class ...\n");
    }

    classDef.klass = klass;
    classDef.class_byte_count =
        JNI_ENV_PTR(env)->GetArrayLength(JNI_ENV_ARG((JNIEnv *)env, classBytes));
    classDef.class_bytes = (unsigned char*)
        JNI_ENV_PTR(env)->GetByteArrayElements(JNI_ENV_ARG((JNIEnv *)env,
            classBytes), NULL);

    err = (*jvmti)->RedefineClasses(jvmti, 1, &classDef);
    if (err != JVMTI_ERROR_NONE) {
        printf("(RedefineClasses) unexpected error: %s (%d)\n",
               TranslateError(err), err);
        result = STATUS_FAILED;
        return;
    }
    JNI_ENV_PTR(env)->DeleteGlobalRef(JNI_ENV_ARG((JNIEnv *)env, classBytes));
    classBytes = NULL;

    check(jvmti_env, thr);
}

#ifdef STATIC_BUILD
JNIEXPORT jint JNICALL Agent_OnLoad_getstacktr007(JavaVM *jvm, char *options, void *reserved) {
    return Agent_Initialize(jvm, options, reserved);
}
JNIEXPORT jint JNICALL Agent_OnAttach_getstacktr007(JavaVM *jvm, char *options, void *reserved) {
    return Agent_Initialize(jvm, options, reserved);
}
JNIEXPORT jint JNI_OnLoad_getstacktr007(JavaVM *jvm, char *options, void *reserved) {
    return JNI_VERSION_1_8;
}
#endif
jint Agent_Initialize(JavaVM *jvm, char *options, void *reserved) {
    jvmtiError err;
    jint res;

    if (options != NULL && strcmp(options, "printdump") == 0) {
        printdump = JNI_TRUE;
    }

    res = JNI_ENV_PTR(jvm)->GetEnv(JNI_ENV_ARG(jvm, (void **) &jvmti),
        JVMTI_VERSION_1_1);
    if (res != JNI_OK || jvmti == NULL) {
        printf("Wrong result of a valid call to GetEnv!\n");
        return JNI_ERR;
    }

    err = (*jvmti)->GetPotentialCapabilities(jvmti, &caps);
    if (err != JVMTI_ERROR_NONE) {
        printf("(GetPotentialCapabilities) unexpected error: %s (%d)\n",
               TranslateError(err), err);
        return JNI_ERR;
    }

    err = (*jvmti)->AddCapabilities(jvmti, &caps);
    if (err != JVMTI_ERROR_NONE) {
        printf("(AddCapabilities) unexpected error: %s (%d)\n",
               TranslateError(err), err);
        return JNI_ERR;
    }

    err = (*jvmti)->GetCapabilities(jvmti, &caps);
    if (err != JVMTI_ERROR_NONE) {
        printf("(GetCapabilities) unexpected error: %s (%d)\n",
               TranslateError(err), err);
        return JNI_ERR;
    }

    if (caps.can_generate_breakpoint_events) {
        callbacks.Breakpoint = &Breakpoint;
        err = (*jvmti)->SetEventCallbacks(jvmti, &callbacks, sizeof(callbacks));
        if (err != JVMTI_ERROR_NONE) {
            printf("(SetEventCallbacks) unexpected error: %s (%d)\n",
                   TranslateError(err), err);
            return JNI_ERR;
        }
    } else {
        printf("Warning: Breakpoint event is not implemented\n");
    }

    return JNI_OK;
}

JNIEXPORT void JNICALL
Java_nsk_jvmti_GetStackTrace_getstacktr007_getReady(JNIEnv *env, jclass cls,
                           jclass clazz, jbyteArray bytes) {
    jvmtiError err;

    if (jvmti == NULL) {
        printf("JVMTI client was not properly loaded!\n");
        result = STATUS_FAILED;
        return;
    }

    if (!caps.can_generate_breakpoint_events) {
        return;
    }

    classBytes = JNI_ENV_PTR(env)->NewGlobalRef(JNI_ENV_ARG(env, bytes));

    mid = JNI_ENV_PTR(env)->GetMethodID(JNI_ENV_ARG(env, clazz),
         "checkPoint", "()V");
    if (mid == NULL) {
        printf("Cannot find Method ID for method checkPoint\n");
        result = STATUS_FAILED;
        return;
    }

    err = (*jvmti)->SetBreakpoint(jvmti, mid, 0);
    if (err != JVMTI_ERROR_NONE) {
        printf("(SetBreakpoint) unexpected error: %s (%d)\n",
               TranslateError(err), err);
        result = STATUS_FAILED;
        return;
    }

    err = (*jvmti)->SetEventNotificationMode(jvmti, JVMTI_ENABLE,
        JVMTI_EVENT_BREAKPOINT, NULL);
    if (err != JVMTI_ERROR_NONE) {
        printf("Failed to enable BREAKPOINT event: %s (%d)\n",
               TranslateError(err), err);
        result = STATUS_FAILED;
    }
}

JNIEXPORT jint JNICALL
Java_nsk_jvmti_GetStackTrace_getstacktr007_getRes(JNIEnv *env, jclass cls) {
    return result;
}

#ifdef __cplusplus
}
#endif
