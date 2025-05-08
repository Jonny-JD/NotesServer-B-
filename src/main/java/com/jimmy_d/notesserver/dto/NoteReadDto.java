package com.jimmy_d.notesserver.dto;

public record NoteReadDto(String title,
                          String tag,
                          String content,
                          UserReadDto author) {
}
