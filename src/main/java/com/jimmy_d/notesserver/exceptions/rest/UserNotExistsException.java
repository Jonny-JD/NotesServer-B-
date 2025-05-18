package com.jimmy_d.notesserver.exceptions.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class UserNotExistsException extends RuntimeException {
    public UserNotExistsException(String type, Object identifier) {
        super("User with " + type + ": [" + identifier + "] not exists");
    }

}
