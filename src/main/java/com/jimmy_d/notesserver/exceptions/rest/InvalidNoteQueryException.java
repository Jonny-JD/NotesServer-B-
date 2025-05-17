package com.jimmy_d.notesserver.exceptions.rest;

public class InvalidNoteQueryException extends RuntimeException{
    public InvalidNoteQueryException(){
        super("At least one of the next parameters: tag, author, or cursor must be provided");
    }
}
