package com.jimmy_d.notes_backend.exceptions.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoteNotFoundException extends ApiException {
    public NoteNotFoundException(String type, Object identifier) {
        super("Note not found by " + type + ": [" + identifier + "]");
    }

    @Override
    public String getKey() {
        return "note";
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}