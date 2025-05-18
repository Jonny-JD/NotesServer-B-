package com.jimmy_d.notesserver.exceptions.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidNoteQueryException extends RuntimeException{
    public InvalidNoteQueryException(){
        super("At least one of the next parameters: tag, title, content, or author_id must be provided");
    }
}
