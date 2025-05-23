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
        log.info("Saving new note with title: {}", note.title());
        NoteReadDto savedNote = noteService.save(note);
        log.info("Note saved with id: {}", savedNote.id());
        return savedNote;
    }

    @GetMapping("/{id}")
    public NoteReadDto getById(@PathVariable Long id) {
        log.info("Fetching note by id: {}", id);
        return noteService.findById(id)
                .orElseThrow(() -> {
                    log.warn("Note not found with id: {}", id);
                    return new NoteNotFoundException("id", id);
                });
    }

    @GetMapping("/all-by-tag/{tag}")
    public List<NoteReadDto> getAllByTag(@PathVariable String tag) {
        log.info("Fetching all notes with tag: {}", tag);
        return noteService.findAllByTag(tag);
    }

    @GetMapping("/all-by-author-id/{authorId}")
    public List<NoteReadDto> getAllByAuthor(@PathVariable Long authorId) {
        log.info("Fetching all notes by authorId: {}", authorId);
        return noteService.findAllByAuthorId(authorId);
    }

    @GetMapping
    public List<NoteReadDto> getNotes(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) Long authorId
    ) {
        log.info("Fetching notes with filters - title: {}, tag: {}, content: {}, authorId: {}", title, tag, content, authorId);
        if (title == null && tag == null && content == null && authorId == null) {
            log.warn("Invalid note query: all filters are null");
            throw new InvalidNoteQueryException();
        }
        return noteService.findAllByFilter(new NoteFilter(title, tag, content, authorId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.jimmy_d.notesserver.database.entity.Role).ADMIN) or @accessChecker.isNoteOwner(#id)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        log.info("Request to delete note with id: {}", id);
        if (!noteService.deleteById(id)) {
            log.warn("Note to delete not found with id: {}", id);
            throw new NoteNotFoundException("id", id);
        }
        log.info("Note deleted with id: {}", id);
    }

    @DeleteMapping("/all-by-tag/{tag}")
    @PreAuthorize("hasAuthority(T(com.jimmy_d.notesserver.database.entity.Role).ADMIN)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllByTag(@PathVariable String tag) {
        log.info("Request to delete all notes with tag: {}", tag);
        if (!noteService.deleteAllByTag(tag)) {
            log.warn("No notes found to delete with tag: {}", tag);
            throw new NoteNotFoundException("tag", tag);
        }
        log.info("All notes deleted with tag: {}", tag);
    }

    @DeleteMapping("/all-by-author/{authorId}")
    @PreAuthorize("hasAuthority(T(com.jimmy_d.notesserver.database.entity.Role).ADMIN)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllByAuthorId(@PathVariable Long authorId) {
        log.info("Request to delete all notes by authorId: {}", authorId);
        if (!noteService.deleteAllByAuthor(authorId)) {
            log.warn("No notes found to delete by authorId: {}", authorId);
            throw new NoteNotFoundException("author id", authorId);
        }
        log.info("All notes deleted by authorId: {}", authorId);
    }

    @GetMapping("/fresh")
    public List<NoteReadDto> getFreshNotes(@RequestParam Instant from) {
        log.info("Fetching fresh notes from: {}", from);
        return noteService.getNextNotes(from);
    }
}
