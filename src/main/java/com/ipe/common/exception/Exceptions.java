package com.ipe.common.exception;

import com.ipe.common.exception.CustException;
import com.ipe.common.exception.ServiceException;

/**
 * Created by tangdu on 14-2-28.
 */
public class Exceptions {

    public static ServiceException throwServiceException(Exception e){
        return new ServiceException(e);
    }
    public static CustException throwCustException(Exception e){
        return new CustException(e);
    }
}
