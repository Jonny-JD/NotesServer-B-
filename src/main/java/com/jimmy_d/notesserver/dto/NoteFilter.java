package com.jimmy_d.notesserver.dto;

public record NoteFilter(String title,
                         String tag,
                         String content,
                         Long authorId) {
}
