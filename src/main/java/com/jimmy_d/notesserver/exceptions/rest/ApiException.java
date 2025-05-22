package com.jimmy_d.notesserver.exceptions.rest;

import org.springframework.http.HttpStatus;

public abstract class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }

    public abstract String getKey();

    public abstract HttpStatus getStatus();
}
