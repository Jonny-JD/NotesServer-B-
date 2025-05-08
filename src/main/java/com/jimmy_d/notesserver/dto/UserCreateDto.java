package com.jimmy_d.notesserver.dto;

public record UserCreateDto(String username,
                            String RawPassword,
                            String email) {
}
