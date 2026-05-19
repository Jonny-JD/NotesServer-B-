package com.jimmy_d.notes_backend.exceptions.rest;

import com.jimmy_d.notes_backend.database.entity.Role;
import org.springframework.http.HttpStatus;

public class RoleNotFoundException extends ApiException {
    public RoleNotFoundException(Role role) {
        super("Role" + ": [" + role + "]" + "not found");
    }

    @Override
    public String getKey() {
        return "role";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }

}
