package com.another.exception;

public class RetryException extends RuntimeException{
    public RetryException(String error){
        super(error);
    }
}
