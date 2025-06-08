package com.jimmy_d.notes_backend.exceptions.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidNoteQueryException extends ApiException{
    public InvalidNoteQueryException(){
        super("You cannot create an empty note.");
    }

    @Override
    public String getKey() {
        return "query";
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
