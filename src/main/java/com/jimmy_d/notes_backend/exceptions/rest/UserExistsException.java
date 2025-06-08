package com.jimmy_d.notes_backend.exceptions.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class UserExistsException extends ApiException {
    public UserExistsException(String type, Object identifier) {
        super("User with " + type + ": [" + identifier + "] already exists");
    }

    @Override
    public String getKey() {
        return "user";
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}
