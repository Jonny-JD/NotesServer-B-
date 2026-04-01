package com.jimmy_d.notes_backend.dto;

import java.util.Set;

public record UserReadDto(Long id,
                          String username,
                          String email,
                          Set<String> roles) {
}
