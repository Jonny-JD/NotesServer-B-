package com.jimmy_d.notes_backend.dto;

import java.time.Instant;
import java.util.UUID;

public record NotePreviewDto(
        UUID id,
        String title,
        String tag,
        String author,
        Instant createdAt
) {
}