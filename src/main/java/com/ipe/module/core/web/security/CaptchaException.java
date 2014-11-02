package com.ipe.module.core.web.security;

import org.apache.shiro.authc.AuthenticationException;

public class CaptchaException extends AuthenticationException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7103360498780003661L;

	public CaptchaException() {
		super();
	}

	public CaptchaException(String message, Throwable cause) {
		super(message, cause);
	}

	public CaptchaException(String message) {
		super(message);
	}

	public CaptchaException(Throwable cause) {
		super(cause);
	}
}
