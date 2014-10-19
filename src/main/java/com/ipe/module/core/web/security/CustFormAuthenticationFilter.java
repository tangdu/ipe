package com.ipe.module.core.web.security;

import com.ipe.module.core.web.util.WebUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * User: tangdu
 * Date: 13-11-17
 * Time: 下午5:17
 * To change this template use File | Settings | File Templates.
 */
public class CustFormAuthenticationFilter extends FormAuthenticationFilter {

    public String getCaptcha(ServletRequest request){
        return request.getParameter("captcha");
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        String username=getUsername(request);
        String password=getPassword(request);
        String captcha=getCaptcha(request);
        String host= WebUtil.getIpAddr((HttpServletRequest) request);
        String method=((HttpServletRequest) request).getMethod();
        String accessUrl=((HttpServletRequest) request).getRequestURL().toString();
        return new CustUsernamePasswordToken(username,password,host,method,captcha,accessUrl);
    }

    @Override
    protected boolean executeLogin(ServletRequest request,ServletResponse response) throws Exception {
        CustUsernamePasswordToken token = (CustUsernamePasswordToken) createToken(request, response);
        try {
            Subject subject = getSubject(request, response);
            subject.login(token);//正常验证
            return onLoginSuccess(token, subject, request, response);
        }catch (AuthenticationException e) {
            return onLoginFailure(token, e, request, response);
        }
    }
}
