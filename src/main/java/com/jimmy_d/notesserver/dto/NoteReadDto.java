package com.jimmy_d.notesserver.dto;

public record NoteReadDto(Long id,
                          String title,
                          String tag,
                          String content,
                          UserReadDto author) {
}
