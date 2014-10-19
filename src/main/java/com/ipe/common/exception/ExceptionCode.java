package com.ipe.common.exception;

/**
 * Created by tangdu on 14-2-28.
 */
public enum ExceptionCode {
    SERVICEEXCEPTION(100),CUSTEXCEPTION(200);

    private int code;
    public int getCode(){
        return this.code;
    }
    ExceptionCode(int code){
        this.code=code;
    }
}
