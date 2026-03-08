package com.jimmy_d.notes_backend.dto;

import java.util.Set;

public record UserUpdateDto(Long id,
                            String username,
                            String email,
                            String currentPassword,
                            String newPassword,
                            Set<String> roles) {
}
