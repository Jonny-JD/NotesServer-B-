package com.jimmy_d.notes_backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UserUpdateDto(
                            @NotNull @Positive Long id,
                            String username,
                            String email,
                            String currentPassword,
                            String newPassword) {
}
