package com.jimmy_d.notesserver.dto;

public record NotePreviewFilter(String title,
                                String tag,
                                Long authorId) {
}
