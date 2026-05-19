package com.jimmy_d.notes_backend.dto;

import com.jimmy_d.notes_backend.validation.annotation.NoteContent;

public record NoteCreateDto(String title,
                            String tag,
                            @NoteContent String content,
                            UserDetailsDto author,
                            Boolean isPrivate) {
}
