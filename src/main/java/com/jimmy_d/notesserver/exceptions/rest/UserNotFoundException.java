package com.jimmy_d.notesserver.exceptions.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String type, Object identifier) {
        super("User not found by " + type + ": [" + identifier + "]");;
    }
}
