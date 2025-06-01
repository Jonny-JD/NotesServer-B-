package com.jimmy_d.notesserver.dto;

import java.time.Instant;
import java.util.UUID;

public record NotePreviewDto(
        UUID id,
        String title,
        String tag,
        String authorUsername,
        Instant createdAt,
        String link
) {
    public NotePreviewDto(UUID id, String title, String tag, String authorUsername, Instant createdAt) {
        this(id, title, tag, authorUsername, createdAt, "/notes/" + id);
    }
}