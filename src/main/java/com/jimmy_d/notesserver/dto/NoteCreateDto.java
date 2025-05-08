package com.jimmy_d.notesserver.dto;

public record NoteCreateDto(String title,
                            String tag,
                            String content,
                            String author) {
}
