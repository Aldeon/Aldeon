package org.aldeon.utils.various;

import org.aldeon.app.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.jvmstat.monitor.MonitoredHost;
import sun.jvmstat.monitor.MonitoredVm;
import sun.jvmstat.monitor.MonitoredVmUtil;
import sun.jvmstat.monitor.VmIdentifier;

import java.lang.management.ManagementFactory;

public class VirtualMachine {

    private static final Logger log = LoggerFactory.getLogger(VirtualMachine.class);

    //Source: http://willhaley.com/blog/java-single-instance/
    //Based on: http://chaoticjava.com/posts/retrieving-a-vms-pid-and-more-info-through-java/
    public static int getNumberOfApplicationInstances()
    {
        int counter = 0;
        log.info("Verify if another running instance is active...");
        try
        {
            int pid;
            MonitoredVm vm;
            String vmClass;
            String vmCmdLine;

            //get monitored host for local computer
            MonitoredHost host = MonitoredHost.getMonitoredHost("localhost");

            log.info("Class name: " +Main.class.getName());
            log.info("Class simple name: " +Main.class.getSimpleName());
            log.info("Active virtual machines: " + host.activeVms().toString());
            log.info("Runtime info of this class: " + ManagementFactory.getRuntimeMXBean().getName());

            //iterate over pids of running applications on this host.
            //seems every application is considered an instance of the 'activeVM'
            for(Object activeVMPid : host.activeVms())
            {
                pid = (Integer)activeVMPid;

                //get specific vm instance for given pid
                vm = host.getMonitoredVm(new VmIdentifier("//" + pid));

                //get class of given vm instance's main class
                vmCmdLine = MonitoredVmUtil.commandLine(vm);
                vmClass = MonitoredVmUtil.mainClass(vm, true);

                //is class in vm same as class you're comparing?
                log.info("Looking at pid: " + pid + " | Class to examine: [" + vmClass + "] | Cmd line to examine: [" + vmCmdLine + "]");

                if(vmClass.equals(Main.class.getName())
                        || vmCmdLine.contains(Main.class.getName())
                        // || vmCmdLine.contains(Main.class.getSimpleName())
                        // || vmClass.equals("aldeon.jar")
                        // || vmCmdLine.contains("aldeon.jar")
                  )
                {
                    counter++;
                    log.info("Match to current class");
                }
            }

            log.info("Found running instances of this program (including this one): " + counter);

            return counter;
        }
        catch (Exception e)
        {
            log.error("Error in getNumberOfApplicationInstances", e);
            return 2;
        }
    }
}
