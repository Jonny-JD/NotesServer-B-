package com.jimmy_d.notes_backend.exceptions.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends ApiException {
    public UserNotFoundException(String type, Object identifier) {
        super("User not found by " + type + ": [" + identifier + "]");
    }

    @Override
    public String getKey() {
        return "user";
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
