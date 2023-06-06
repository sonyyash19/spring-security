package com.ecom.user.exception;

public class UserNotAuthenticatedException extends RuntimeException {

    public UserNotAuthenticatedException(String msg){
        super(msg);
    }
    
}
