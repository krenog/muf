package com.krenog.myf.services.authentication.exceptions;

public class NumberCodeAttemptsException extends RuntimeException {
    public NumberCodeAttemptsException(String message) {
        super(message);
    }
}