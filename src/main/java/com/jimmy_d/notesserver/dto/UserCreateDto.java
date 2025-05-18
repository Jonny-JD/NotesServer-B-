package com.jimmy_d.notesserver.dto;

import com.jimmy_d.notesserver.validation.annotation.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record UserCreateDto(@NotBlank String username,
                            @NotBlank String RawPassword,
                            @Email @NotBlank String email,
                            @UserRole Set<String> roles) {
}
