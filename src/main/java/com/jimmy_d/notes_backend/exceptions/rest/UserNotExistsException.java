package com.jimmy_d.notes_backend.exceptions.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class UserNotExistsException extends ApiException {
    public UserNotExistsException(String type, Object identifier) {
        super("User with " + type + ": [" + identifier + "] not exists");
    }

    @Override
    public String getKey() {
        return "update";
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}
