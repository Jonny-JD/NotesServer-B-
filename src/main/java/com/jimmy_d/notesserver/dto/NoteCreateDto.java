package com.jimmy_d.notesserver.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NoteCreateDto(@NotBlank String title,
                            String tag,
                            String content,
                            @NotNull UserReadDto author,
                            Boolean isPrivate) {
}
