/*
 * Copyright (c) 2001, 2018, Oracle and/or its affiliates. All rights reserved.
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

package nsk.jdi.StepRequest.depth;

import nsk.share.*;
import nsk.share.jpda.*;
import nsk.share.jdi.*;

import com.sun.jdi.*;
import com.sun.jdi.event.*;
import com.sun.jdi.request.*;

import java.util.*;
import java.io.*;

/**
 * The test for the implementation of an object of the type     <BR>
 * StepRequest.                                                 <BR>
 *                                                              <BR>
 * The test checks that results of the method                   <BR>
 * <code>com.sun.jdi.StepRequest.depth()</code>                 <BR>
 * complies with its spec.                                      <BR>
 * <BR>
 * The test checks if an int value StepRequest.STEP_INTO,               <BR>
 * an argument of the method                                            <BR>
 *    EventRequestManager.createAccessWatchpointRequest(),              <BR>
 * is equal to one returned by the method StepRequest.depth()           <BR>
 * <BR>
 * The test has three phases and works as follows.                      <BR>
 * <BR>
 * In first phase,                                                      <BR>
 * upon launching debuggee's VM which will be suspended,                <BR>
 * a debugger waits for the VMStartEvent within a predefined            <BR>
 * time interval. If no the VMStartEvent received, the test is FAILED.  <BR>
 * Upon getting the VMStartEvent, it makes the request for debuggee's   <BR>
 * ClassPrepareEvent with SUSPEND_EVENT_THREAD, resumes the VM,         <BR>
 * and waits for the event within the predefined time interval.         <BR>
 * If no the ClassPrepareEvent received, the test is FAILED.            <BR>
 * Upon getting the ClassPrepareEvent,                                  <BR>
 * the debugger sets up the breakpoint with SUSPEND_EVENT_THREAD        <BR>
 * within debuggee's special methodForCommunication().                  <BR>
 * <BR>
 * In second phase to check the assertion,                              <BR>
 * the debugger and the debuggee perform the following.                 <BR>
 * - The debugger resumes the debuggee and waits for the BreakpointEvent.<BR>
 * - The debuggee creates a tested thread                               <BR>
 *   and invokes the methodForCommunication to be suspended and         <BR>
 *   to inform the debugger with the event.                             <BR>
 * - Upon getting the BreakpointEvent, the debugger                     <BR>
 *   - creates an StepRequest with the depth StepRequest.STEP_INTO,     <BR>
 *   - gets  the depth of the Request with the method StepRequest.depth(),<BR>
 *   - and compares the values.                                         <BR>
 * <BR>
 * In third phase, at the end of the test, the debuggee changes         <BR>
 * the value of the "instruction" which the debugger and debuggee       <BR>
 * use to inform each other of needed actions,  and both end.           <BR>
 * <BR>
 */

public class depth001 {

    //----------------------------------------------------- templete section
    static final int PASSED = 0;
    static final int FAILED = 2;
    static final int PASS_BASE = 95;

    //----------------------------------------------------- templete parameters
    static final String
    sHeader1 = "\n==> nsk/jdi/StepRequest/depth/depth001 ",
    sHeader2 = "--> debugger: ",
    sHeader3 = "##> debugger: ";

    //----------------------------------------------------- main method

    public static void main (String argv[]) {

        int result = run(argv, System.out);

        System.exit(result + PASS_BASE);
    }

    public static int run (String argv[], PrintStream out) {

        int exitCode = new depth001().runThis(argv, out);

        if (exitCode != PASSED) {
            System.out.println("TEST FAILED");
        }
        return testExitCode;
    }

    //--------------------------------------------------   log procedures

    private static Log  logHandler;

    private static void log1(String message) {
        logHandler.display(sHeader1 + message);
    }
    private static void log2(String message) {
        logHandler.display(sHeader2 + message);
    }
    private static void log3(String message) {
        logHandler.complain(sHeader3 + message);
    }

    //  ************************************************    test parameters

    private String debuggeeName =
        "nsk.jdi.StepRequest.depth.depth001a";

    private String testedClassName =
      "nsk.jdi.StepRequest.depth.TestClass10";

    //====================================================== test program
    //------------------------------------------------------ common section

    static Debugee          debuggee;
    static ArgumentHandler  argsHandler;

    static int waitTime;

    static VirtualMachine      vm            = null;
    static EventRequestManager eventRManager = null;
    static EventQueue          eventQueue    = null;
    static EventSet            eventSet      = null;
    static EventIterator       eventIterator = null;

    static ReferenceType       debuggeeClass = null;

    static int  testExitCode = PASSED;


    class JDITestRuntimeException extends RuntimeException {
        JDITestRuntimeException(String str) {
            super("JDITestRuntimeException : " + str);
        }
    }

    //------------------------------------------------------ methods

