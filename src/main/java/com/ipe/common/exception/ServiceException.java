package com.ipe.common.exception;

/**
 * Created by tangdu on 14-2-28.
 */
public class ServiceException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6555556900058478507L;
	private int code;

    public int getCode() {
        return code;
    }

    public ServiceException(Exception e){
        this.code=100;
    }
    public ServiceException(Exception e,ExceptionCode code){
        this.code=code.getCode();
    }
}
