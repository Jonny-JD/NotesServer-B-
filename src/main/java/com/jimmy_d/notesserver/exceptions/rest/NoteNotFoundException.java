package com.jimmy_d.notesserver.exceptions.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoteNotFoundException extends RuntimeException {
    public NoteNotFoundException(String type, Object identifier) {
        super("Note not found by " + type + ": [" + identifier + "]");
    }
}