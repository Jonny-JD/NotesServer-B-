package com.jimmy_d.notes_backend.http.controller.rest;


import com.jimmy_d.notes_backend.dto.NoteReadDto;
import com.jimmy_d.notes_backend.exceptions.rest.NoteNotFoundException;
import com.jimmy_d.notes_backend.service.NoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admin/notes")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority(T(com.jimmy_d.notes_backend.database.entity.Role).ADMIN)")
public class AdminNoteController {

    private final NoteService noteService;

    @GetMapping("/all-by-tag/{tag}")
    public List<NoteReadDto> getAllByTag(@PathVariable String tag) {
        return noteService.findAllByTag(tag);
    }

    @GetMapping("/all-by-author-id/{authorId}")
    public List<NoteReadDto> getAllByAuthor(@PathVariable Long authorId) {
        return noteService.findAllByAuthorId(authorId);
    }

    @DeleteMapping("/all-by-tag/{tag}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllByTag(@PathVariable String tag) {
        if (!noteService.deleteAllByTag(tag)) {
            throw new NoteNotFoundException("tag", tag);
        }
    }

    @DeleteMapping("/all-by-author/{authorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllByAuthorId(@PathVariable Long authorId) {
        if (!noteService.deleteAllByAuthor(authorId)) {
            throw new NoteNotFoundException("author id", authorId);
        }
    }
}
