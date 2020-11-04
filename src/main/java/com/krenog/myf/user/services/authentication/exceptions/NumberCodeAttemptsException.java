package com.krenog.myf.user.services.authentication.exceptions;

public class NumberCodeAttemptsException extends RuntimeException {
    public NumberCodeAttemptsException(String message) {
        super(message);
    }
}