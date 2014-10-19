package com.ipe.module.core.web.interceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.ipe.common.util.Logs;
import com.ipe.module.core.entity.Log;
import com.ipe.module.core.service.LogService;
import com.ipe.module.core.web.security.SystemRealm;
import com.ipe.module.core.web.util.WebUtil;

/**
 * Controller 日志切面
 * Created by tangdu on 14-2-8.
 */
public class SecutiryInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private LogService logService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURL().toString();
        if(url.endsWith(".js")||url.endsWith(".png")||url.endsWith(".css")||url.endsWith(".jpg")||url.endsWith(".jpeg")){
            return true;
        }

        ///对系统资源添加日志
        if(handler instanceof  HandlerMethod){
            HandlerMethod method=(HandlerMethod)handler;
            //方法注解路径
            Annotation annotation=method.getMethodAnnotation(Logs.class);
            if(annotation!=null){
                Logs log=(Logs)annotation;
                MethodParameter [] parameters=method.getMethodParameters();
                Method method1=method.getMethod();
                //保存日志
                logService.save(saveLog(request, method1.getName(), log.opdesc()));
            }
        }
        return super.preHandle(request,response,handler);
    }

    Log saveLog(HttpServletRequest request,String method,String desc){
        SystemRealm.UserInfo user =null;
        if(SecurityUtils.getSubject().getPrincipal()!=null){
            user = (SystemRealm.UserInfo)SecurityUtils.getSubject().getPrincipal();
        }
        String ip = WebUtil.getIpAddr(request);
        String url = request.getRequestURL().toString();

        Log log = new Log();
        log.setAccessIp(ip);
        log.setAccessMethod(method);
        log.setAccessPerson(accessUserName());
        log.setAccessTime(new Date());
        log.setOperate(request.getMethod() + "_" + url);
        log.setLogType("sys");
        log.setRemark(desc);
        if(user!=null){
            log.setAccessUserid(user.getUserId());
        }
        return log;
    }

    String accessUserName(){
        SystemRealm.UserInfo user =null;
        if(SecurityUtils.getSubject().getPrincipal()!=null){
            user = (SystemRealm.UserInfo)SecurityUtils.getSubject().getPrincipal();
           return user.getUserAccount()+"-"+user.getUserName();
        }
        return "N/A";
    }

    private Log saveAccessLog(HttpServletRequest request) {
        SystemRealm.UserInfo user =null;
        if(SecurityUtils.getSubject().getPrincipal()!=null){
            user = (SystemRealm.UserInfo)SecurityUtils.getSubject().getPrincipal();
        }
        String jsessionId = request.getRequestedSessionId();
        String ip = WebUtil.getIpAddr(request);
        String accept = request.getHeader("accept");
        String userAgent = request.getHeader("User-Agent");
        String url = request.getRequestURL().toString();

        StringBuilder s = new StringBuilder();
        s.append(getBlock(jsessionId));
        s.append(getBlock(accessUserName()));
        s.append(getBlock(jsessionId));
        s.append(getBlock(ip));
        s.append(getBlock(accept));
        s.append(getBlock(userAgent));
        s.append(getBlock(url));
        s.append(getBlock(request.getHeader("Referer")));

        Log log = new Log();
        log.setAccessIp(ip);
        log.setAccessMethod(request.getMethod());
        log.setAccessPerson(accessUserName());
        log.setAccessTime(new Date());
        log.setOperate(url);
        log.setLogType("operation");
        log.setRemark(s.toString());
        if(user!=null){
            log.setAccessUserid(user.getUserId());
        }
        return log;
    }

    protected String getParams(HttpServletRequest request) {
        Map<String, String[]> params = request.getParameterMap();
        return JSON.toJSONString(params);
    }

    protected String getBlock(Object msg) {
        if (msg == null) {
            msg = "";
        }
        return "[" + msg.toString() + "]";
    }
}
