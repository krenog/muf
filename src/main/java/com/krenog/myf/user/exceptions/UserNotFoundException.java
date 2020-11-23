package com.krenog.myf.user.exceptions;

import com.krenog.myf.exceptions.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
