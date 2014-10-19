package com.ipe.module.core.web.util;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: tangdu
 * Date: 13-10-31
 * Time: 上午7:33
 * To change this template use File | Settings | File Templates.
 */
public class CustExceptionHandler implements HandlerExceptionResolver {
    private MappingJackson2JsonView  jsonView = new MappingJackson2JsonView();

    @Override
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response,
                                         Object o, Exception ex) {
        Map model = new HashMap();
        model.put("ex", ex.getClass().getSimpleName());
        model.put("error", ex.getMessage());
        model.put("success", false);

        //如果是ajax提交
        if (request.getHeader("X-Requested-With") != null
                && request.getHeader("X-Requested-With")
                .equalsIgnoreCase("XMLHttpRequest")){
            if(ex instanceof  org.apache.shiro.authz.UnauthorizedException){
                model.put("rows", "您没有权限访问");
                //response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return new ModelAndView(jsonView,model);
            }else {
                //response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                model.put("rows", "程序内部错误"+model.get("error"));
                return new ModelAndView(jsonView,model);
            }
        }else{
            //如果是页面提交
            if(ex instanceof  org.apache.shiro.authz.UnauthorizedException){
                return new ModelAndView("common/403",model);
            }else {
                return new ModelAndView("common/500",model);
            }
        }
    }
}
