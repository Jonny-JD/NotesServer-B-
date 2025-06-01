package com.jimmy_d.notesserver.dto;

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