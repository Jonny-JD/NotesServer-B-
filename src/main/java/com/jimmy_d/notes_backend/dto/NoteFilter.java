package com.jimmy_d.notes_backend.dto;

public record NoteFilter(String title,
                         String tag,
                         String content,
                         Long authorId) {
}
