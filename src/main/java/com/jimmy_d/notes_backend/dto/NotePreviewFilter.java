package com.jimmy_d.notes_backend.dto;

public record NotePreviewFilter(String title,
                                String tag,
                                Long authorId) {
}
