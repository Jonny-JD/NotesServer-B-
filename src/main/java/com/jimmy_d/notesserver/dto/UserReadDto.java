package com.jimmy_d.notesserver.dto;

import java.util.Set;

public record UserReadDto(Long id,
                          String username,
                          String email,
                          Set<String> roles) {
}
