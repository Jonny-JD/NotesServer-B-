package com.jimmy_d.notes_backend.dto;

import com.jimmy_d.notes_backend.validation.annotation.NoteContent;

public record NoteUpdateDto(String title,
                            String tag,
                            @NoteContent String content,
                            Boolean isPrivate) {
}
