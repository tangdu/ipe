package com.ipe.module.core.web.controller;

import com.ipe.module.core.web.util.BodyWrapper;
import com.ipe.module.core.web.util.URLMapping;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.management.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tangdu on 14-2-6.
 */
@Controller
@RequestMapping("tool")
public class ToolController extends AbstractController {
    private static final org.slf4j.Logger Logger = LoggerFactory.getLogger(ToolController.class);

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


   /* *//**
     * 得到信息所有URL映射
     *
     * @param params
     * @param request
     * @return
     *//*
    @RequestMapping(value = "/spring_urlmapping")
    public
    @ResponseBody
    BodyWrapper getSpringUrlMapping(String params, HttpServletRequest request) {
        try {
            List<URLMapping> list = new ArrayList<URLMapping>();
            WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
            RequestMappingInfoHandlerMapping requestMappingHandlerMapping=webApplicationContext.getBean(RequestMappingInfoHandlerMapping.class);
            Map<String, HandlerMapping> matchingBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors(webApplicationContext, HandlerMapping.class, true, false);
            Map<RequestMappingInfo, HandlerMethod> methodMap = new HashMap<>();
            for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : methodMap.entrySet()) {
                RequestMappingInfo info = entry.getKey();
                HandlerMethod method = entry.getValue();
                URLMapping mapping = new URLMapping();

                mapping.setConsumes(String.valueOf(info.getConsumesCondition()));
                mapping.setCustom(String.valueOf(info.getCustomCondition()));
                mapping.setHeaders(String.valueOf(info.getHeadersCondition()));
                mapping.setMethods(String.valueOf(info.getMethodsCondition()));
                mapping.setParams(String.valueOf(info.getParamsCondition()));
                mapping.setProduces(String.valueOf(info.getProducesCondition()));
                mapping.setUrl(String.valueOf(info.getPatternsCondition()));
                mapping.setMethodName(method.getMethod().getName());
                mapping.setClassName(method.getBeanType().getName());
                mapping.setReturnType(method.getReturnType().getParameterType().toString());
                MethodParameter[] parameters = method.getMethodParameters();
                ResponseBody responseBody = method.getMethodAnnotation(ResponseBody.class);
                if (responseBody != null) {
                    mapping.setAnnotationName(responseBody.getClass().getName());
                }
                List<String> params_ = new ArrayList<String>();
                for (MethodParameter m : parameters) {
                    params_.add(m.getParameterType().getName());
                }
                mapping.setParameters(String.valueOf(params_));
                list.add(mapping);
            }
            return success(list);
        } catch (Exception e) {
            Logger.error("Exception {}", e);
            return failure();
        }
    }*/
}
