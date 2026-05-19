package com.jimmy_d.notes_backend.dto;

public record UserInfoResponse(
        Long id,
        String username,
        String email) {
}
