package com.ipe.module.core.web.security;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * Created with IntelliJ IDEA. User: tangdu Date: 13-11-17 Time: 下午5:20 To
 * change this template use File | Settings | File Templates.
 */
public class CustUsernamePasswordToken extends UsernamePasswordToken {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6155075936209206793L;
	private String method;
	private String captcha;
	private String accessUrl;
	private String accessIp;

	public CustUsernamePasswordToken(String username, String password) {
		super(username, password);
	}

	public CustUsernamePasswordToken(String method, String captcha,
			String accessIp) {
		super();
		this.method = method;
		this.captcha = captcha;
		this.accessIp = accessIp;
	}
	public CustUsernamePasswordToken(String username, String password,String method, String captcha,
			String accessUrl, String accessIp) {
		super(username, password);
		this.method = method;
		this.captcha = captcha;
		this.accessUrl = accessUrl;
		this.accessIp = accessIp;
	}
	
	public CustUsernamePasswordToken(String method, String captcha,
			String accessUrl, String accessIp) {
		super();
		this.method = method;
		this.captcha = captcha;
		this.accessUrl = accessUrl;
		this.accessIp = accessIp;
	}

	public String getAccessIp() {
		return accessIp;
	}

	public void setAccessIp(String accessIp) {
		this.accessIp = accessIp;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	public String getAccessUrl() {
		return accessUrl;
	}

	public void setAccessUrl(String accessUrl) {
		this.accessUrl = accessUrl;
	}
}
