package com.jimmy_d.notes_backend.exceptions.rest;


import com.jimmy_d.notes_backend.database.entity.Role;
import org.springframework.http.HttpStatus;

public class RoleAlreadyExistsException extends ApiException {
    public RoleAlreadyExistsException(Role role) {
        super("Role" + ": [" + role + "]" + "already exists");
    }

    @Override
    public String getKey() {
        return "role";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.CONFLICT;
    }
}
