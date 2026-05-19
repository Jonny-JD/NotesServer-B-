package com.jimmy_d.notes_backend.exceptions.rest;

import org.springframework.http.HttpStatus;

public abstract class ApiException extends RuntimeException {

    protected ApiException(String message) {
        super(message);
    }

    public abstract String getKey();

    public abstract HttpStatus getHttpStatus();
}
