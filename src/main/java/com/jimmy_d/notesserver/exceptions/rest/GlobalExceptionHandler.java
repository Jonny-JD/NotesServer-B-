package com.jimmy_d.notesserver.exceptions.rest;

import com.jimmy_d.notesserver.dto.ApiExceptionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ApiExceptionDto> buildErrorResponse(HttpStatus status, String key, String message) {
        Map<String, String> errors = Map.of(key, message);
        ApiExceptionDto apiError = new ApiExceptionDto(status, errors);
        return ResponseEntity.status(status).body(apiError);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiExceptionDto> handleApiException(ApiException exception) {
        log.warn("API exception: {} - {}", exception.getKey(), exception.getMessage());
        return buildErrorResponse(exception.getStatus(), exception.getKey(), exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiExceptionDto> handleValidationExceptions(MethodArgumentNotValidException exception) {
        FieldError fieldError = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .orElse(null);

        String errorMessage = fieldError != null
                ? String.format("Field '%s': %s", fieldError.getField(), fieldError.getDefaultMessage())
                : "Validation error";

        log.warn("Validation failed: {}", errorMessage);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "validation", errorMessage);
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<ApiExceptionDto> handleInternalAuthenticationServiceException(InternalAuthenticationServiceException exception) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "validation", exception.getMessage());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ApiExceptionDto> handleAuthorizationDeniedException(AuthorizationDeniedException exception) {
        log.warn("Authorization denied: {}", exception.getMessage());
        return buildErrorResponse(HttpStatus.FORBIDDEN, "access", exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiExceptionDto> handleAllExceptions(Exception exception) {
        log.error("Unhandled exception caught: ", exception);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "error", "Internal server error");
    }
}