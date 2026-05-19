package com.jimmy_d.notes_backend.dto;

public record AuthResponse(
        String token,
        Long id,
        String username,
        String email) {}