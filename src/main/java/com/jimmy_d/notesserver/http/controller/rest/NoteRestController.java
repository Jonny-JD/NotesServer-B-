package com.jimmy_d.notesserver.http.controller.rest;

import com.jimmy_d.notesserver.dto.NoteCreateDto;
import com.jimmy_d.notesserver.dto.NoteFilter;
import com.jimmy_d.notesserver.dto.NoteReadDto;
import com.jimmy_d.notesserver.exceptions.rest.InvalidNoteQueryException;
import com.jimmy_d.notesserver.exceptions.rest.NoteNotFoundException;
import com.jimmy_d.notesserver.service.NoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@Slf4j  // Ломбок добавит поле log
@RestController
@RequestMapping("/api/v1/notes")
@RequiredArgsConstructor
public class NoteRestController {

    private final NoteService noteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NoteReadDto save(@RequestBody NoteCreateDto note) {
        return noteService.save(note);
    }

    @GetMapping("/{id}")
    public NoteReadDto getById(@PathVariable Long id) {
        return noteService.findById(id)
                .orElseThrow(() -> new NoteNotFoundException("id", id));
    }

    @GetMapping("/all-by-tag/{tag}")
    public List<NoteReadDto> getAllByTag(@PathVariable String tag) {
        return noteService.findAllByTag(tag);
    }

    @GetMapping("/all-by-author-id/{authorId}")
    public List<NoteReadDto> getAllByAuthor(@PathVariable Long authorId) {
        return noteService.findAllByAuthorId(authorId);
    }

    @GetMapping
    public List<NoteReadDto> getNotes(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) Long authorId
    ) {
        if (title == null && tag == null && content == null && authorId == null) {
            throw new InvalidNoteQueryException();
        }
        return noteService.findAllByFilter(new NoteFilter(title, tag, content, authorId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.jimmy_d.notesserver.database.entity.Role).ADMIN) or @accessChecker.isNoteOwner(#id)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!noteService.deleteById(id)) {
            throw new NoteNotFoundException("id", id);
        }
        
    }

    @DeleteMapping("/all-by-tag/{tag}")
    @PreAuthorize("hasAuthority(T(com.jimmy_d.notesserver.database.entity.Role).ADMIN)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllByTag(@PathVariable String tag) {
        if (!noteService.deleteAllByTag(tag)) {
            throw new NoteNotFoundException("tag", tag);
        }
        
    }

    @DeleteMapping("/all-by-author/{authorId}")
    @PreAuthorize("hasAuthority(T(com.jimmy_d.notesserver.database.entity.Role).ADMIN)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllByAuthorId(@PathVariable Long authorId) {
        if (!noteService.deleteAllByAuthor(authorId)) {
            throw new NoteNotFoundException("author id", authorId);
        }
        
    }

    @GetMapping("/fresh")
    public List<NoteReadDto> getFreshNotes(@RequestParam Instant from) {
        return noteService.getNextNotes(from);
    }
}
