package com.krenog.myf.user.services.authentication.exceptions;

public class CodeDoesNotExistException extends RuntimeException {
    public CodeDoesNotExistException(String message) {
        super(message);
    }
}
