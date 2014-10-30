package com.ipe.module.core.web.interceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.RequestMapping;
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
    private static final String FILTERS=".js,.png,.css,.jpg,.jpeg";

    @SuppressWarnings({"unused" })
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURL().toString();
        String f_[]=FILTERS.split("[,]");
        boolean f_flag=false;
        for(int i=0;i<f_.length;i++){
        	if(url.endsWith(f_[i])){
        		return true;
        	}
        }
        
        //拦截方法日志//
        if(handler instanceof  HandlerMethod){
            HandlerMethod method=(HandlerMethod)handler;
            //日志注释
            Annotation annotation=method.getMethodAnnotation(Logs.class);
            if(annotation!=null){
                Logs log=(Logs)annotation;
				MethodParameter [] parameters=method.getMethodParameters();
                Method method1=method.getMethod();
                logService.save(saveLog(request, method.getBean().getClass().getSimpleName()+"."+method1.getName(), log.opdesc()));//保存日志
            }
            //映射注释
            RequestMapping annRes=method.getMethodAnnotation(RequestMapping.class);
            if(annRes!=null && annRes.value()!=null){
            	Subject subject=SecurityUtils.getSubject();
            	boolean[] check=subject.isPermitted(annRes.value());
            	boolean hasPer=false;
        		for(boolean c :check){
        			if(c){
        				hasPer=true;
        			}
        			break;
        		}
        		if(hasPer){
        			
        		}else{
        			System.out.println("not ...");
        		}
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
        log.setLogType("L2");
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

    @SuppressWarnings("unused")
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
    
    @SuppressWarnings("unchecked")
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
