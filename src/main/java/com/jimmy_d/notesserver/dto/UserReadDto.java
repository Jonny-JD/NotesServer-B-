package com.jimmy_d.notesserver.dto;

public record UserReadDto(Long id,
                          String username,
                          String email) {
}