    private int runThis (String argv[], PrintStream out) {

        argsHandler     = new ArgumentHandler(argv);
        logHandler      = new Log(out, argsHandler);
        Binder binder   = new Binder(argsHandler, logHandler);

        waitTime        = argsHandler.getWaitTime() * 60000;

        try {
            log2("launching a debuggee :");
            log2("       " + debuggeeName);
            if (argsHandler.verbose()) {
                debuggee = binder.bindToDebugee(debuggeeName + " -vbs");
            } else {
                debuggee = binder.bindToDebugee(debuggeeName);
            }
            if (debuggee == null) {
                log3("ERROR: no debuggee launched");
                return FAILED;
            }
            log2("debuggee launched");
        } catch ( Exception e ) {
            log3("ERROR: Exception : " + e);
            log2("       test cancelled");
            return FAILED;
        }

        debuggee.redirectOutput(logHandler);

        vm = debuggee.VM();

        eventQueue = vm.eventQueue();
        if (eventQueue == null) {
            log3("ERROR: eventQueue == null : TEST ABORTED");
            vm.exit(PASS_BASE);
            return FAILED;
        }

        log2("invocation of the method runTest()");
        switch (runTest()) {

            case 0 :  log2("test phase has finished normally");
                      log2("   waiting for the debuggee to finish ...");
                      debuggee.waitFor();

                      log2("......getting the debuggee's exit status");
                      int status = debuggee.getStatus();
                      if (status != PASS_BASE) {
                          log3("ERROR: debuggee returned UNEXPECTED exit status: " +
                              status + " != PASS_BASE");
                          testExitCode = FAILED;
                      } else {
                          log2("......debuggee returned expected exit status: " +
                              status + " == PASS_BASE");
                      }
                      break;

            default : log3("ERROR: runTest() returned unexpected value");

            case 1 :  log3("test phase has not finished normally: debuggee is still alive");
                      log2("......forcing: vm.exit();");
                      testExitCode = FAILED;
                      try {
                          vm.exit(PASS_BASE);
                      } catch ( Exception e ) {
                          log3("ERROR: Exception : " + e);
                      }
                      break;

            case 2 :  log3("test cancelled due to VMDisconnectedException");
                      log2("......trying: vm.process().destroy();");
                      testExitCode = FAILED;
                      try {
                          Process vmProcess = vm.process();
                          if (vmProcess != null) {
                              vmProcess.destroy();
                          }
                      } catch ( Exception e ) {
                          log3("ERROR: Exception : " + e);
                      }
                      break;
            }

        return testExitCode;
    }


   /*
    * Return value: 0 - normal end of the test
    *               1 - ubnormal end of the test
    *               2 - VMDisconnectedException while test phase
    */

    private int runTest() {

        try {
            testRun();

            log2("waiting for VMDeathEvent");
            getEventSet();
            if (eventIterator.nextEvent() instanceof VMDeathEvent)
                return 0;

            log3("ERROR: last event is not the VMDeathEvent");
            return 1;
        } catch ( VMDisconnectedException e ) {
            log3("ERROR: VMDisconnectedException : " + e);
            return 2;
        } catch ( Exception e ) {
            log3("ERROR: Exception : " + e);
            return 1;
        }

    }

    private void testRun()
                 throws JDITestRuntimeException, Exception {

        eventRManager = vm.eventRequestManager();

        ClassPrepareRequest cpRequest = eventRManager.createClassPrepareRequest();
        cpRequest.setSuspendPolicy( EventRequest.SUSPEND_EVENT_THREAD);
        cpRequest.addClassFilter(debuggeeName);

        cpRequest.enable();
        vm.resume();
        getEventSet();
        cpRequest.disable();

        ClassPrepareEvent event = (ClassPrepareEvent) eventIterator.next();
        debuggeeClass = event.referenceType();

        if (!debuggeeClass.name().equals(debuggeeName))
           throw new JDITestRuntimeException("** Unexpected ClassName for ClassPrepareEvent **");

        log2("      received: ClassPrepareEvent for debuggeeClass");

        String bPointMethod = "methodForCommunication";
        String lineForComm  = "lineForComm";

        ThreadReference   mainThread = threadByName("main");

        BreakpointRequest bpRequest = settingBreakpoint(mainThread,
                                             debuggeeClass,
                                            bPointMethod, lineForComm, "zero");
        bpRequest.enable();

    //------------------------------------------------------  testing section

        log1("     TESTING BEGINS");

        EventRequest  eventRequest1 = null;

        ThreadReference thread1     = null;
        String          threadName1 = "thread1";

        int depth;


        for (int i = 0; ; i++) {

            vm.resume();
            breakpointForCommunication();

            int instruction = ((IntegerValue)
                               (debuggeeClass.getValue(debuggeeClass.fieldByName("instruction")))).value();

            if (instruction == 0) {
                vm.resume();
                break;
            }


            log1(":::::: case: # " + i);

            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ variable part

            switch (i) {

              case 0:
                     thread1 = threadByName(threadName1);

                     log2("......setting up StepRequest with depth StepRequest.STEP_INTO");
                     eventRequest1 = setting24StepRequest(thread1, StepRequest.STEP_LINE,
                                              StepRequest.STEP_INTO);

                     log2("......getting: depth = ((StepRequest) eventRequest1).depth();");
                     depth = ((StepRequest) eventRequest1).depth();

                     log2("      checking up on equality of values");
                     if ( depth != StepRequest.STEP_INTO ) {
                         testExitCode = FAILED;
                         log3("ERROR: depth() != StepRequest.STEP_INTO : " + depth);
                     }

                      break;

              default:
                      throw new JDITestRuntimeException("** default case 2 **");
            }

            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        }
        log1("    TESTING ENDS");
        return;
    }

