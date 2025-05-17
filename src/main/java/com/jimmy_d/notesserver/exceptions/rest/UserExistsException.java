package com.jimmy_d.notesserver.exceptions.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserExistsException extends RuntimeException {
    public UserExistsException(String type, Object identifier) {
        super("User with " + type + ": [" + identifier + "] already exists");
    }
}
