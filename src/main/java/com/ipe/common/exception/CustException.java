package com.ipe.common.exception;

/**
 * Created by tangdu on 14-2-28.
 */
public class CustException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5241526710121081092L;
	private int code=000;

    public int getCode() {
        return code;
    }

    public CustException(String e) {
        super("CustException "+e);
        this.code = 200;
    }
    
    public CustException(Exception e) {
        super("CustException ", e);
        this.code = 200;
    }

    public CustException(Throwable throwable) {
        super("CustException ", throwable);
        this.code = 200;
    }

    public CustException(Exception e, ExceptionCode code) {
        super("CustException ", e);
        this.code = code.getCode();
    }

    public CustException(Throwable throwable, ExceptionCode code) {
        super("CustException ", throwable);
        this.code = code.getCode();
    }
}
