package com.jimmy_d.notesserver.dto;

import java.util.UUID;

public record NoteReadDto(UUID id,
                          String title,
                          String tag,
                          String content,
                          NoteAuthorDto author) {
}
