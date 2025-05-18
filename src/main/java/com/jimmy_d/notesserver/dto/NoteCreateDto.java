package com.jimmy_d.notesserver.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record NoteCreateDto(@NotEmpty String title,
                            String tag,
                            String content,
                            @NotNull UserReadDto author) {
}
