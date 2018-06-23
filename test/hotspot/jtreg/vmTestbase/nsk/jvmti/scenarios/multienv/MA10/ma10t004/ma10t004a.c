/*
 * Copyright (c) 2004, 2018, Oracle and/or its affiliates. All rights reserved.
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

#include <stdlib.h>
#include <string.h>
#include "jni_tools.h"
#include "agent_common.h"
#include "jvmti_tools.h"

#define PASSED 0
#define STATUS_FAILED 2

#ifdef __cplusplus
extern "C" {
#endif

/* ========================================================================== */

/* scaffold objects */
static jlong timeout = 0;

/* event counts */
static int SingleStepEventsCount = 0;

/* ========================================================================== */

/** callback functions **/

static void JNICALL
SingleStep(jvmtiEnv *jvmti_env, JNIEnv* jni_env, jthread thread,
        jmethodID method, jlocation location) {
    char *name = NULL;
    char *signature = NULL;
    char buffer[32];

    SingleStepEventsCount++;

    NSK_JVMTI_VERIFY(NSK_CPP_STUB4(SetEventNotificationMode,
        jvmti_env, JVMTI_DISABLE, JVMTI_EVENT_SINGLE_STEP, NULL));

    if (!NSK_JVMTI_VERIFY(NSK_CPP_STUB5(GetMethodName,
            jvmti_env, method, &name, &signature, NULL))) {
        nsk_jvmti_setFailStatus();
        return;
    }
    NSK_DISPLAY3("SingleStep event: %s%s, location=%s\n", name, signature,
        jlong_to_string(location, buffer));
    if (name != NULL)
        NSK_CPP_STUB2(Deallocate, jvmti_env, (unsigned char*)name);
    if (signature != NULL)
        NSK_CPP_STUB2(Deallocate, jvmti_env, (unsigned char*)signature);
}

/* ========================================================================== */

/** Agent algorithm. */
static void JNICALL
agentProc(jvmtiEnv* jvmti, JNIEnv* jni, void* arg) {

    if (!nsk_jvmti_waitForSync(timeout))
        return;

    /* resume debugee and wait for sync */
    if (!nsk_jvmti_resumeSync())
        return;
    if (!nsk_jvmti_waitForSync(timeout))
        return;

    NSK_DISPLAY1("SingleStep events received: %d\n",
        SingleStepEventsCount);
    if (!NSK_VERIFY(SingleStepEventsCount == 0))
        nsk_jvmti_setFailStatus();

    if (!nsk_jvmti_resumeSync())
        return;
}

/* ========================================================================== */

/** Agent library initialization. */
#ifdef STATIC_BUILD
JNIEXPORT jint JNICALL Agent_OnLoad_ma10t004a(JavaVM *jvm, char *options, void *reserved) {
    return Agent_Initialize(jvm, options, reserved);
}
JNIEXPORT jint JNICALL Agent_OnAttach_ma10t004a(JavaVM *jvm, char *options, void *reserved) {
    return Agent_Initialize(jvm, options, reserved);
}
JNIEXPORT jint JNI_OnLoad_ma10t004a(JavaVM *jvm, char *options, void *reserved) {
    return JNI_VERSION_1_8;
}
#endif
jint Agent_Initialize(JavaVM *jvm, char *options, void *reserved) {
    jvmtiEnv* jvmti = NULL;
    jvmtiCapabilities caps;
    jvmtiEventCallbacks callbacks;

    NSK_DISPLAY0("Agent_OnLoad\n");

    if (!NSK_VERIFY(nsk_jvmti_parseOptions(options)))
        return JNI_ERR;

    timeout = nsk_jvmti_getWaitTime() * 60 * 1000;

    if (!NSK_VERIFY((jvmti =
            nsk_jvmti_createJVMTIEnv(jvm, reserved)) != NULL))
        return JNI_ERR;

    if (!NSK_VERIFY(nsk_jvmti_setAgentProc(agentProc, NULL)))
        return JNI_ERR;

    memset(&caps, 0, sizeof(caps));
    caps.can_generate_single_step_events = 1;
    if (!NSK_JVMTI_VERIFY(NSK_CPP_STUB2(AddCapabilities, jvmti, &caps))) {
        return JNI_ERR;
    }

    memset(&callbacks, 0, sizeof(callbacks));
    callbacks.SingleStep = &SingleStep;
    if (!NSK_VERIFY(nsk_jvmti_init_MA(&callbacks)))
        return JNI_ERR;

    return JNI_OK;
}

/* ========================================================================== */

#ifdef __cplusplus
}
#endif
