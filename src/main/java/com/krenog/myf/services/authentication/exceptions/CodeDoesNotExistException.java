package com.krenog.myf.services.authentication.exceptions;

public class CodeDoesNotExistException extends RuntimeException {
    public CodeDoesNotExistException(String message) {
        super(message);
    }
}
