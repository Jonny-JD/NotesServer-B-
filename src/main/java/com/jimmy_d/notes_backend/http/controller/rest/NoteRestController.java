package com.jimmy_d.notes_backend.http.controller.rest;

import com.jimmy_d.notes_backend.dto.*;
import com.jimmy_d.notes_backend.exceptions.rest.NoteNotFoundException;
import com.jimmy_d.notes_backend.security.CustomUserDetails;
import com.jimmy_d.notes_backend.service.NoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/notes")
@RequiredArgsConstructor
public class NoteRestController {

    private final NoteService noteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NoteReadDto save(@RequestBody @Validated NoteCreateDto note) {
        return noteService.save(note);
    }

    @GetMapping("/{id}")
    public NoteReadDto getById(@PathVariable UUID id) {
        return noteService.findById(id)
                .orElseThrow(() -> new NoteNotFoundException("id", id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("@accessChecker.isNoteOwner(#id)")
    @ResponseStatus(HttpStatus.OK)
    public NoteReadDto updateNote(@PathVariable UUID id, @RequestBody @Validated NoteUpdateDto note) {
        return noteService.updateNote(id, note);
    }

    @GetMapping("/user-notes")
    public List<NotePreviewDto> getUserNotes(@AuthenticationPrincipal CustomUserDetails user, @RequestParam Instant from) {
        return noteService.findAllPreviewByFilter(new NotePreviewFilter(null, null, user.getId()), from);
    }


    @GetMapping("/search")
    public List<NotePreviewDto> getByFilter(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) Long authorId,
            @RequestParam Instant from) {
        return noteService.findAllPreviewByFilter(new NotePreviewFilter(title, tag, authorId), from);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.jimmy_d.notes_backend.database.entity.Role).ADMIN) or @accessChecker.isNoteOwner(#id)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        if (!noteService.deleteById(id)) {
            throw new NoteNotFoundException("id", id);
        }
    }
}
