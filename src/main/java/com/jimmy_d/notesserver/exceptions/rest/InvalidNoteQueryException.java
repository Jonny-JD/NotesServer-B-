package com.jimmy_d.notesserver.exceptions.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidNoteQueryException extends ApiException{
    public InvalidNoteQueryException(){
        super("At least one of the next parameters: tag, title, content, or author_id must be provided");
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
