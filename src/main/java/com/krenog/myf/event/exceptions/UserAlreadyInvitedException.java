package com.krenog.myf.event.exceptions;

public class UserAlreadyInvitedException extends RuntimeException {
    public UserAlreadyInvitedException(String message) {
        super(message);
    }
}
