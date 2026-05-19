package com.jimmy_d.notes_backend.http.controller.rest;


import com.jimmy_d.notes_backend.dto.NoteReadDto;
import com.jimmy_d.notes_backend.service.NoteService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/notes")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasAuthority(T(com.jimmy_d.notes_backend.database.entity.Role).ADMIN)")
public class AdminNoteController {

    private final NoteService noteService;

    @GetMapping("/all-by-tag/{tag}")
    public List<NoteReadDto> getAllByTag(@PathVariable String tag) {
        return noteService.findAllByTag(tag);
    }

    @GetMapping("/all-by-author/{authorId}")
    public List<NoteReadDto> getAllByAuthor(@PathVariable @Positive Long authorId) {
        return noteService.findAllByAuthorId(authorId);
    }

    @DeleteMapping("/all-by-tag/{tag}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllByTag(@PathVariable String tag) {
        noteService.deleteAllByTag(tag);
    }

    @DeleteMapping("/all-by-author/{authorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllByAuthorId(@PathVariable Long authorId) {
        noteService.deleteAllByAuthor(authorId);

    }
}
