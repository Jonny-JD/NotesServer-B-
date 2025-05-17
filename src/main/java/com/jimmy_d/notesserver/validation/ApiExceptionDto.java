package com.jimmy_d.notesserver.validation;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class ApiExceptionDto {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private Map<String, String> errors;

    public ApiExceptionDto(HttpStatus status, Map<String, String> errors) {
        this.timestamp = LocalDateTime.now();
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.errors = errors;
    }
}
