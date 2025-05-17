package com.jimmy_d.notesserver.dto;

import com.jimmy_d.notesserver.validation.annotation.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record UserCreateDto(@NotBlank String username,
                            @NotBlank String RawPassword,
                            @Email String email,
                            @NotEmpty @UserRole Set<String> roles) {
}