    private ThreadReference threadByName(String name)
                 throws JDITestRuntimeException {

        List         all = vm.allThreads();
        ListIterator li  = all.listIterator();

        for (; li.hasNext(); ) {
            ThreadReference thread = (ThreadReference) li.next();
            if (thread.name().equals(name))
                return thread;
        }
        throw new JDITestRuntimeException("** Thread IS NOT found ** : " + name);
    }

   /*
    * private BreakpointRequest settingBreakpoint(ThreadReference, ReferenceType,
    *                                             String, String, String)
    *
    * It sets up a breakpoint at given line number within a given method in a given class
    * for a given thread.
    *
    * Return value: BreakpointRequest object  in case of success
    *
    * JDITestRuntimeException   in case of an Exception thrown within the method
    */

    private BreakpointRequest settingBreakpoint ( ThreadReference thread,
                                                  ReferenceType testedClass,
                                                  String methodName,
                                                  String bpLine,
                                                  String property)
            throws JDITestRuntimeException {

        log2("......setting up a breakpoint:");
        log2("       thread: " + thread + "; class: " + testedClass +
                        "; method: " + methodName + "; line: " + bpLine);

        List              alllineLocations = null;
        Location          lineLocation     = null;
        BreakpointRequest breakpRequest    = null;

        try {
            Method  method  = (Method) testedClass.methodsByName(methodName).get(0);

            alllineLocations = method.allLineLocations();

            int n =
                ( (IntegerValue) testedClass.getValue(testedClass.fieldByName(bpLine) ) ).value();
            if (n > alllineLocations.size()) {
                log3("ERROR:  TEST_ERROR_IN_settingBreakpoint(): number is out of bound of method's lines");
            } else {
                lineLocation = (Location) alllineLocations.get(n);
                try {
                    breakpRequest = eventRManager.createBreakpointRequest(lineLocation);
                    breakpRequest.putProperty("number", property);
                    breakpRequest.addThreadFilter(thread);
                    breakpRequest.setSuspendPolicy( EventRequest.SUSPEND_EVENT_THREAD);
                } catch ( Exception e1 ) {
                    log3("ERROR: inner Exception within settingBreakpoint() : " + e1);
                    breakpRequest    = null;
                }
            }
        } catch ( Exception e2 ) {
            log3("ERROR: ATTENTION:  outer Exception within settingBreakpoint() : " + e2);
            breakpRequest    = null;
        }

        if (breakpRequest == null) {
            log2("      A BREAKPOINT HAS NOT BEEN SET UP");
            throw new JDITestRuntimeException("**FAILURE to set up a breakpoint**");
        }

        log2("      a breakpoint has been set up");
        return breakpRequest;
    }


    private void getEventSet()
                 throws JDITestRuntimeException {
        try {
//            log2("       eventSet = eventQueue.remove(waitTime);");
            eventSet = eventQueue.remove(waitTime);
            if (eventSet == null) {
                throw new JDITestRuntimeException("** TIMEOUT while waiting for event **");
            }
//            log2("       eventIterator = eventSet.eventIterator;");
            eventIterator = eventSet.eventIterator();
        } catch ( Exception e ) {
            throw new JDITestRuntimeException("** EXCEPTION while waiting for event ** : " + e);
        }
    }


    private void breakpointForCommunication()
                 throws JDITestRuntimeException {

        log2("breakpointForCommunication");
        getEventSet();

        if (eventIterator.nextEvent() instanceof BreakpointEvent)
            return;

        throw new JDITestRuntimeException("** event IS NOT a breakpoint **");
    }

    // ============================== test's additional methods

    private StepRequest setting24StepRequest ( ThreadReference thread,
                                               int             size,
                                               int             depth )
            throws JDITestRuntimeException {
        try {
            log2("......setting up StepRequest:");
            log2("       thread: " + thread + "; size: " + size + "; depth: " + depth);

            StepRequest
            str = eventRManager.createStepRequest(thread, size, depth);

            log2("      StepRequest has been set up");
            return str;
        } catch ( Exception e ) {
            log3("ERROR: ATTENTION: Exception within settingStepRequest() : " + e);
            log3("       StepRequest HAS NOT BEEN SET UP");
            throw new JDITestRuntimeException("** FAILURE to set up StepRequest **");
        }
    }

}
