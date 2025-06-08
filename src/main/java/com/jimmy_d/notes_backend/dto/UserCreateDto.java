package com.jimmy_d.notes_backend.dto;

import com.jimmy_d.notes_backend.validation.annotation.UserRole;
import com.jimmy_d.notes_backend.validation.annotation.Username;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record UserCreateDto(@Username String username,
                            @NotBlank String rawPassword,
                            @Email @NotBlank String email,
                            @UserRole Set<String> roles) {
}
