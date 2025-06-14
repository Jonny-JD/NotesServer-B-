package com.jimmy_d.notes_backend.dto;

import java.util.UUID;

public record NoteReadDto(UUID id,
                          String title,
                          String tag,
                          String content,
                          NoteAuthorDto author,
                          Boolean isPrivate) {
}
