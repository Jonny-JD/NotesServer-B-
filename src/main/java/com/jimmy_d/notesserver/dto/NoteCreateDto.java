package com.jimmy_d.notesserver.dto;

import com.jimmy_d.notesserver.validation.annotation.NoteContent;

public record NoteCreateDto(String title,
                            String tag,
                            @NoteContent String content,
                            UserReadDto author,
                            Boolean isPrivate) {
}
