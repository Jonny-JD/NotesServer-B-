package com.jimmy_d.notesserver.http.controller.rest;

import com.jimmy_d.notesserver.exceptions.rest.*;
import com.jimmy_d.notesserver.validation.ApiExceptionDto;
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
        Map<String, String> errors = Map.of("user", exception.getMessage());
        ApiExceptionDto apiError = new ApiExceptionDto(HttpStatus.BAD_REQUEST, errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(UserUpdateException.class)
    public ResponseEntity<ApiExceptionDto> handleUserUpdateException(UserUpdateException exception) {
        Map<String, String> errors = Map.of("update", exception.getMessage());
        ApiExceptionDto apiError = new ApiExceptionDto(HttpStatus.CONFLICT, errors);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiExceptionDto> handleUserNotFoundException(UserNotFoundException exception) {
        Map<String, String> errors = Map.of("user", exception.getMessage());
        ApiExceptionDto apiError = new ApiExceptionDto(HttpStatus.NOT_FOUND, errors);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler(InvalidNoteQueryException.class)
    public ResponseEntity<ApiExceptionDto> handleInvalidNoteQueryException(InvalidNoteQueryException exception) {
        Map<String, String> errors = Map.of("query", exception.getMessage());
        ApiExceptionDto apiError = new ApiExceptionDto(HttpStatus.NOT_FOUND, errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(UserNotExistsException.class)
    public ResponseEntity<ApiExceptionDto> handleInvalidNoteQueryException(UserNotExistsException exception) {
        Map<String, String> errors = Map.of("update", exception.getMessage());
        ApiExceptionDto apiError = new ApiExceptionDto(HttpStatus.CONFLICT, errors);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiExceptionDto> handleAllExceptions(Exception ex) {
        log.error("Unhandled exception caught: ", ex);
        Map<String, String> errors = Map.of("error", "Internal server error occurred");
        ApiExceptionDto apiError = new ApiExceptionDto(HttpStatus.INTERNAL_SERVER_ERROR, errors);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }
}
