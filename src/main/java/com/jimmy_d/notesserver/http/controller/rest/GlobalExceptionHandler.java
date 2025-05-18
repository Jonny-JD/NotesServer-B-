package com.jimmy_d.notesserver.http.controller.rest;

import com.jimmy_d.notesserver.exceptions.rest.*;
import com.jimmy_d.notesserver.dto.ApiExceptionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ApiExceptionDto> buildErrorResponse(HttpStatus status, String key, String message) {
        Map<String, String> errors = Map.of(key, message);
        ApiExceptionDto apiError = new ApiExceptionDto(status, errors);
        return ResponseEntity.status(status).body(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiExceptionDto> handleValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        ApiExceptionDto apiError = new ApiExceptionDto(HttpStatus.BAD_REQUEST, errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<ApiExceptionDto> handleUserCreateException(UserExistsException exception) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "user", exception.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiExceptionDto> handleUserNotFoundException(UserNotFoundException exception) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "user", exception.getMessage());
    }

    @ExceptionHandler(InvalidNoteQueryException.class)
    public ResponseEntity<ApiExceptionDto> handleInvalidNoteQueryException(InvalidNoteQueryException exception) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "query", exception.getMessage());
    }

    @ExceptionHandler(UserNotExistsException.class)
    public ResponseEntity<ApiExceptionDto> handleInvalidNoteQueryException(UserNotExistsException exception) {
        return buildErrorResponse(HttpStatus.CONFLICT, "update", exception.getMessage());

    }

    @ExceptionHandler(NoteNotFoundException.class)
    public ResponseEntity<ApiExceptionDto> handleNoteNotFoundException(NoteNotFoundException exception) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "note", exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiExceptionDto> handleAllExceptions(Exception exception) {
        log.error("Unhandled exception caught: ", exception);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "error", exception.getMessage());
    }

}
