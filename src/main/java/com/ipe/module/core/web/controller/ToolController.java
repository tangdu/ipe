package com.ipe.module.core.web.controller;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by tangdu on 14-2-6.
 */
@Controller
@RequestMapping("tool")
public class ToolController extends AbstractController {
    /**
     * 查看系统信息-内存-环境等信息
     *
     * @param mp
     * @return
     */
    @RequestMapping(value = "/systeminfo")
    public String getRuntimeInfo(ModelMap mp) {
        RuntimeMXBean rt = ManagementFactory.getRuntimeMXBean();
        mp.put("vmName", rt.getVmName());
        mp.put("vmVersion", rt.getVmVersion());
        mp.put("vmVendor", rt.getVmVendor());
        mp.put("upTime", rt.getUptime());
        mp.put("inputArguments", rt.getInputArguments().toString());
        mp.put("libPath", rt.getLibraryPath());
        mp.put("classPath", rt.getClassPath());

        MemoryMXBean my = ManagementFactory.getMemoryMXBean();
        mp.put("initMemory", my.getHeapMemoryUsage().getInit() / 1000000 + " M");
        mp.put("maxMemory", my.getHeapMemoryUsage().getMax() / 1000000 + " M");
        mp.put("usedMemory", my.getHeapMemoryUsage().getUsed() / 1000000 + " M");

        OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
        mp.put("osName", os.getName());
        mp.put("osVersion", os.getVersion());
        mp.put("osAvailableProcessors", os.getAvailableProcessors());
        mp.put("osArch", os.getArch());

        ThreadMXBean tm = ManagementFactory.getThreadMXBean();
        tm.setThreadContentionMonitoringEnabled(true);
        mp.put("threadCount", tm.getThreadCount());
        mp.put("startedThreadCount", tm.getTotalStartedThreadCount());
        mp.put("threadContentionMonitoringEnabled", tm.isThreadContentionMonitoringEnabled() ? "启用" : "禁用");
        mp.put("threadContentionMonitoringSupported", tm.isThreadContentionMonitoringSupported() ? "支持" : "不支持");
        mp.put("threadCpuTimeEnabled", tm.isThreadCpuTimeEnabled() ? "启用" : "禁用");
        mp.put("threadCpuTimeSupported", tm.isThreadCpuTimeSupported() ? "支持" : "不支持");

        return "tools/jvminfo";
    }
}
