package com.jimmy_d.notesserver.exceptions.rest;

public class InvalidNoteQueryException extends RuntimeException{
    public InvalidNoteQueryException(){
        super("At least one of the next parameters: tag, title, content, or author_id must be provided");
    }
}
