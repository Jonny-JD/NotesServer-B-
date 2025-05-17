package com.jimmy_d.notesserver.exceptions.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class UserUpdateException extends RuntimeException{
    public UserUpdateException() {
        super("ID in path and body must match");
    }
}
